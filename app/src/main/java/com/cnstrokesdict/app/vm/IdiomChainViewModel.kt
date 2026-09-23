package com.cnstrokesdict.app.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.cnstrokesdict.app.util.GameResult
import com.cnstrokesdict.app.util.GameState
import com.cnstrokesdict.app.util.IdiomChainGame
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * 成语接龙游戏界面状态
 */
data class IdiomChainUiState(
    val isLoading: Boolean = false,
    val isGameStarted: Boolean = false,
    val currentIdiom: String = "",
    val gameState: GameState? = null,
    val score: Int = 0,
    val rounds: Int = 0,
    val message: String? = null,
    val isError: Boolean = false,
    val inputIdiom: String = "",
    val computerIdiom: String? = null,
    val isComputerTurn: Boolean = false,
    val gameOver: Boolean = false,
    val errorMessage: String? = null,
)

/**
 * 成语接龙游戏ViewModel
 */
class IdiomChainViewModel(application: Application) : AndroidViewModel(application) {
    private val game = IdiomChainGame(application)
    private val _uiState = MutableStateFlow(IdiomChainUiState())
    val uiState: StateFlow<IdiomChainUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            game.init()
            _uiState.value = _uiState.value.copy(isLoading = false)
        }
    }

    /**
     * 开始游戏
     */
    fun startGame() {
        val firstIdiom = game.startGame()
        _uiState.value = _uiState.value.copy(
            isGameStarted = true,
            currentIdiom = firstIdiom,
            gameState = game.getGameState(),
            score = 0,
            rounds = 1,
            message = "游戏开始！第一个成语：$firstIdiom",
            isError = false,
            inputIdiom = "",
            computerIdiom = null,
            isComputerTurn = false,
            gameOver = false,
        )
    }

    /**
     * 更新输入
     */
    fun updateInput(input: String) {
        _uiState.value = _uiState.value.copy(inputIdiom = input)
    }

    /**
     * 玩家出牌
     */
    fun playIdiom() {
        val input = _uiState.value.inputIdiom.trim()
        if (input.isEmpty()) {
            _uiState.value = _uiState.value.copy(
                message = "请输入成语",
                isError = true,
            )
            return
        }

        when (val result = game.play(input)) {
            is GameResult.Success -> {
                val newScore = _uiState.value.score + result.score
                val newRounds = _uiState.value.rounds + 1

                _uiState.value = _uiState.value.copy(
                    currentIdiom = result.idiom,
                    gameState = game.getGameState(),
                    score = newScore,
                    rounds = newRounds,
                    message = "正确！+${result.score}分",
                    isError = false,
                    inputIdiom = "",
                    computerIdiom = null,
                    isComputerTurn = true,
                )

                // 检查游戏是否结束
                if (game.isGameOver()) {
                    _uiState.value = _uiState.value.copy(
                        message = "游戏结束！最终得分：$newScore",
                        gameOver = true,
                        isComputerTurn = false,
                    )
                } else {
                    // 电脑出牌
                    viewModelScope.launch {
                        kotlinx.coroutines.delay(1000)
                        computerPlay()
                    }
                }
            }
            is GameResult.Invalid -> {
                _uiState.value = _uiState.value.copy(
                    message = result.message,
                    isError = true,
                )
            }
        }
    }

    /**
     * 电脑出牌
     */
    private fun computerPlay() {
        val computerIdiom = game.getComputerPlay()
        if (computerIdiom != null) {
            val newRounds = _uiState.value.rounds + 1
            _uiState.value = _uiState.value.copy(
                currentIdiom = computerIdiom,
                gameState = game.getGameState(),
                rounds = newRounds,
                message = "电脑出牌：$computerIdiom",
                isError = false,
                computerIdiom = computerIdiom,
                isComputerTurn = false,
            )

            // 检查游戏是否结束
            if (game.isGameOver()) {
                _uiState.value = _uiState.value.copy(
                    message = "游戏结束！最终得分：${_uiState.value.score}",
                    gameOver = true,
                )
            }
        } else {
            _uiState.value = _uiState.value.copy(
                message = "电脑无法出牌，你赢了！最终得分：${_uiState.value.score}",
                gameOver = true,
                isComputerTurn = false,
            )
        }
    }

    /**
     * 获取提示
     */
    fun getHint() {
        val hint = game.getHint()
        if (hint != null) {
            _uiState.value = _uiState.value.copy(
                message = hint,
                isError = false,
            )
        } else {
            _uiState.value = _uiState.value.copy(
                message = "没有可用的提示",
                isError = true,
            )
        }
    }

    /**
     * 跳过回合
     */
    fun skipTurn() {
        computerPlay()
    }

    /**
     * 清除消息
     */
    fun clearMessage() {
        _uiState.value = _uiState.value.copy(message = null)
    }
}
