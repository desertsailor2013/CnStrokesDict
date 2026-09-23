package com.cnstrokesdict.app.util

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

/**
 * TTS管理器
 */
class TtsManager(context: Context) {

    companion object {
        private const val TAG = "TtsManager"
    }

    private var tts: TextToSpeech? = null
    private val _isReady = MutableStateFlow(false)
    val isReady: StateFlow<Boolean> = _isReady.asStateFlow()

    private val _isSpeaking = MutableStateFlow(false)
    val isSpeaking: StateFlow<Boolean> = _isSpeaking.asStateFlow()

    init {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = tts?.setLanguage(Locale.CHINA)
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e(TAG, "中文语言包缺失或不支持")
                    _isReady.value = false
                } else {
                    _isReady.value = true
                    setupListener()
                }
            } else {
                Log.e(TAG, "TTS初始化失败: $status")
                _isReady.value = false
            }
        }
    }

    private fun setupListener() {
        tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) {
                _isSpeaking.value = true
            }

            override fun onDone(utteranceId: String?) {
                _isSpeaking.value = false
            }

            @Deprecated("Deprecated in Java")
            override fun onError(utteranceId: String?) {
                _isSpeaking.value = false
            }
        })
    }

    /**
     * 朗读文本
     * @param text 要朗读的文本
     * @param speed 语速（0.5-2.0）
     */
    fun speak(text: String, speed: Float = 1.0f) {
        if (!_isReady.value) {
            Log.e(TAG, "TTS未就绪")
            return
        }

        tts?.setSpeechRate(speed)
        val params = android.os.Bundle().apply {
            putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, System.currentTimeMillis().toString())
        }
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, params, null)
    }

    /**
     * 停止朗读
     */
    fun stop() {
        tts?.stop()
        _isSpeaking.value = false
    }

    /**
     * 设置语速
     * @param speed 语速（0.5-2.0）
     */
    fun setSpeed(speed: Float) {
        tts?.setSpeechRate(speed)
    }

    /**
     * 释放资源
     */
    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
        tts = null
        _isReady.value = false
        _isSpeaking.value = false
    }
}
