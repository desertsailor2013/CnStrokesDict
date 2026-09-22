package com.cnstrokesdict.app.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.cnstrokesdict.app.data.WordPackageManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * 扫码导入界面状态
 */
data class QrScanUiState(
    val isScanning: Boolean = true,
    val scanResult: String? = null,
    val importedPackageName: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,
)

/**
 * 扫码导入ViewModel
 */
class QrScanViewModel(application: Application) : AndroidViewModel(application) {

    private val packageManager = WordPackageManager(application)

    private val _uiState = MutableStateFlow(QrScanUiState())
    val uiState: StateFlow<QrScanUiState> = _uiState.asStateFlow()

    /**
     * 处理扫码结果
     */
    fun onScanResult(qrContent: String) {
        _uiState.value = _uiState.value.copy(
            isScanning = false,
            scanResult = qrContent,
        )

        // 解析二维码内容
        parseAndImport(qrContent)
    }

    /**
     * 解析并导入词库包
     */
    private fun parseAndImport(qrContent: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            try {
                // 解析JSON格式的二维码内容
                // 格式: {"type":"word_package","id":1,"name":"词库包名称","version":"1.0"}
                val jsonStr = qrContent.trim()

                // 简单解析（实际项目中应使用kotlinx.serialization）
                if (!jsonStr.contains("\"type\":\"word_package\"")) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "无效的词库包二维码",
                    )
                    return@launch
                }

                // 提取词库包ID
                val idMatch = Regex("\"id\":(\\d+)").find(jsonStr)
                val packageId = idMatch?.groupValues?.get(1)?.toIntOrNull()

                if (packageId == null) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "无法解析词库包ID",
                    )
                    return@launch
                }

                // 检查词库包是否已存在
                val existingPackage = packageManager.getPackageById(packageId)
                if (existingPackage != null) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        successMessage = "词库包「${existingPackage.name}」已存在",
                        importedPackageName = existingPackage.name,
                    )
                    return@launch
                }

                // 提取词库包名称
                val nameMatch = Regex("\"name\":\"([^\"]+)\"").find(jsonStr)
                val packageName = nameMatch?.groupValues?.get(1) ?: "未命名词库包"

                // 创建词库包
                packageManager.createPackage(
                    name = packageName,
                    description = "通过二维码导入",
                )

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    successMessage = "词库包「$packageName」导入成功",
                    importedPackageName = packageName,
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "导入失败: ${e.message}",
                )
            }
        }
    }

    /**
     * 重新开始扫码
     */
    fun startScanning() {
        _uiState.value = QrScanUiState()
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
