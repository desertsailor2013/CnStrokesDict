package com.cnstrokesdict.app.vm

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.cnstrokesdict.app.data.WordPackageManager
import com.cnstrokesdict.app.data.db.WordPackageEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File

/**
 * 词库管理界面状态
 */
data class WordPackageManagerUiState(
    val packages: List<WordPackageEntity> = emptyList(),
    val selectedPackage: WordPackageEntity? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,
)

/**
 * 词库管理ViewModel
 */
class WordPackageManagerViewModel(application: Application) : AndroidViewModel(application) {

    private val packageManager = WordPackageManager(application)

    private val _uiState = MutableStateFlow(WordPackageManagerUiState())
    val uiState: StateFlow<WordPackageManagerUiState> = _uiState.asStateFlow()

    init {
        loadPackages()
    }

    /**
     * 加载所有词库包
     */
    fun loadPackages() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            try {
                val packages = packageManager.getAllPackages()
                _uiState.value = _uiState.value.copy(
                    packages = packages,
                    isLoading = false,
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "加载词库包失败: ${e.message}",
                )
            }
        }
    }

    /**
     * 创建新词库包
     */
    fun createPackage(
        name: String,
        description: String = "",
        author: String = "",
        grade: Int = 0,
        semester: Int = 0,
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            try {
                packageManager.createPackage(name, description, author, grade, semester)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    successMessage = "词库包创建成功",
                )
                loadPackages()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "创建词库包失败: ${e.message}",
                )
            }
        }
    }

    /**
     * 删除词库包
     */
    fun deletePackage(id: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            try {
                packageManager.deletePackage(id)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    successMessage = "词库包删除成功",
                )
                loadPackages()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "删除词库包失败: ${e.message}",
                )
            }
        }
    }

    /**
     * 启用/禁用词库包
     */
    fun setPackageActive(id: Int, isActive: Boolean) {
        viewModelScope.launch {
            try {
                packageManager.setPackageActive(id, isActive)
                loadPackages()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "更新词库包状态失败: ${e.message}",
                )
            }
        }
    }

    /**
     * 导出词库包
     */
    fun exportPackage(packageId: Int, outputFile: File) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            try {
                packageManager.exportPackageToFile(packageId, outputFile)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    successMessage = "词库包导出成功: ${outputFile.name}",
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "导出词库包失败: ${e.message}",
                )
            }
        }
    }

    /**
     * 导入词库包
     */
    fun importPackage(inputFile: File) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            try {
                packageManager.importPackageFromFile(inputFile)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    successMessage = "词库包导入成功",
                )
                loadPackages()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "导入词库包失败: ${e.message}",
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
