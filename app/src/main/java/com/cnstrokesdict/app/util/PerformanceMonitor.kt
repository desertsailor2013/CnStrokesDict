package com.cnstrokesdict.app.util

import android.os.SystemClock
import android.util.Log

/**
 * 性能监控工具类
 */
object PerformanceMonitor {
    private const val TAG = "PerformanceMonitor"

    private val timings = mutableMapOf<String, MutableList<Long>>()

    /**
     * 开始计时
     * @param label 计时标签
     * @return 开始时间
     */
    fun startTiming(label: String): Long {
        return SystemClock.elapsedRealtime()
    }

    /**
     * 结束计时并记录
     * @param label 计时标签
     * @param startTime 开始时间
     */
    fun endTiming(label: String, startTime: Long) {
        val endTime = SystemClock.elapsedRealtime()
        val duration = endTime - startTime

        timings.getOrPut(label) { mutableListOf() }.add(duration)

        Log.d(TAG, "$label: ${duration}ms")
    }

    /**
     * 获取平均耗时
     * @param label 计时标签
     * @return 平均耗时（毫秒）
     */
    fun getAverageTiming(label: String): Double {
        val times = timings[label] ?: return 0.0
        return times.average()
    }

    /**
     * 获取最大耗时
     * @param label 计时标签
     * @return 最大耗时（毫秒）
     */
    fun getMaxTiming(label: String): Long {
        val times = timings[label] ?: return 0L
        return times.maxOrNull() ?: 0L
    }

    /**
     * 获取最小耗时
     * @param label 计时标签
     * @return 最小耗时（毫秒）
     */
    fun getMinTiming(label: String): Long {
        val times = timings[label] ?: return 0L
        return times.minOrNull() ?: 0L
    }

    /**
     * 获取所有计时统计
     * @return 统计信息字符串
     */
    fun getStatistics(): String {
        return buildString {
            appendLine("=== 性能统计 ===")
            timings.forEach { (label, times) ->
                appendLine("$label:")
                appendLine("  次数: ${times.size}")
                appendLine("  平均: ${times.average().toInt()}ms")
                appendLine("  最大: ${times.maxOrNull() ?: 0}ms")
                appendLine("  最小: ${times.minOrNull() ?: 0}ms")
            }
        }
    }

    /**
     * 清除所有计时数据
     */
    fun clear() {
        timings.clear()
    }

    /**
     * 测量代码块执行时间
     * @param label 计时标签
     * @param block 要测量的代码块
     * @return 代码块的返回值
     */
    inline fun <T> measureTime(label: String, block: () -> T): T {
        val startTime = startTiming(label)
        try {
            return block()
        } finally {
            endTiming(label, startTime)
        }
    }
}
