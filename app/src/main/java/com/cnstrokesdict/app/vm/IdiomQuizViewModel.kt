package com.cnstrokesdict.app.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.cnstrokesdict.app.util.IdiomQuizGame
import com.cnstrokesdict.app.util.QuizQuestion
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class IdiomQuizUiState(
    val isLoading: Boolean = false,
    val question: QuizQuestion? = null,
    val score: Int = 0,
    val correctCount: Int = 0,
    val totalCount: Int = 0,
    val streak: Int = 0,
    val maxStreak: Int = 0,
    val message: String? = null,
    val isError: Boolean = false,
    val selectedOption: String? = null,
    val showResult: Boolean = false,
    val isCorrect: Boolean = false,
)

class IdiomQuizViewModel(application: Application) : AndroidViewModel(application) {
    private val game = IdiomQuizGame(application)
    private val _uiState = MutableStateFlow(IdiomQuizUiState())
    val uiState: StateFlow<IdiomQuizUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            game.init()
            loadQuestion()
        }
    }

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
            streak = game.getStreak(),
            maxStreak = game.getMaxStreak(),
            message = if (isCorrect) "正确！连续答对${game.getStreak()}题" else "错误，正确答案是：${_uiState.value.question?.idiom}",
            isError = !isCorrect,
        )
    }

    fun nextQuestion() {
        loadQuestion()
    }

    fun reset() {
        game.reset()
        _uiState.value = IdiomQuizUiState()
        viewModelScope.launch {
            game.init()
            loadQuestion()
        }
    }
}
