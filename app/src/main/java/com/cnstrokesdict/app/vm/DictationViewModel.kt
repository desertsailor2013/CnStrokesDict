package com.cnstrokesdict.app.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.cnstrokesdict.app.data.TextbookWordEntry
import com.cnstrokesdict.app.data.WordListItem
import com.cnstrokesdict.app.data.db.WordPackageEntity
import com.cnstrokesdict.app.util.DictationManager
import com.cnstrokesdict.app.util.TtsManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * 听写界面状态
 */
data class DictationUiState(
    val isSetup: Boolean = true,
    val isPlaying: Boolean = false,
    val currentWord: TextbookWordEntry? = null,
    val showAnswer: Boolean = false,
    val progress: Pair<Int, Int> = Pair(0, 0),
    val errors: List<TextbookWordEntry> = emptyList(),
    val isComplete: Boolean = false,
    val speed: Float = 1.0f,
    val intervalMs: Long = 3000L,
    val repeatCount: Int = 1,
    val mode: DictationManager.DictationMode = DictationManager.DictationMode.SEQUENTIAL,
    val errorMessage: String? = null,
    val successMessage: String? = null,
)

/**
 * 听写ViewModel
 */
class DictationViewModel(application: Application) : AndroidViewModel(application) {

    private val ttsManager = TtsManager(application)
    private val dictationManager = DictationManager()

    private val _uiState = MutableStateFlow(DictationUiState())
    val uiState: StateFlow<DictationUiState> = _uiState.asStateFlow()

    init {
        // 监听TTS状态
        viewModelScope.launch {
            ttsManager.isReady.collect { isReady ->
                if (!isReady) {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = "语音引擎不可用",
                    )
                }
            }
        }
    }

    /**
     * 开始听写
     * @param words 词语列表
     */
    fun startDictation(words: List<WordListItem>) {
        val textbookWords = words.map { word ->
            TextbookWordEntry(
                word = word.word,
                pinyin = word.pinyin,
                meaning = word.meaning,
                grade = word.grade,
                semester = word.semester,
            )
        }

        dictationManager.start(
            wordList = textbookWords,
            mode = _uiState.value.mode,
            speed = _uiState.value.speed,
            repeatCount = _uiState.value.repeatCount,
            intervalMs = _uiState.value.intervalMs,
        )

        _uiState.value = _uiState.value.copy(
            isSetup = false,
            isPlaying = true,
            progress = dictationManager.getProgress(),
        )

        // 播放第一个词语
        playNextWord()
    }

    /**
     * 播放下一个词语
     */
    private fun playNextWord() {
        val word = dictationManager.next()
        if (word == null) {
            // 听写完成
            _uiState.value = _uiState.value.copy(
                isPlaying = false,
                isComplete = true,
                errors = dictationManager.getErrors(),
                progress = dictationManager.getProgress(),
            )
            return
        }

        _uiState.value = _uiState.value.copy(
            currentWord = word,
            showAnswer = false,
            progress = dictationManager.getProgress(),
        )

        // 播放词语
        viewModelScope.launch {
            for (i in 1..dictationManager.getRepeatCount()) {
                ttsManager.speak(word.word, dictationManager.getSpeed())
                if (i < dictationManager.getRepeatCount()) {
                    delay(dictationManager.getIntervalMs())
                }
            }
        }
    }

    /**
     * 显示答案
     */
    fun showAnswer() {
        _uiState.value = _uiState.value.copy(showAnswer = true)
    }

    /**
     * 标记为正确
     */
    fun markCorrect() {
        _uiState.value.currentWord?.let { word ->
            dictationManager.markCorrect(word)
            playNextWord()
        }
    }

    /**
     * 标记为错误
     */
    fun markError() {
        _uiState.value.currentWord?.let { word ->
            dictationManager.markError(word)
            playNextWord()
        }
    }

    /**
     * 跳过当前词语
     */
    fun skip() {
        playNextWord()
    }

    /**
     * 设置语速
     * @param speed 语速（0.5-2.0）
     */
    fun setSpeed(speed: Float) {
        _uiState.value = _uiState.value.copy(speed = speed)
        ttsManager.setSpeed(speed)
    }

    /**
     * 设置间隔时间
     * @param intervalMs 间隔时间（毫秒）
     */
    fun setIntervalMs(intervalMs: Long) {
        _uiState.value = _uiState.value.copy(intervalMs = intervalMs)
    }

    /**
     * 设置重复次数
     * @param count 重复次数
     */
    fun setRepeatCount(count: Int) {
        _uiState.value = _uiState.value.copy(repeatCount = count)
    }

    /**
     * 设置听写模式
     * @param mode 听写模式
     */
    fun setMode(mode: DictationManager.DictationMode) {
        _uiState.value = _uiState.value.copy(mode = mode)
    }

    /**
     * 重新开始
     */
    fun restart() {
        _uiState.value = _uiState.value.copy(
            isSetup = true,
            isPlaying = false,
            currentWord = null,
            showAnswer = false,
            progress = Pair(0, 0),
            errors = emptyList(),
            isComplete = false,
        )
    }

    /**
     * 错词复习
     */
    fun reviewErrors() {
        val errors = dictationManager.getErrors()
        if (errors.isEmpty()) {
            _uiState.value = _uiState.value.copy(
                successMessage = "没有错误词语",
            )
            return
        }

        dictationManager.start(
            wordList = errors,
            mode = DictationManager.DictationMode.ERROR_ONLY,
            speed = _uiState.value.speed,
            repeatCount = _uiState.value.repeatCount,
            intervalMs = _uiState.value.intervalMs,
        )

        _uiState.value = _uiState.value.copy(
            isComplete = false,
            isPlaying = true,
            errors = emptyList(),
            progress = dictationManager.getProgress(),
        )

        playNextWord()
    }

    /**
     * 停止听写
     */
    fun stopDictation() {
        ttsManager.stop()
        _uiState.value = _uiState.value.copy(
            isPlaying = false,
            currentWord = null,
        )
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

    override fun onCleared() {
        super.onCleared()
        ttsManager.shutdown()
    }
}
