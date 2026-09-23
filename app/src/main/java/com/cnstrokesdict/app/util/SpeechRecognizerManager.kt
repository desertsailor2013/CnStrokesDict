package com.cnstrokesdict.app.util

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * 语音识别状态
 */
sealed class SpeechRecognizerState {
    object Idle : SpeechRecognizerState()
    object Listening : SpeechRecognizerState()
    object Processing : SpeechRecognizerState()
    data class Success(val text: String) : SpeechRecognizerState()
    data class Error(val message: String) : SpeechRecognizerState()
}

/**
 * 语音识别管理器
 */
class SpeechRecognizerManager(private val context: Context) {
    private var speechRecognizer: SpeechRecognizer? = null
    private val _state = MutableStateFlow<SpeechRecognizerState>(SpeechRecognizerState.Idle)
    val state: StateFlow<SpeechRecognizerState> = _state.asStateFlow()

    /**
     * 初始化语音识别器
     */
    fun init() {
        if (!SpeechRecognizer.isRecognitionAvailable(context)) {
            _state.value = SpeechRecognizerState.Error("设备不支持语音识别")
            return
        }

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
        speechRecognizer?.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {
                _state.value = SpeechRecognizerState.Listening
            }

            override fun onBeginningOfSpeech() {
                // 开始说话
            }

            override fun onRmsChanged(rmsdB: Float) {
                // 音量变化
            }

            override fun onBufferReceived(buffer: ByteArray?) {
                // 接收到音频缓冲区
            }

            override fun onEndOfSpeech() {
                _state.value = SpeechRecognizerState.Processing
            }

            override fun onError(error: Int) {
                val message = when (error) {
                    SpeechRecognizer.ERROR_NO_MATCH -> "未识别到语音"
                    SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "语音输入超时"
                    SpeechRecognizer.ERROR_AUDIO -> "音频错误"
                    SpeechRecognizer.ERROR_CLIENT -> "客户端错误"
                    SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "权限不足"
                    SpeechRecognizer.ERROR_NETWORK -> "网络错误"
                    SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "网络超时"
                    SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "识别器忙"
                    SpeechRecognizer.ERROR_SERVER -> "服务器错误"
                    else -> "识别错误: $error"
                }
                _state.value = SpeechRecognizerState.Error(message)
            }

            override fun onResults(results: Bundle?) {
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                val text = matches?.firstOrNull() ?: ""
                _state.value = SpeechRecognizerState.Success(text)
            }

            override fun onPartialResults(partialResults: Bundle?) {
                // 部分结果
            }

            override fun onEvent(eventType: Int, params: Bundle?) {
                // 事件
            }
        })
    }

    /**
     * 开始监听
     */
    fun startListening() {
        if (speechRecognizer == null) {
            init()
        }

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "zh-CN")
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
        }

        _state.value = SpeechRecognizerState.Listening
        speechRecognizer?.startListening(intent)
    }

    /**
     * 停止监听
     */
    fun stopListening() {
        speechRecognizer?.stopListening()
        _state.value = SpeechRecognizerState.Idle
    }

    /**
     * 取消监听
     */
    fun cancel() {
        speechRecognizer?.cancel()
        _state.value = SpeechRecognizerState.Idle
    }

    /**
     * 释放资源
     */
    fun destroy() {
        speechRecognizer?.destroy()
        speechRecognizer = null
        _state.value = SpeechRecognizerState.Idle
    }

    /**
     * 检查是否支持语音识别
     */
    fun isAvailable(): Boolean {
        return SpeechRecognizer.isRecognitionAvailable(context)
    }
}
