package com.shubham.type2speak.presentation.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.shubham.type2speak.data.model.TTSState
import androidx.lifecycle.ViewModel
import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import java.util.*

//import dagger.hilt.android.lifecycle.HiltViewModel
//import javax.inject.Inject

//@HiltViewModel
class TTSViewModel constructor() : ViewModel() {

    private var textToSpeech: TextToSpeech? = null


    //Private only ViewModel can change this

    private val _state = MutableStateFlow(TTSState())

    //public -Ui can only read /observe this

    val state: StateFlow<TTSState> = _state.asStateFlow()

    //Function to update text when user types
    fun updateText(newText: String) {
        val detectedLanguage = detectLanguage(newText)

        //DEBUG : Learning What's Happening
        println("\uD83D\uDD0D Text :${newText}")
        println("\uD83C\uDF0D Detected Language :${detectedLanguage.displayLanguage} ")

        _state.value = _state.value.copy(
            text = newText,
            selectedLanguage = detectedLanguage
        )

        textToSpeech?.language = detectedLanguage
        checkLanguageSupport(detectedLanguage)
    }


    //Function to update pitch when user moves slider

    fun updatePitch(newPitch: Float) {
        _state.value = _state.value.copy(pitch = newPitch)
    }

    //Function to update speed when user moves slider

    fun updateSpeed(newSpeed: Float) {
        _state.value = _state.value.copy(speed = newSpeed)
    }

    fun initializeTTS(context: Context) {
        textToSpeech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech?.language = Locale.US
                setVoiceSettings()

            }
        }
    }

    fun speak() {
        val currentState = _state.value

        //checking if language is supported
        if (!currentState.isLanguageSupported) {
            println("Language not supported : ${currentState.selectedLanguage}")
            return
        }


        //setting speaking state to true
        _state.value = _state.value.copy(isSpeaking = true)

        //setting the language before speaking
        textToSpeech?.language = currentState.selectedLanguage


        //setting gender appropriate pitch
        val basePitch = if (currentState.isMaleVoice) {
            currentState.pitch * 0.8f  //lower for male
        } else {
            currentState.pitch * 1.0f //Higher for female
        }

        textToSpeech?.setPitch(basePitch)
        textToSpeech?.setSpeechRate(currentState.speed * 0.8f)

        val naturalText = currentState.text.replace(",", ",").replace(".", ".")

        //Adding Listen for Completion
        textToSpeech?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) {
                //Speaking Started
            }

            override fun onDone(utteranceId: String?) {
                //Speaking finished
                _state.value = _state.value.copy(isSpeaking = false)
            }

            override fun onError(utteranceId: String?) {
                //Error Occurred
                _state.value = _state.value.copy(isSpeaking = false)
            }
        })

        textToSpeech?.speak(naturalText, TextToSpeech.QUEUE_FLUSH, null, "utterance_id")


    }

    fun setVoiceSettings() {
        textToSpeech?.let { tts ->
            //trying to find better voice

            val voices = tts.voices
            val betterVoice = voices?.find { voice ->
                voice.locale == Locale.US &&
                        voice.quality >= 300 && //Higher Quality
                        !voice.isNetworkConnectionRequired && //Offline voice
                        voice.name.contains("enhanced", ignoreCase = true)

            }

            if (betterVoice != null) {
                tts.voice = betterVoice
            }
        }
    }

    //Function to toggle voice gender
    fun toggleVoiceGender(isMale: Boolean) {
        _state.value = _state.value.copy(isMaleVoice = isMale)
        setVoiceGender(isMale)
    }

    //Function to set voice based on gender

    private fun setVoiceGender(isMale: Boolean) {

        textToSpeech?.let { tts ->
            val voices = tts.voices?.toList() ?: return

            //Debug : print available voices
            println("Available Voice :")
            voices.forEach { voice ->
                println("voice:${voice.name} -Features:${voice.features}")
            }

            val targetVoice = if (isMale) {
                //Try to find male voices
                voices.find { voice ->
                    voice.locale.language == "en" &&
                            !voice.isNetworkConnectionRequired &&
                            (voice.name.contains("gb", ignoreCase = true) ||
                                    voice.name.contains("au", ignoreCase = true) ||
                                    voice.name.contains("iob", ignoreCase = true) ||
                                    voice.name.contains("tpf", ignoreCase = true))

                } ?: voices.find {

                    it.locale.language == "en" && !it.isNetworkConnectionRequired
                }

            } else {
                //Try to find female voices
                voices.find { voice ->
                    voice.locale.language == "en" &&
                            !voice.isNetworkConnectionRequired
                    (voice.name.contains("sfg", ignoreCase = true) ||
                            voice.name.contains("ene", ignoreCase = true) ||
                            voice.name.contains("enc", ignoreCase = true))
                } ?: voices.firstOrNull {
                    it.locale.language == "en" && !it.isNetworkConnectionRequired
                }
            }

            targetVoice?.let { voice ->
                tts.voice = voice
                println("selected voice :${voice.name}")

                //Adjust pitch for gender effect

                if (isMale) {
                    tts.setPitch(0.8f)  //lower pitch for male effect

                } else {
                    tts.setPitch(1.0f) //Higher pitch for female effect
                }
            }

        }

    }

    private fun detectLanguage(text: String): Locale {
        return when {
            //Hindi Character (Devanagari Script range )
            text.any { it in '\u0900'..'\u097F' } -> Locale("hi", "IN")

            //Japanese Characters (Hiragana Scripts)
            text.any { it in '\u3040'..'\u309F' } -> Locale("ja", "JP")

            //Hebrew Characters (Hebrew Scripts )
            text.any { it in '\u0590'..'\u05FF' } -> Locale("he", "IL")

            //Arabic Characters (Arabic Scripts )
            text.any { it in '\u0600'..'\u06FF' } -> Locale("ar", "SA")


            //Default to English

            else -> Locale.US
        }
    }

    private fun checkLanguageSupport(locale: Locale) {
        textToSpeech?.let { tts ->
            val result = tts.isLanguageAvailable(locale)
            val isSupported = result == TextToSpeech.LANG_AVAILABLE ||
                    result == TextToSpeech.LANG_COUNTRY_AVAILABLE ||
                    result == TextToSpeech.LANG_COUNTRY_VAR_AVAILABLE
            _state.value = _state.value.copy(isLanguageSupported = isSupported)

            //DEBUG : To see whats going on
            println(" Language : ${locale.displayLanguage}")
            println("TTS Result : $result")
            println("Support :$isSupported")

        }
    }

    fun pauseSpeaking() {
        textToSpeech?.stop()    //stops current speaking
        _state.value = _state.value.copy(isSpeaking = false)
        println("\uD83D\uDED1 Speech PAUSED/STOPPED")
    }

    fun resumeSpeaking() {
        //For now we'll restart from beginning
        speak()
        println("▶\uFE0F speak resumed")
    }

    fun stopSpeaking(){
        textToSpeech?.stop()
        _state.value=_state.value.copy(isSpeaking = false)
        println("\uD83D\uDED1 speech Stopped completely ")
    }

    fun toggleLanguageDropdown (){
        _state.value =_state.value.copy(showLanguageDropdown = !_state.value.showLanguageDropdown)
    }

    fun selectLanguageManually (locale:Locale){
        _state.value =_state.value.copy(selectedLanguage = locale, showLanguageDropdown = false)
        textToSpeech?.language=locale
        checkLanguageSupport(locale)
    }

    //Available language for dropdown
    fun getAvailableLanguage (): List<Pair<Locale, String>>{
        return listOf(
            Locale.US to "English (US)",
            Locale("hi","IN") to "Hindi (हिंदी)",
            Locale("ja","JP") to "Japanese (日本語)",
            Locale("he","IL") to "Hebrew (עברית)",
            Locale("ar","SA") to "Arabic (العربية)"

        )
    }
}