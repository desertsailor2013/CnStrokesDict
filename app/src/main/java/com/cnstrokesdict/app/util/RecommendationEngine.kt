package com.cnstrokesdict.app.util

import android.content.Context
import com.cnstrokesdict.app.data.db.DictionaryDatabase
import com.cnstrokesdict.app.data.db.LearningRecordEntity
import com.cnstrokesdict.app.data.db.WordEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

/**
 * 推荐引擎
 * 基于遗忘曲线算法智能推荐复习内容
 */
class RecommendationEngine(private val context: Context) {
    private val db = DictionaryDatabase.getInstance(context)
    private val learningDao = db.learningDao()
    private val wordDao = db.wordDao()
    private val spacedRepetition = SpacedRepetition()

    /**
     * 获取推荐复习内容
     * @param limit 推荐数量
     * @return 推荐列表
     */
    suspend fun getRecommendations(limit: Int = 10): List<Recommendation> = withContext(Dispatchers.IO) {
        val recommendations = mutableListOf<Recommendation>()

        // 1. 获取需要复习的词语（基于遗忘曲线）
        val dueItems = getDueReviewItems(limit)
        recommendations.addAll(dueItems)

        // 2. 获取错误频率高的词语
        if (recommendations.size < limit) {
            val errorItems = getFrequentErrors(limit - recommendations.size)
            recommendations.addAll(errorItems)
        }

        // 3. 获取新词语
        if (recommendations.size < limit) {
            val newItems = getNewItems(limit - recommendations.size)
            recommendations.addAll(newItems)
        }

        // 去重并按优先级排序
        recommendations.distinctBy { it.word }
            .sortedByDescending { it.priority }
            .take(limit)
    }

    /**
     * 获取需要复习的词语
     */
    private suspend fun getDueReviewItems(limit: Int): List<Recommendation> {
        val now = System.currentTimeMillis()
        val records = learningDao.getRecentRecords(100)

        // 按词语分组，获取最新的学习记录
        val latestRecords = records.groupBy { it.word }
            .mapValues { (_, records) -> records.maxByOrNull { it.createdAt }!! }

        return latestRecords.values
            .filter { record ->
                // 计算记忆强度
                val daysSinceLastReview = TimeUnit.MILLISECONDS.toDays(now - record.createdAt).toInt()
                val memoryStrength = spacedRepetition.calculateMemoryStrength(
                    repetition = record.type.toIntOrNull() ?: 0,
                    daysSinceLastReview = daysSinceLastReview,
                )
                // 需要复习
                spacedRepetition.needsReview(memoryStrength)
            }
            .sortedByDescending { record ->
                // 按优先级排序
                val daysSinceLastReview = TimeUnit.MILLISECONDS.toDays(now - record.createdAt).toInt()
                val memoryStrength = spacedRepetition.calculateMemoryStrength(
                    repetition = record.type.toIntOrNull() ?: 0,
                    daysSinceLastReview = daysSinceLastReview,
                )
                spacedRepetition.calculatePriority(
                    memoryStrength = memoryStrength,
                    errorCount = if (record.isCorrect == 0) 1 else 0,
                    daysSinceLastReview = daysSinceLastReview,
                )
            }
            .take(limit)
            .map { record ->
                val daysSinceLastReview = TimeUnit.MILLISECONDS.toDays(now - record.createdAt).toInt()
                val memoryStrength = spacedRepetition.calculateMemoryStrength(
                    repetition = record.type.toIntOrNull() ?: 0,
                    daysSinceLastReview = daysSinceLastReview,
                )
                Recommendation(
                    word = record.word,
                    type = "review",
                    reason = "需要复习",
                    priority = spacedRepetition.calculatePriority(
                        memoryStrength = memoryStrength,
                        errorCount = if (record.isCorrect == 0) 1 else 0,
                        daysSinceLastReview = daysSinceLastReview,
                    ),
                )
            }
    }

    /**
     * 获取错误频率高的词语
     */
    private suspend fun getFrequentErrors(limit: Int): List<Recommendation> {
        val errorRecords = learningDao.getFrequentErrors(limit)
        return errorRecords.map { error ->
            Recommendation(
                word = error.word,
                type = "error",
                reason = "错误${error.errorCount}次",
                priority = error.errorCount * 10.0,
            )
        }
    }

    /**
     * 获取新词语
     */
    private suspend fun getNewItems(limit: Int): List<Recommendation> {
        // 获取所有词语
        val allWords = wordDao.getAllWords()
        // 获取已学习的词语
        val learnedWords = learningDao.getLearnedWords()
        // 过滤出未学习的词语
        val newWords = allWords.filter { it.word !in learnedWords }
        return newWords.take(limit).map { word ->
            Recommendation(
                word = word.word,
                type = "new",
                reason = "新词语",
                priority = 5.0,
            )
        }
    }

    /**
     * 记录学习结果
     * @param word 词语
     * @param isCorrect 是否正确
     * @param timeSpent 学习时长（毫秒）
     */
    suspend fun recordLearning(
        word: String,
        isCorrect: Boolean,
        timeSpent: Long = 0,
    ) = withContext(Dispatchers.IO) {
        val record = LearningRecordEntity(
            word = word,
            type = if (isCorrect) "correct" else "error",
            isCorrect = if (isCorrect) 1 else 0,
            timeSpent = timeSpent,
        )
        learningDao.insertRecord(record)
    }

    /**
     * 获取学习统计
     */
    suspend fun getLearningStats(): LearningStats = withContext(Dispatchers.IO) {
        val totalWords = wordDao.getWordCount()
        val learnedWords = learningDao.getLearnedWordCount()
        val correctCount = learningDao.getCorrectRecordCount()
        val totalCount = learningDao.getTotalRecordCount()

        val accuracy = if (totalCount > 0) {
            correctCount.toFloat() / totalCount
        } else {
            0f
        }

        val masteredCount = learningDao.getMasteredWordCount()

        LearningStats(
            totalWords = totalWords,
            learnedWords = learnedWords,
            masteredCount = masteredCount,
            accuracy = accuracy,
            totalRecords = totalCount,
        )
    }
}

/**
 * 推荐项
 */
data class Recommendation(
    val word: String,
    val type: String, // 'review', 'error', 'new'
    val reason: String,
    val priority: Double,
)

/**
 * 学习统计
 */
data class LearningStats(
    val totalWords: Int,
    val learnedWords: Int,
    val masteredCount: Int,
    val accuracy: Float,
    val totalRecords: Int,
)
