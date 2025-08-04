package com.shubham.type2speak.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.shubham.type2speak.presentation.viewmodel.TTSViewModel
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import android.content.Intent
import androidx.compose.animation.core.*
import androidx.compose.ui.draw.scale
import kotlinx.coroutines.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll



@Composable
fun TTSScreen(
    viewModel: TTSViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.initializeTTS(context)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
        .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .padding(bottom=48.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        //Title

        Row(modifier =Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center)
        {
            Text(
                text = "Type2Speak",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier =Modifier.padding(vertical = 6.dp)
            )
        }
        OutlinedTextField(
            value = state.text,
            onValueChange = { viewModel.updateText(it) },
            label = { Text("Enter Text to Speak") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )

        //Current State Display

        Text(
            text = "Current text: ${state.text}",
            style = MaterialTheme.typography.bodyMedium
        )

        //Current Language Display

        Row(verticalAlignment = Alignment.CenterVertically)
        {
            Text(
                text = "Detected Language :${state.selectedLanguage.displayLanguage}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = if (state.isLanguageSupported) "✅" else "⚠\uFE0F",
                style = MaterialTheme.typography.bodySmall
            )
        }

        //Manual Language Slider
        //show dropdown when open
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        )
        {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Language",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Button(
                        onClick = { viewModel.toggleLanguageDropdown() },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                    )
                    {
                        Text("${state.selectedLanguage.displayLanguage} ▼ ")
                    }
                }
            }
        }

                if (state.showLanguageDropdown) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        viewModel.getAvailableLanguage().forEach { (locale, displayName) ->
                            Button(
                                onClick = { viewModel.selectLanguageManually(locale) },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor =
                                        if (locale == state.selectedLanguage)
                                            MaterialTheme.colorScheme.primary
                                        else MaterialTheme.colorScheme.surfaceVariant,
                                    contentColor = if (locale == state.selectedLanguage)
                                        MaterialTheme.colorScheme.onPrimary
                                    else MaterialTheme.colorScheme.onSurfaceVariant

                                )
                            ) {
                                Text(displayName)
                            }
                            Spacer(modifier = Modifier.height(4.dp))

                        }
                    }
                }


                //Pitch Slider

                Text(
                    text = "Pitch : ${String.format("%.1f", state.pitch)}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Slider(
                    value = state.pitch,
                    onValueChange = { viewModel.updatePitch(it) },
                    valueRange = 0.5f..2.0f,
                    modifier = Modifier.fillMaxWidth()
                )

                //Speed slider

                Text(
                    text = "Speed :${String.format("%.1f", state.speed)}",
                    style = MaterialTheme.typography.bodyMedium

                )

                Slider(
                    value = state.speed,
                    onValueChange = { viewModel.updateSpeed(it) },
                    valueRange = 0.5f..2.0f,
                    modifier = Modifier.fillMaxWidth()
                )


                //Voice Gender Toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "voice:${if (state.isMaleVoice) "Deep" else "Normal"}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Switch(
                        checked = state.isMaleVoice,
                        onCheckedChange = { viewModel.toggleVoiceGender(it) }
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))

                //Speak Button
        var isPressed by remember { mutableStateOf(false)}

        val scale by animateFloatAsState(
            targetValue = if(isPressed) 0.95f else 1.0f,
            animationSpec = tween(durationMillis = 150)
        )
                Button(
                    onClick = {

                        isPressed =true

                        if (state.isSpeaking) {
                            viewModel.pauseSpeaking()
                            println("Pause Clicked!")
                        } else {
                            viewModel.speak()
                        }

                        //Reset after animation (make it spring back )
                        CoroutineScope(Dispatchers.Main).launch {
                            delay(150)
                            isPressed = false //Button scale back
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .scale(scale),
                    enabled = state.text.isNotEmpty()
                ) {
                    Text(
                        text = if (state.isSpeaking) " ⏸️ PAUSE" else "▶️ SPEAK",
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                //Adding stop Button
                if (state.isSpeaking) {
                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {
                            viewModel.stopSpeaking()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                    ) {
                        Text(
                            text = "⏹\uFE0F STOP",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                // More Voices Button
                Button(
                    onClick = {
                        try {
                            val intent = Intent().apply {
                                action = "com.android.settings.TTS_SETTINGS"
                                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            }
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            //Fallback :open general setting if TTS setting not found
                            val fallbackIntent = Intent().apply {
                                action = android.provider.Settings.ACTION_SETTINGS
                                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            }
                            context.startActivity(fallbackIntent)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary
                    )
                ) {

                    Text(
                        text = "\uD83D\uDCE5 Download More Voices",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }


            }


        }