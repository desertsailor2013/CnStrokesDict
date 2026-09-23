package com.cnstrokesdict.app.util

import android.content.Context
import com.cnstrokesdict.app.data.db.DictionaryDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * 成语猜谜游戏
 */
class IdiomQuizGame(private val context: Context) {
    private val db = DictionaryDatabase.getInstance(context)
    private val idiomDao = db.idiomDao()
    private var currentQuiz: QuizQuestion? = null
    private var score = 0
    private var correctCount = 0
    private var totalCount = 0
    private var streak = 0
    private var maxStreak = 0
    private var allIdioms: List<com.cnstrokesdict.app.data.db.IdiomEntity> = emptyList()

    /**
     * 初始化
     */
    suspend fun init() = withContext(Dispatchers.IO) {
        allIdioms = idiomDao.getAllIdioms()
    }

    /**
     * 获取猜谜题目
     * @return 猜谜题目
     */
    suspend fun getQuestion(): QuizQuestion = withContext(Dispatchers.IO) {
        if (allIdioms.isEmpty()) {
            allIdioms = idiomDao.getAllIdioms()
        }
        val idiom = allIdioms.random()
        val category = idiom.category

        currentQuiz = QuizQuestion(
            idiom = idiom.idiom,
            pinyin = idiom.pinyin,
            explanation = idiom.meaning,
            category = category,
            options = generateOptions(idiom.idiom),
        )
        currentQuiz!!
    }

    /**
     * 生成选项
     */
    private fun generateOptions(correct: String): List<String> {
        val options = mutableSetOf(correct)
        while (options.size < 4) {
            val randomIdiom = allIdioms.random()
            if (randomIdiom.idiom != correct) {
                options.add(randomIdiom.idiom)
            }
        }
        return options.shuffled()
    }

    /**
     * 检查答案
     * @param answer 用户答案
     * @return 是否正确
     */
    fun checkAnswer(answer: String): Boolean {
        val quiz = currentQuiz ?: return false
        val isCorrect = answer == quiz.idiom
        totalCount++
        if (isCorrect) {
            correctCount++
            streak++
            maxStreak = maxOf(maxStreak, streak)
            score += 10 + streak * 2
        } else {
            streak = 0
        }
        return isCorrect
    }

    fun getScore(): Int = score
    fun getAccuracy(): Float = if (totalCount > 0) correctCount.toFloat() / totalCount else 0f
    fun getStreak(): Int = streak
    fun getMaxStreak(): Int = maxStreak

    fun reset() {
        score = 0
        correctCount = 0
        totalCount = 0
        streak = 0
        maxStreak = 0
        currentQuiz = null
    }
}

data class QuizQuestion(
    val idiom: String,
    val pinyin: String,
    val explanation: String,
    val category: String,
    val options: List<String>,
)
