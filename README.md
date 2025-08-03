# Type2Speak - Advanced Text-to-Speech Android App
<img width="1024" height="1024" alt="Type2Speak 10" src="https://github.com/user-attachments/assets/e4fda626-a8b2-442b-b290-7c62306539a3" />



## 🎯 Overview

Type2Speak is a modern, feature-rich Text-to-Speech Android application built with **Jetpack Compose** and **MVVM architecture**. The app supports multiple languages with automatic detection, professional audio controls, and a beautiful Material Design 3 interface.

## ✨ Features

### 🌍 Multi-Language Support
- **5 Languages**: English, Hindi (हिंदी), Japanese (日本語), Hebrew (עברית), Arabic (العربية)
- **Auto-Detection**: Automatically detects language based on character scripts
- **Manual Selection**: Dropdown to manually choose language
- **Real-time Validation**: Shows support status for each language

### 🎮 Professional Audio Controls
- **Play/Pause/Stop**: Full control over speech playback
- **Button Animations**: Smooth press animations with professional feedback
- **Dynamic UI**: Buttons change based on speaking state

### 🎛️ Advanced Voice Customization
- **Pitch Control**: Adjustable voice pitch (0.5x - 2.0x)
- **Speed Control**: Variable speaking speed (0.5x - 2.0x)
- **Voice Gender**: Toggle between Normal and Deep voice
- **Accent Variation**: Authentic regional accents when switching languages

### 🎨 Modern UI Design![Uploading Type2Speak 10.png…]()

- **Material Design 3**: Latest design guidelines
- **Card-based Layout**: Organized, professional interface
- **Responsive Controls**: Real-time slider updates
- **Visual Feedback**: Language support indicators (✅/⚠️)

## 🏗️ Technical Architecture

### **MVVM Pattern**
```
├── data/model/
│   ├── TTSState.kt          # App state management
│   └── Language.kt          # Language enums
├── presentation/
│   ├── viewmodel/           # Business logic
│   └── ui/screens/          # Compose UI
└── MainActivity.kt          # App entry point
```

### **Key Technologies**
- **Jetpack Compose**: Modern declarative UI
- **StateFlow**: Reactive state management
- **Android TTS API**: Native text-to-speech
- **Material Design 3**: Google's latest design system
- **Kotlin Coroutines**: Asynchronous operations

## 🚀 Getting Started

### Prerequisites
- Android Studio Arctic Fox or later
- Minimum SDK: 24 (Android 7.0)
- Target SDK: 34 (Android 14)

### Installation
1. Clone the repository:
```bash
git clone https://github.com/yourusername/Type2Speak.git
```

2. Open in Android Studio
3. Sync project with Gradle files
4. Run on device or emulator

### Building from Source
```bash
./gradlew assembleDebug
```

## 📱 Usage

1. **Enter Text**: Type or paste text in multiple languages
2. **Auto-Detection**: App automatically detects language
3. **Manual Override**: Use dropdown to select specific language
4. **Customize Voice**: Adjust pitch, speed, and voice type
5. **Control Playback**: Use Play/Pause/Stop buttons
6. **Download Voices**: Access system TTS settings for more voices

## 🔧 Configuration

### Adding New Languages
To add support for additional languages:

1. Update `detectLanguage()` function in `TTSViewModel`:
```kotlin
text.any { it in '\uXXXX'..'\uYYYY' } -> Locale("lang", "COUNTRY")
```

2. Add to `getAvailableLanguages()` list
3. Test TTS support on target devices

### Customizing Voice Settings
Modify default voice parameters in `TTSViewModel`:
- Default pitch range: `0.5f..2.0f`
- Default speed range: `0.5f..2.0f`
- Voice quality preferences in `setVoiceSettings()`

## 🎨 UI Customization

### Theming
The app uses Material Design 3 with automatic dark/light theme support. Customize colors in:
- `MaterialTheme.colorScheme.primary`
- `MaterialTheme.colorScheme.secondary`
- `MaterialTheme.colorScheme.surface`

### Animations
Button press animations can be adjusted:
```kotlin
animationSpec = tween(durationMillis = 150) // Modify timing
targetValue = if (isPressed) 0.95f else 1.0f // Modify scale
```

## 🧪 Testing

### Supported Languages Testing
Test with these sample texts:
- **English**: "Hello, how are you?"
- **Hindi**: "नमस्ते, आप कैसे हैं?"
- **Japanese**: "こんにちは、元気ですか？"
- **Hebrew**: "שלום, מה שלומך?"
- **Arabic**: "مرحبا، كيف حالك؟"

### Device Compatibility
Tested on:
- Android 7.0+ devices
- Various screen sizes
- Different TTS engines

## 🤝 Contributing

1. Fork the repository
2. Create feature branch: `git checkout -b feature/amazing-feature`
3. Commit changes: `git commit -m 'Add amazing feature'`
4. Push to branch: `git push origin feature/amazing-feature`
5. Open Pull Request

### Development Guidelines
- Follow MVVM architecture patterns
- Use Jetpack Compose best practices
- Maintain Material Design 3 consistency
- Add appropriate comments and documentation

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👨‍💻 Author

**Your Name**
- GitHub: [@yourusername](https://github.com/yourusername)
- LinkedIn: [Your LinkedIn](https://linkedin.com/in/yourprofile)

## 🙏 Acknowledgments

- Android TTS API documentation
- Material Design 3 guidelines
- Jetpack Compose community
- Unicode character set references

## 📱 Screenshots

<!-- Add screenshots here -->
<img width="1080" height="2400" alt="image" src="https://github.com/user-attachments/assets/0156934d-9eaf-46ba-a30a-95082f85c438" />
<img width="1080" height="2400" alt="image" src="https://github.com/user-attachments/assets/c4e2d2c4-11b1-4b7d-8f36-2910f0b6541b" />
<img width="1080" height="2400" alt="image" src="https://github.com/user-attachments/assets/762c5496-304b-43ed-a61e-c0539080f474" />



---

**⭐ If you found this project helpful, please give it a star!**
