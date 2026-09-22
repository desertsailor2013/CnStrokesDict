package com.cnstrokesdict.app.util

import android.app.ActivityManager
import android.content.Context
import android.os.Debug
import android.util.Log

/**
 * 内存监控工具类
 */
object MemoryMonitor {
    private const val TAG = "MemoryMonitor"

    /**
     * 获取当前应用内存使用情况
     * @param context 上下文
     * @return 内存使用信息
     */
    fun getMemoryInfo(context: Context): MemoryInfo {
        val runtime = Runtime.getRuntime()
        val maxMemory = runtime.maxMemory()
        val totalMemory = runtime.totalMemory()
        val freeMemory = runtime.freeMemory()
        val usedMemory = totalMemory - freeMemory

        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val memoryInfo = ActivityManager.MemoryInfo()
        activityManager.getMemoryInfo(memoryInfo)

        return MemoryInfo(
            maxMemory = maxMemory,
            totalMemory = totalMemory,
            freeMemory = freeMemory,
            usedMemory = usedMemory,
            availableMemory = memoryInfo.availMem,
            lowMemory = memoryInfo.lowMemory,
            threshold = memoryInfo.threshold,
        )
    }

    /**
     * 获取Native堆内存信息
     * @return Native堆内存信息
     */
    fun getNativeHeapInfo(): NativeHeapInfo {
        val nativeHeapSize = Debug.getNativeHeapSize()
        val nativeHeapAllocated = Debug.getNativeHeapAllocatedSize()
        val nativeHeapFree = Debug.getNativeHeapFreeSize()

        return NativeHeapInfo(
            size = nativeHeapSize,
            allocated = nativeHeapAllocated,
            free = nativeHeapFree,
        )
    }

    /**
     * 打印内存使用情况
     * @param context 上下文
     */
    fun logMemoryUsage(context: Context) {
        val memoryInfo = getMemoryInfo(context)
        val nativeHeapInfo = getNativeHeapInfo()

        Log.d(TAG, "=== 内存使用情况 ===")
        Log.d(TAG, "JVM堆内存:")
        Log.d(TAG, "  最大: ${memoryInfo.maxMemory / 1024 / 1024}MB")
        Log.d(TAG, "  已用: ${memoryInfo.usedMemory / 1024 / 1024}MB")
        Log.d(TAG, "  空闲: ${memoryInfo.freeMemory / 1024 / 1024}MB")
        Log.d(TAG, "Native堆内存:")
        Log.d(TAG, "  大小: ${nativeHeapInfo.size / 1024 / 1024}MB")
        Log.d(TAG, "  已用: ${nativeHeapInfo.allocated / 1024 / 1024}MB")
        Log.d(TAG, "  空闲: ${nativeHeapInfo.free / 1024 / 1024}MB")
        Log.d(TAG, "系统内存:")
        Log.d(TAG, "  可用: ${memoryInfo.availableMemory / 1024 / 1024}MB")
        Log.d(TAG, "  低内存: ${memoryInfo.lowMemory}")
    }

    /**
     * 检查是否内存不足
     * @param context 上下文
     * @return 是否内存不足
     */
    fun isLowMemory(context: Context): Boolean {
        val memoryInfo = getMemoryInfo(context)
        val usedMemoryPercentage = (memoryInfo.usedMemory.toDouble() / memoryInfo.maxMemory) * 100
        return memoryInfo.lowMemory || usedMemoryPercentage > 80
    }

    data class MemoryInfo(
        val maxMemory: Long,
        val totalMemory: Long,
        val freeMemory: Long,
        val usedMemory: Long,
        val availableMemory: Long,
        val lowMemory: Boolean,
        val threshold: Long,
    )

    data class NativeHeapInfo(
        val size: Long,
        val allocated: Long,
        val free: Long,
    )
}
