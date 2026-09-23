package com.cnstrokesdict.app.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.cnstrokesdict.app.data.IdiomRepository
import com.cnstrokesdict.app.data.db.CategoryStat
import com.cnstrokesdict.app.data.db.IdiomEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * 成语词典界面状态
 */
data class IdiomDictionaryState(
    val isLoading: Boolean = false,
    val idiomCount: Int = 0,
    val categories: List<CategoryStat> = emptyList(),
    val searchQuery: String = "",
    val searchResults: List<IdiomEntity> = emptyList(),
    val currentIdiom: IdiomEntity? = null,
    val isIdiomLoaded: Boolean = false,
    val errorMessage: String? = null,
)

/**
 * 成语词典ViewModel
 */
class IdiomDictionaryViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = IdiomRepository(application)
    private val _uiState = MutableStateFlow(IdiomDictionaryState())
    val uiState: StateFlow<IdiomDictionaryState> = _uiState.asStateFlow()

    init {
        loadIdioms()
    }

    private fun loadIdioms() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                // 先检查数据库中是否已有数据
                val count = repository.getIdiomCount()
                if (count == 0) {
                    // 从assets加载数据
                    repository.loadIdiomsFromAssets()
                }

                val categories = repository.getCategoryStats()
                val totalCount = repository.getIdiomCount()

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    idiomCount = totalCount,
                    categories = categories,
                    isIdiomLoaded = true,
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message,
                )
            }
        }
    }

    /**
     * 搜索成语
     */
    fun searchIdioms(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
        if (query.isBlank()) {
            _uiState.value = _uiState.value.copy(searchResults = emptyList())
            return
        }

        viewModelScope.launch {
            try {
                val results = repository.searchIdioms(query)
                _uiState.value = _uiState.value.copy(searchResults = results)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(errorMessage = e.message)
            }
        }
    }

    /**
     * 按分类查询
     */
    fun getIdiomsByCategory(category: String) {
        viewModelScope.launch {
            try {
                val results = repository.getIdiomsByCategory(category)
                _uiState.value = _uiState.value.copy(
                    searchQuery = category,
                    searchResults = results,
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(errorMessage = e.message)
            }
        }
    }

    /**
     * 查看成语详情
     */
    fun viewIdiom(idiom: IdiomEntity) {
        _uiState.value = _uiState.value.copy(currentIdiom = idiom)
    }

    /**
     * 清除搜索
     */
    fun clearSearch() {
        _uiState.value = _uiState.value.copy(
            searchQuery = "",
            searchResults = emptyList(),
        )
    }

    /**
     * 获取随机成语
     */
    fun getRandomIdioms(limit: Int = 5) {
        viewModelScope.launch {
            try {
                val results = repository.getRandomIdioms(limit)
                _uiState.value = _uiState.value.copy(searchResults = results)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(errorMessage = e.message)
            }
        }
    }

    /**
     * 清除错误信息
     */
    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}
