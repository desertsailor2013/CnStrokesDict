package com.cnstrokesdict.app.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.cnstrokesdict.app.data.remote.UpdateCheckResponse
import com.cnstrokesdict.app.data.update.UpdateManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * 更新界面状态
 */
data class UpdateUiState(
    val currentVersion: String = "",
    val latestVersion: String = "",
    val hasUpdate: Boolean = false,
    val changelog: String = "",
    val isLoading: Boolean = false,
    val isDownloading: Boolean = false,
    val downloadProgress: Int = 0,
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val lastCheckTime: Long = 0,
    val autoCheckEnabled: Boolean = true,
)

/**
 * 更新ViewModel
 */
class UpdateViewModel(application: Application) : AndroidViewModel(application) {

    private val updateManager = UpdateManager(application)

    private val _uiState = MutableStateFlow(UpdateUiState())
    val uiState: StateFlow<UpdateUiState> = _uiState.asStateFlow()

    init {
        loadUpdateInfo()
    }

    /**
     * 加载更新信息
     */
    private fun loadUpdateInfo() {
        _uiState.value = _uiState.value.copy(
            currentVersion = updateManager.getCurrentVersion(),
            lastCheckTime = updateManager.getLastCheckTime(),
            autoCheckEnabled = updateManager.isAutoCheckEnabled(),
        )
    }

    /**
     * 检查更新
     */
    fun checkUpdate() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null,
            )
            try {
                val response = updateManager.checkUpdate()
                updateManager.saveLastCheckTime(System.currentTimeMillis())
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    hasUpdate = response.hasUpdate,
                    latestVersion = response.latestVersion,
                    changelog = response.changelog,
                    lastCheckTime = System.currentTimeMillis(),
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "检查更新失败: ${e.message}",
                )
            }
        }
    }

    /**
     * 下载更新
     */
    fun downloadUpdate() {
        viewModelScope.launch {
            val version = _uiState.value.latestVersion
            if (version.isEmpty()) return@launch

            _uiState.value = _uiState.value.copy(
                isDownloading = true,
                errorMessage = null,
            )
            try {
                updateManager.downloadUpdate(version)
                _uiState.value = _uiState.value.copy(
                    isDownloading = false,
                    successMessage = "更新下载完成",
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isDownloading = false,
                    errorMessage = "下载更新失败: ${e.message}",
                )
            }
        }
    }

    /**
     * 设置自动检查更新
     */
    fun setAutoCheckEnabled(enabled: Boolean) {
        updateManager.setAutoCheckEnabled(enabled)
        _uiState.value = _uiState.value.copy(autoCheckEnabled = enabled)
    }

    /**
     * 清除消息
     */
    fun clearMessages() {
        _uiState.value = _uiState.value.copy(
            errorMessage = null,
            successMessage = null,
        )
    }
}
