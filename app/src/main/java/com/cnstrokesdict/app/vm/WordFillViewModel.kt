package com.cnstrokesdict.app.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.cnstrokesdict.app.util.FillQuestion
import com.cnstrokesdict.app.util.WordFillGame
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * 词语填空游戏界面状态
 */
data class WordFillUiState(
    val isLoading: Boolean = false,
    val question: FillQuestion? = null,
    val score: Int = 0,
    val correctCount: Int = 0,
    val totalCount: Int = 0,
    val message: String? = null,
    val isError: Boolean = false,
    val selectedOption: String? = null,
    val showResult: Boolean = false,
    val isCorrect: Boolean = false,
)

/**
 * 词语填空游戏ViewModel
 */
class WordFillViewModel(application: Application) : AndroidViewModel(application) {
    private val game = WordFillGame(application)
    private val _uiState = MutableStateFlow(WordFillUiState())
    val uiState: StateFlow<WordFillUiState> = _uiState.asStateFlow()

    init {
        loadQuestion()
    }

    /**
     * 加载题目
     */
    fun loadQuestion() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val question = game.getQuestion()
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                question = question,
                selectedOption = null,
                showResult = false,
                message = null,
            )
        }
    }

    /**
     * 选择答案
     */
    fun selectOption(option: String) {
        if (_uiState.value.showResult) return

        _uiState.value = _uiState.value.copy(selectedOption = option)
        val isCorrect = game.checkAnswer(option)

        _uiState.value = _uiState.value.copy(
            showResult = true,
            isCorrect = isCorrect,
            score = game.getScore(),
            correctCount = game.correctCount,
            totalCount = game.totalCount,
            message = if (isCorrect) "正确！" else "错误，正确答案是：${_uiState.value.question?.hiddenChar}",
            isError = !isCorrect,
        )
    }

    /**
     * 下一题
     */
    fun nextQuestion() {
        loadQuestion()
    }

    /**
     * 重新开始
     */
    fun reset() {
        game.reset()
        _uiState.value = WordFillUiState()
        loadQuestion()
    }
}
