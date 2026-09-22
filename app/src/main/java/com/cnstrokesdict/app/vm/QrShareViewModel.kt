package com.cnstrokesdict.app.vm

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.cnstrokesdict.app.data.WordPackageManager
import com.cnstrokesdict.app.data.db.WordPackageEntity
import com.cnstrokesdict.app.util.QrCodeUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * 二维码分享界面状态
 */
data class QrShareUiState(
    val packageEntity: WordPackageEntity? = null,
    val qrBitmap: Bitmap? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,
)

/**
 * 二维码分享ViewModel
 */
class QrShareViewModel(application: Application) : AndroidViewModel(application) {

    private val packageManager = WordPackageManager(application)

    private val _uiState = MutableStateFlow(QrShareUiState())
    val uiState: StateFlow<QrShareUiState> = _uiState.asStateFlow()

    /**
     * 加载词库包并生成二维码
     */
    fun loadPackageAndGenerateQr(packageId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            try {
                val packageEntity = packageManager.getPackageById(packageId)
                if (packageEntity == null) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "词库包不存在",
                    )
                    return@launch
                }

                val qrContent = QrCodeUtil.generateWordPackageQrContent(
                    packageId = packageEntity.id,
                    packageName = packageEntity.name,
                )
                val qrBitmap = QrCodeUtil.generateQrCodeBitmap(qrContent)

                _uiState.value = _uiState.value.copy(
                    packageEntity = packageEntity,
                    qrBitmap = qrBitmap,
                    isLoading = false,
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "生成二维码失败: ${e.message}",
                )
            }
        }
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
