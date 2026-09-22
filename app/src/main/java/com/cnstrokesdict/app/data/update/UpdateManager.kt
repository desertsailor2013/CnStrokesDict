package com.cnstrokesdict.app.data.update

import android.content.Context
import com.cnstrokesdict.app.data.remote.NetworkClient
import com.cnstrokesdict.app.data.remote.UpdateCheckResponse
import com.cnstrokesdict.app.data.remote.UpdateDataResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

/**
 * 更新管理器
 */
class UpdateManager(private val context: Context) {

    companion object {
        private const val CURRENT_VERSION = "1.0.0"
        private const val UPDATE_DIR = "updates"
        private const val UPDATE_FILE = "update.json"
    }

    private val api = NetworkClient.updateApi

    /**
     * 获取当前版本
     */
    fun getCurrentVersion(): String = CURRENT_VERSION

    /**
     * 检查更新
     * @return 更新检查响应
     */
    suspend fun checkUpdate(): UpdateCheckResponse = withContext(Dispatchers.IO) {
        try {
            api.checkUpdate(CURRENT_VERSION)
        } catch (e: Exception) {
            UpdateCheckResponse(
                hasUpdate = false,
                latestVersion = CURRENT_VERSION,
            )
        }
    }

    /**
     * 下载更新
     * @param version 目标版本
     * @return 更新数据响应
     */
    suspend fun downloadUpdate(version: String): UpdateDataResponse = withContext(Dispatchers.IO) {
        try {
            val response = api.downloadUpdate(version = version)
            saveUpdateData(response)
            response
        } catch (e: Exception) {
            throw e
        }
    }

    /**
     * 保存更新数据到本地
     */
    private suspend fun saveUpdateData(data: UpdateDataResponse) = withContext(Dispatchers.IO) {
        val updateDir = File(context.filesDir, UPDATE_DIR)
        if (!updateDir.exists()) {
            updateDir.mkdirs()
        }

        val updateFile = File(updateDir, UPDATE_FILE)
        val json = kotlinx.serialization.json.Json {
            prettyPrint = true
            ignoreUnknownKeys = true
        }
        val jsonString = json.encodeToString(UpdateDataResponse.serializer(), data)
        FileOutputStream(updateFile).use { out ->
            out.write(jsonString.toByteArray())
        }
    }

    /**
     * 检查是否有本地更新
     */
    fun hasLocalUpdate(): Boolean {
        val updateDir = File(context.filesDir, UPDATE_DIR)
        val updateFile = File(updateDir, UPDATE_FILE)
        return updateFile.exists()
    }

    /**
     * 读取本地更新数据
     */
    suspend fun readLocalUpdate(): UpdateDataResponse? = withContext(Dispatchers.IO) {
        try {
            val updateDir = File(context.filesDir, UPDATE_DIR)
            val updateFile = File(updateDir, UPDATE_FILE)
            if (!updateFile.exists()) return@withContext null

            val jsonString = updateFile.readText()
            val json = kotlinx.serialization.json.Json {
                ignoreUnknownKeys = true
                isLenient = true
            }
            json.decodeFromString(UpdateDataResponse.serializer(), jsonString)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * 清除更新数据
     */
    suspend fun clearUpdateData() = withContext(Dispatchers.IO) {
        val updateDir = File(context.filesDir, UPDATE_DIR)
        if (updateDir.exists()) {
            updateDir.deleteRecursively()
        }
    }

    /**
     * 获取上次检查更新时间
     */
    fun getLastCheckTime(): Long {
        val prefs = context.getSharedPreferences("update_prefs", Context.MODE_PRIVATE)
        return prefs.getLong("last_check_time", 0)
    }

    /**
     * 保存上次检查更新时间
     */
    fun saveLastCheckTime(time: Long) {
        val prefs = context.getSharedPreferences("update_prefs", Context.MODE_PRIVATE)
        prefs.edit().putLong("last_check_time", time).apply()
    }

    /**
     * 是否自动检查更新
     */
    fun isAutoCheckEnabled(): Boolean {
        val prefs = context.getSharedPreferences("update_prefs", Context.MODE_PRIVATE)
        return prefs.getBoolean("auto_check", true)
    }

    /**
     * 设置自动检查更新
     */
    fun setAutoCheckEnabled(enabled: Boolean) {
        val prefs = context.getSharedPreferences("update_prefs", Context.MODE_PRIVATE)
        prefs.edit().putBoolean("auto_check", enabled).apply()
    }
}
