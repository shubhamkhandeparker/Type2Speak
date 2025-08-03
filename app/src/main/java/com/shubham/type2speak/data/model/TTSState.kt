package com.shubham.type2speak.data.model

import java.util.Locale

data class TTSState(
    val text: String = "",
    val pitch: Float = 1.0f,
    val speed: Float = 1.0f,
    val isPlaying: Boolean = false,
    val isLoading: Boolean = false,
    val progress: Float = 0.0f,
    val isMaleVoice: Boolean = false,
    val selectedLanguage: Locale = Locale.US,
    val isLanguageSupported: Boolean = true,
    val isSpeaking: Boolean = false,
    val showLanguageDropdown: Boolean = false
)