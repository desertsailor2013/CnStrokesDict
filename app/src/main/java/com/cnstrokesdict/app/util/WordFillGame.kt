package com.cnstrokesdict.app.util

import android.content.Context
import com.cnstrokesdict.app.data.db.DictionaryDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * 词语填空游戏
 */
class WordFillGame(private val context: Context) {
    private val db = DictionaryDatabase.getInstance(context)
    private val wordDao = db.wordDao()
    private var currentQuestion: FillQuestion? = null
    private var score = 0
    private var correctCount = 0
    private var totalCount = 0

    /**
     * 获取填空题目
     * @return 填空题目
     */
    suspend fun getQuestion(): FillQuestion = withContext(Dispatchers.IO) {
        val words = wordDao.browseAllWords()
        val word = words.random()
        val chars = word.word.toCharArray()
        val hiddenIndex = (0 until chars.size).random()
        val hiddenChar = chars[hiddenIndex]

        currentQuestion = FillQuestion(
            sentence = "${word.meaning}",
            word = word.word,
            pinyin = word.pinyin,
            hiddenChar = hiddenChar.toString(),
            hiddenIndex = hiddenIndex,
            options = generateOptions(hiddenChar.toString(), words),
        )
        currentQuestion!!
    }

    /**
     * 生成选项
     */
    private fun generateOptions(correct: String, words: List<com.cnstrokesdict.app.data.WordListItem>): List<String> {
        val options = mutableSetOf(correct)
        while (options.size < 4) {
            val randomWord = words.random()
            if (randomWord.word.length >= 2) {
                options.add(randomWord.word.random().toString())
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
        val question = currentQuestion ?: return false
        val isCorrect = answer == question.hiddenChar
        totalCount++
        if (isCorrect) {
            correctCount++
            score += 10
        }
        return isCorrect
    }

    /**
     * 获取当前分数
     */
    fun getScore(): Int = score

    /**
     * 获取正确率
     */
    fun getAccuracy(): Float {
        return if (totalCount > 0) correctCount.toFloat() / totalCount else 0f
    }

    /**
     * 重置游戏
     */
    fun reset() {
        score = 0
        correctCount = 0
        totalCount = 0
        currentQuestion = null
    }
}

/**
 * 填空题目
 */
data class FillQuestion(
    val sentence: String,
    val word: String,
    val pinyin: String,
    val hiddenChar: String,
    val hiddenIndex: Int,
    val options: List<String>,
)
