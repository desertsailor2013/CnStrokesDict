package com.cnstrokesdict.app.data

import android.content.Context
import com.cnstrokesdict.app.data.db.DictionaryDatabase
import com.cnstrokesdict.app.data.db.IdiomEntity
import com.cnstrokesdict.app.data.db.LearningRecordEntity
import com.cnstrokesdict.app.data.db.LearningStatsEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * 成语词典仓库
 */
class IdiomRepository(private val context: Context) {
    private val db = DictionaryDatabase.getInstance(context)
    private val idiomDao = db.idiomDao()
    private val learningDao = db.learningDao()
    private val json = Json { ignoreUnknownKeys = true }

    /**
     * 从assets加载成语数据
     */
    suspend fun loadIdiomsFromAssets(): Boolean = withContext(Dispatchers.IO) {
        try {
            val jsonString = context.assets.open("dictionary/idioms.json")
                .bufferedReader()
                .use { it.readText() }

            val root = json.decodeFromString<IdiomDictRoot>(jsonString)
            val entities = root.idioms.map { entry ->
                IdiomEntity(
                    idiom = entry.idiom,
                    pinyin = entry.pinyin,
                    meaning = entry.meaning,
                    source = entry.source,
                    examples = json.encodeToString(ListSerializer(String.serializer()), entry.examples),
                    synonyms = json.encodeToString(ListSerializer(String.serializer()), entry.synonyms),
                    antonyms = json.encodeToString(ListSerializer(String.serializer()), entry.antonyms),
                    category = entry.category,
                    difficulty = entry.difficulty,
                )
            }

            idiomDao.insertIdioms(entities)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * 搜索成语
     */
    suspend fun searchIdioms(query: String, limit: Int = 20): List<IdiomEntity> =
        withContext(Dispatchers.IO) {
            idiomDao.searchIdioms(query, limit)
        }

    /**
     * 按分类获取成语
     */
    suspend fun getIdiomsByCategory(category: String, limit: Int = 20): List<IdiomEntity> =
        withContext(Dispatchers.IO) {
            idiomDao.getIdiomsByCategory(category, limit)
        }

    /**
     * 按难度获取成语
     */
    suspend fun getIdiomsByDifficulty(difficulty: Int, limit: Int = 20): List<IdiomEntity> =
        withContext(Dispatchers.IO) {
            idiomDao.getIdiomsByDifficulty(difficulty, limit)
        }

    /**
     * 获取随机成语
     */
    suspend fun getRandomIdioms(limit: Int = 10): List<IdiomEntity> =
        withContext(Dispatchers.IO) {
            idiomDao.getRandomIdioms(limit)
        }

    /**
     * 获取成语数量
     */
    suspend fun getIdiomCount(): Int = withContext(Dispatchers.IO) {
        idiomDao.getIdiomCount()
    }

    /**
     * 获取分类统计
     */
    suspend fun getCategoryStats(): List<CategoryStat> = withContext(Dispatchers.IO) {
        idiomDao.getCategoryStats()
    }

    /**
     * 记录学习结果
     */
    suspend fun recordLearning(word: String, type: String, isCorrect: Boolean, timeSpent: Long = 0) =
        withContext(Dispatchers.IO) {
            val record = LearningRecordEntity(
                word = word,
                type = type,
                isCorrect = if (isCorrect) 1 else 0,
                timeSpent = timeSpent,
            )
            learningDao.insertRecord(record)
        }

    /**
     * 获取错误记录
     */
    suspend fun getErrorRecords(limit: Int = 20): List<LearningRecordEntity> =
        withContext(Dispatchers.IO) {
            learningDao.getErrorRecords(limit)
        }

    /**
     * 获取常见错误
     */
    suspend fun getFrequentErrors(limit: Int = 20): List<FrequentError> =
        withContext(Dispatchers.IO) {
            learningDao.getFrequentErrors(limit)
        }

    /**
     * 更新每日统计
     */
    suspend fun updateDailyStats(isCorrect: Boolean, timeSpent: Long = 0) =
        withContext(Dispatchers.IO) {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val today = dateFormat.format(Date())

            val existingStats = learningDao.getStatsByDate(today)
            if (existingStats != null) {
                val updatedStats = existingStats.copy(
                    totalCount = existingStats.totalCount + 1,
                    correctCount = existingStats.correctCount + if (isCorrect) 1 else 0,
                    errorCount = existingStats.errorCount + if (isCorrect) 0 else 1,
                    timeSpent = existingStats.timeSpent + timeSpent,
                )
                learningDao.insertStats(updatedStats)
            } else {
                val newStats = LearningStatsEntity(
                    date = today,
                    totalCount = 1,
                    correctCount = if (isCorrect) 1 else 0,
                    errorCount = if (isCorrect) 0 else 1,
                    timeSpent = timeSpent,
                )
                learningDao.insertStats(newStats)
            }
        }

    /**
     * 获取最近统计
     */
    suspend fun getRecentStats(limit: Int = 7): List<LearningStatsEntity> =
        withContext(Dispatchers.IO) {
            learningDao.getRecentStats(limit)
        }
}
