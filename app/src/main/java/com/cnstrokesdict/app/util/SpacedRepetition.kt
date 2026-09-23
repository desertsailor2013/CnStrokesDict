package com.cnstrokesdict.app.util

/**
 * 间隔重复算法（艾宾浩斯遗忘曲线）
 * 用于智能推荐复习内容
 */
class SpacedRepetition {

    companion object {
        // 默认难度因子
        const val DEFAULT_EASE_FACTOR = 2.5

        // 最小难度因子
        const val MIN_EASE_FACTOR = 1.3

        // 最大复习间隔（天）
        const val MAX_INTERVAL = 365

        // 答题质量等级
        const val QUALITY_BLACKOUT = 0      // 完全不记得
        const val QUALITY_INCORRECT = 1    // 错误，但看到答案后想起
        const val QUALITY_HARD = 2         // 错误，但看到答案后觉得简单
        const val QUALITY_GOOD = 3         // 正确，但有些犹豫
        const val QUALITY_EASY = 4         // 正确，毫不犹豫
        const val QUALITY_PERFECT = 5      // 完美，非常容易
    }

    /**
     * 计算下次复习时间
     * @param repetition 复习次数
     * @param easeFactor 难度因子
     * @param interval 间隔天数
     * @return 下次复习时间（天）
     */
    fun calculateNextReview(
        repetition: Int,
        easeFactor: Double = DEFAULT_EASE_FACTOR,
        interval: Int = 0,
    ): Double {
        return when (repetition) {
            0 -> 1.0          // 第一次复习：1天后
            1 -> 6.0          // 第二次复习：6天后
            else -> {
                // 之后：间隔 * 难度因子
                val newInterval = interval * easeFactor
                newInterval.coerceAtMost(MAX_INTERVAL.toDouble())
            }
        }
    }

    /**
     * 更新难度因子
     * @param quality 答题质量（0-5）
     * @param easeFactor 当前难度因子
     * @return 新的难度因子
     */
    fun updateEaseFactor(quality: Int, easeFactor: Double = DEFAULT_EASE_FACTOR): Double {
        val newFactor = easeFactor + (0.1 - (5 - quality) * (0.08 + (5 - quality) * 0.02))
        return newFactor.coerceAtLeast(MIN_EASE_FACTOR)
    }

    /**
     * 更新复习记录
     * @param quality 答题质量（0-5）
     * @param repetition 当前复习次数
     * @param easeFactor 当前难度因子
     * @param interval 当前间隔天数
     * @return UpdateResult 更新后的结果
     */
    fun updateReview(
        quality: Int,
        repetition: Int,
        easeFactor: Double = DEFAULT_EASE_FACTOR,
        interval: Int = 0,
    ): UpdateResult {
        val newEaseFactor = updateEaseFactor(quality, easeFactor)
        val newRepetition: Int
        val newInterval: Int

        if (quality >= 3) {
            // 答对了
            newRepetition = repetition + 1
            newInterval = calculateNextReview(newRepetition, newEaseFactor, interval).toInt()
        } else {
            // 答错了，重新开始
            newRepetition = 0
            newInterval = 1
        }

        return UpdateResult(
            repetition = newRepetition,
            easeFactor = newEaseFactor,
            interval = newInterval,
            nextReviewTime = System.currentTimeMillis() + newInterval * 24 * 60 * 60 * 1000L,
        )
    }

    /**
     * 计算遗忘因子
     * @param daysSinceLastReview 距离上次复习的天数
     * @param easeFactor 难度因子
     * @return 遗忘因子（0-1，越小越容易忘记）
     */
    fun calculateForgettingFactor(
        daysSinceLastReview: Int,
        easeFactor: Double = DEFAULT_EASE_FACTOR,
    ): Double {
        return Math.exp(-0.1 * daysSinceLastReview / easeFactor)
    }

    /**
     * 计算记忆强度
     * @param repetition 复习次数
     * @param easeFactor 难度因子
     * @param daysSinceLastReview 距离上次复习的天数
     * @return 记忆强度（0-1，越大记忆越牢固）
     */
    fun calculateMemoryStrength(
        repetition: Int,
        easeFactor: Double = DEFAULT_EASE_FACTOR,
        daysSinceLastReview: Int = 0,
    ): Double {
        val baseStrength = when (repetition) {
            0 -> 0.0
            1 -> 0.3
            2 -> 0.5
            3 -> 0.7
            4 -> 0.85
            else -> 0.95
        }
        val forgettingFactor = calculateForgettingFactor(daysSinceLastReview, easeFactor)
        return (baseStrength * forgettingFactor).coerceIn(0.0, 1.0)
    }

    /**
     * 判断是否需要复习
     * @param memoryStrength 记忆强度
     * @param threshold 阈值（默认0.6）
     * @return 是否需要复习
     */
    fun needsReview(memoryStrength: Double, threshold: Double = 0.6): Boolean {
        return memoryStrength < threshold
    }

    /**
     * 计算优先级分数
     * @param memoryStrength 记忆强度
     * @param errorCount 错误次数
     * @param daysSinceLastReview 距离上次复习的天数
     * @return 优先级分数（越高越需要复习）
     */
    fun calculatePriority(
        memoryStrength: Double,
        errorCount: Int = 0,
        daysSinceLastReview: Int = 0,
    ): Double {
        val strengthScore = (1 - memoryStrength) * 50
        val errorScore = errorCount * 10.0
        val timeScore = daysSinceLastReview * 2.0
        return strengthScore + errorScore + timeScore
    }
}

/**
 * 更新结果
 */
data class UpdateResult(
    val repetition: Int,
    val easeFactor: Double,
    val interval: Int,
    val nextReviewTime: Long,
)

/**
 * 复习项目
 */
data class ReviewItem(
    val word: String,
    val type: String, // 'word' or 'idiom'
    val repetition: Int,
    val easeFactor: Double,
    val interval: Int,
    val nextReviewTime: Long,
    val lastReviewTime: Long,
    val errorCount: Int,
    val memoryStrength: Double,
    val priority: Double,
)
