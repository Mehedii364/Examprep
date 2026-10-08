package com.example.tts

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

class SpeechManager(context: Context) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = TextToSpeech(context.applicationContext, this)
    private var isInitialized = false

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _currentlyPlayingId = MutableStateFlow<Long?>(null)
    val currentlyPlayingId: StateFlow<Long?> = _currentlyPlayingId.asStateFlow()

    private val _currentText = MutableStateFlow("")
    val currentText: StateFlow<String> = _currentText.asStateFlow()

    private val _speechRate = MutableStateFlow(1.0f)
    val speechRate: StateFlow<Float> = _speechRate.asStateFlow()

    private val _isBengaliSupported = MutableStateFlow(true)
    val isBengaliSupported: StateFlow<Boolean> = _isBengaliSupported.asStateFlow()

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            isInitialized = true
            val bnLocale = Locale("bn", "BD")
            val result = tts?.setLanguage(bnLocale)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                // Fallback to generic Bengali or English
                val fallbackResult = tts?.setLanguage(Locale("bn"))
                if (fallbackResult == TextToSpeech.LANG_MISSING_DATA || fallbackResult == TextToSpeech.LANG_NOT_SUPPORTED) {
                    tts?.language = Locale.ENGLISH
                    _isBengaliSupported.value = false
                } else {
                    _isBengaliSupported.value = true
                }
            } else {
                _isBengaliSupported.value = true
            }

            tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {
                    _isPlaying.value = true
                }

                override fun onDone(utteranceId: String?) {
                    _isPlaying.value = false
                    _currentlyPlayingId.value = null
                }

                override fun onError(utteranceId: String?) {
                    _isPlaying.value = false
                    _currentlyPlayingId.value = null
                }
            })
        }
    }

    fun speak(id: Long, text: String, speed: Float = _speechRate.value) {
        if (!isInitialized || text.isBlank()) return
        stop()

        _speechRate.value = speed
        tts?.setSpeechRate(speed)
        _currentlyPlayingId.value = id
        _currentText.value = text
        _isPlaying.value = true

        val utteranceId = "q_$id"
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId)
    }

    fun setSpeed(speed: Float) {
        _speechRate.value = speed
        tts?.setSpeechRate(speed)
    }

    fun stop() {
        if (isInitialized) {
            tts?.stop()
        }
        _isPlaying.value = false
        _currentlyPlayingId.value = null
    }

    fun release() {
        stop()
        tts?.shutdown()
        tts = null
    }
}
