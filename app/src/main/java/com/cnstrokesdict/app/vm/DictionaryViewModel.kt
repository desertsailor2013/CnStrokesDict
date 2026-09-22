package com.cnstrokesdict.app.vm

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.cnstrokesdict.app.data.CharacterEntry
import com.cnstrokesdict.app.data.CharacterListItem
import com.cnstrokesdict.app.data.DictionaryRepository
import com.cnstrokesdict.app.util.VoiceQueryParser
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SearchUiState(
    val query: String = "",
    val results: List<CharacterListItem> = emptyList(),
    val loading: Boolean = true,
    val error: String? = null,
)

class DictionaryViewModel(
    private val repository: DictionaryRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    var selected: CharacterEntry? = null
        private set

    private var searchJob: Job? = null

    init {
        refreshList("")
    }

    fun onQueryChange(q: String) {
        _uiState.update { it.copy(query = q) }
        refreshList(q)
    }

    fun onVoiceSearchResult(raw: String) {
        onQueryChange(VoiceQueryParser.parseQuery(raw))
    }

    private fun refreshList(query: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            try {
                val list = if (query.isBlank()) {
                    repository.browseList()
                } else {
                    repository.searchList(query)
                }
                _uiState.update {
                    it.copy(results = list, loading = false, error = null)
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(loading = false, error = e.message ?: "加载失败")
                }
            }
        }
    }

    fun select(entry: CharacterEntry) {
        selected = entry
    }

    suspend fun loadEntryForDetail(char: String): CharacterEntry? =
        repository.findByChar(char)

    companion object {
        fun factory(context: Context): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    val repo = DictionaryRepository(context.applicationContext)
                    return DictionaryViewModel(repo) as T
                }
            }
    }
}
