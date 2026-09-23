package com.cnstrokesdict.app.util

import com.cnstrokesdict.app.data.TextbookWordEntry

/**
 * 听写管理器
 */
class DictationManager {

    /**
     * 听写模式
     */
    enum class DictationMode {
        SEQUENTIAL,  // 顺序听写
        RANDOM,      // 随机听写
        ERROR_ONLY,  // 错词复习
    }

    private val words = mutableListOf<TextbookWordEntry>()
    private var currentIndex = 0
    private val errors = mutableListOf<TextbookWordEntry>()
    private var mode = DictationMode.SEQUENTIAL
    private var speed = 1.0f
    private var repeatCount = 1
    private var intervalMs = 3000L

    /**
     * 开始听写
     * @param wordList 词语列表
     * @param mode 听写模式
     * @param speed 语速
     * @param repeatCount 重复次数
     * @param intervalMs 间隔时间（毫秒）
     */
    fun start(
        wordList: List<TextbookWordEntry>,
        mode: DictationMode = DictationMode.SEQUENTIAL,
        speed: Float = 1.0f,
        repeatCount: Int = 1,
        intervalMs: Long = 3000L,
    ) {
        this.words.clear()
        this.mode = mode
        this.speed = speed
        this.repeatCount = repeatCount
        this.intervalMs = intervalMs
        this.errors.clear()

        when (mode) {
            DictationMode.SEQUENTIAL -> {
                this.words.addAll(wordList)
            }
            DictationMode.RANDOM -> {
                this.words.addAll(wordList.shuffled())
            }
            DictationMode.ERROR_ONLY -> {
                // 错词复习模式需要先有错误记录
                this.words.addAll(errors)
                errors.clear()
            }
        }

        currentIndex = 0
    }

    /**
     * 获取下一个词语
     * @return 下一个词语，如果没有则返回null
     */
    fun next(): TextbookWordEntry? {
        if (currentIndex >= words.size) return null
        return words[currentIndex++]
    }

    /**
     * 标记当前词语为错误
     * @param word 词语
     */
    fun markError(word: TextbookWordEntry) {
        errors.add(word)
    }

    /**
     * 标记当前词语为正确
     * @param word 词语
     */
    fun markCorrect(word: TextbookWordEntry) {
        // 从错误列表中移除（如果有）
        errors.removeAll { it.word == word.word }
    }

    /**
     * 获取错误列表
     */
    fun getErrors(): List<TextbookWordEntry> = errors.toList()

    /**
     * 获取进度
     * @return Pair<当前进度, 总数>
     */
    fun getProgress(): Pair<Int, Int> = Pair(currentIndex, words.size)

    /**
     * 是否还有更多词语
     */
    fun hasNext(): Boolean = currentIndex < words.size

    /**
     * 获取当前词语列表
     */
    fun getCurrentWords(): List<TextbookWordEntry> = words.toList()

    /**
     * 获取语速
     */
    fun getSpeed(): Float = speed

    /**
     * 获取间隔时间
     */
    fun getIntervalMs(): Long = intervalMs

    /**
     * 获取重复次数
     */
    fun getRepeatCount(): Int = repeatCount

    /**
     * 获取听写模式
     */
    fun getMode(): DictationMode = mode
}
