# Political Science 2nd Paper Suggestion Pro
### রাষ্ট্রবিজ্ঞান দ্বিতীয় পত্র চূড়ান্ত সাজেশন ও প্রশ্নব্যাংক প্রো (বিষয় কোড: ১১১৯০৩)

**Political Science 2nd Paper Suggestion Pro** is a production-grade, native Android application engineered for National University (জাতীয় বিশ্ববিদ্যালয়) BSS Degree (Pass) and Honours 1st Year students studying **Political Science 2nd Paper (Political Organization & the Political System of UK & USA / রাজনৈতিক সংগঠন এবং ব্রিটেন ও মার্কিন যুক্তরাষ্ট্রের রাজনৈতিক ব্যবস্থা)**.

The application allows students to import past university question paper PDFs, automatically extracts text, runs OCR on scanned pages, detects years (2010–2024/2026), groups repeated questions (exact, reworded, and conceptual), computes an evidence-based **Analytical Priority Score**, generates exam-oriented structured answers, reads questions aloud via Bengali Text-to-Speech (TTS), exports formatted A4 PDFs, prints study materials, and works completely offline with local Room database persistence.

---

## 🌟 Key Features

### 1. 📄 Real PDF Import & OCR Pipeline
- **Document Picker**: Integration with Android System Document Picker (`OpenDocument`).
- **Scanned & Image Detection**: Renders pages via native `android.graphics.pdf.PdfRenderer`.
- **Step-by-Step Progress Feedback**:
  1. Reading pages
  2. Extracting text
  3. OCR processing for scanned pages
  4. Bengali grammar & text cleanup
  5. Section detection (ক, খ, গ, MCQ)
  6. Year frequency & repeat analysis
  7. Final suggestion generation
- Background asynchronous execution using Kotlin Coroutines and StateFlow.

### 2. 🎯 Question Classification & Year Extraction
- **MCQ Master**: Identifies multiple-choice questions with options and highlighted answers.
- **ক-বিভাগ (Section A - Brief Questions)**: Concise, precise 1-mark answers.
- **খ-বিভাগ (Section B - Short Questions)**: Structured 4-mark answers (ভূমিকা, পয়েন্ট ১–৪, উপসংহার).
- **গ-বিভাগ (Section C - Broad Questions)**: Detailed 10-mark essay answers (ভূমিকা, সংজ্ঞা, বিশ্লেষণাত্মক পয়েন্ট ১–৬, মূল্যায়ন, উপসংহার).
- **Year Detection**: Extracts appearances from 2010 to 2024 without fabricating years (marks "Year unclear" if uncertain).

### 3. 🔁 Repeat Question & Semantic Similarity Engine
- **Exact Repeat**: Verifies identical or nearly identical questions across years.
- **Reworded Repeat**: Detects same question concept with differing phrasing.
- **Conceptual Repeat**: Matches core academic concepts (e.g. *“সংসদীয় সরকারের বৈশিষ্ট্য”* vs *“সংসদীয় শাসনব্যবস্থার প্রধান বৈশিষ্ট্যগুলো কী?”*).
- **Bengali Text Normalization**: Diacritic stripping, Bengali numeral conversion (০-৯ ↔ 0-9), and stop-word filtering.

### 4. ⚖️ Explainable Priority System (Academic Integrity)
- Categorizes questions into:
  - 🔥 **VERY HIGH PRIORITY** (Score 88–99%)
  - ⭐ **HIGH PRIORITY** (Score 75–87%)
  - 📌 **IMPORTANT** (Score 60–74%)
  - 📖 **REVISION** (Score 40–59%)
- **Ethical Academic Rule**: Explicitly labeled as statistical analytical priority scores; strictly avoids sensational claims like *"100% guaranteed"*.
- **"Why this is important" Explanation**: Fully transparent scoring breakdown based on frequency, span of years, and section weight.

### 5. 👁️ Source Traceability & Anti-Hallucination
- Every question retains:
  - Source document name
  - Source page number
  - Historical exam years
  - Original unedited extracted text
- Tap **"উৎস দেখুন" (View Source)** on any question to view the full audit trail.

### 6. 🔊 Android Bengali Text-to-Speech (TTS)
- Integrated Android `TextToSpeech` with support for Bengali (`bn-BD` / `bn-IN`) and English fallback.
- Adjustable speech rate (0.75x to 1.5x).
- Play, stop, and active audio wave status indicator.

### 7. 🖨️ Real PDF Generation, Download & Printing
- **Full Suggestion PDF**: Multi-page formatted A4 document with cover, stats, questions, model answers, page numbers, and headers.
- **Revision Only PDF**: Export high-priority capsule questions for last-minute exam prep.
- **Section Handnote PDF**: Export specific sections (ক, খ, or গ).
- **Android Print**: Direct printing via Android `PrintManager` and `PrintDocumentAdapter`.
- Saved locally to device storage (`Documents/Suggestions` and `Downloads/PoliticalScience`).

### 8. 🤖 Multi-Provider AI Architecture
- Pluggable `AIProvider` interface supporting:
  - **OpenRouter AI** (`google/gemini-2.0-flash-exp:free` or custom)
  - **Google Gemini AI** (`gemini-2.5-flash`)
  - **In-App Local Analytical Engine** (Offline fallback)
- User configurable API keys in Settings.

### 9. 💾 Offline Room Database & Study Progress
- SQLite local database via Android Jetpack Room.
- Pre-seeded with authentic National University question bank covering:
  - সংবিধান (Constitution)
  - সরকারের প্রকারভেদ (Forms of Government)
  - ক্ষমতা স্বতন্ত্রীকরণ নীতি (Separation of Powers)
  - সরকারের অঙ্গসমূহ (Organs of Government)
  - রাজনৈতিক আচরণ (Political Behaviour)
  - ব্রিটিশ রাজনৈতিক ব্যবস্থা (UK System)
  - মার্কিন রাজনৈতিক ব্যবস্থা (USA System)
- Bookmarks management.
- Study progress dashboard (% completed, audio hours, revision tracker).

### 10. 🌐 Multilingual & Premium Material 3 UI
- Languages: **বাংলা (Bengali - Primary)**, English, العربية (Arabic).
- Dark and Light theme support.
- Responsive design for phones and tablets.
- Material 3 cards, badges, and smooth animations.

---

## 🛠️ Architecture & Tech Stack

- **Language**: Kotlin 2.2+
- **UI Toolkit**: Jetpack Compose with Material Design 3 (M3)
- **Architecture**: MVVM (Model-View-ViewModel) with StateFlow
- **Database**: Android Jetpack Room 2.7.0 (KSP code generation)
- **Networking**: OkHttp 4.10, Retrofit 2.12
- **Audio**: Android Native `TextToSpeech`
- **Document & Print**: Android `PdfDocument`, `PdfRenderer`, `PrintManager`
- **Target SDK**: Android 36 (Android 15)
- **Minimum SDK**: Android 24 (Android 7.0 Nougat)
- **Java Version**: JDK 17

---

## 📦 Building the Project Locally

### Prerequisites
- Android Studio Ladybug / Meerkat or newer
- JDK 17
- Android SDK Platform 36

### Build Commands
To run local unit tests:
```bash
./gradlew testDebugUnitTest
```

To build the debug APK:
```bash
./gradlew assembleDebug
```
The output APK will be generated at:
```
app/build/outputs/apk/debug/app-debug.apk
```

---

## 🚀 GitHub Actions Automated Build Pipeline

The workflow `.github/workflows/android-build.yml` runs on every push and pull request:
1. Sets up JDK 17 & Android SDK.
2. Runs local unit tests (`testDebugUnitTest`).
3. Executes `./gradlew assembleDebug`.
4. Validates that `app-debug.apk` is generated, non-empty, and structurally valid via `unzip -t`.
5. Compares SHA256 integrity checksums across `.build-outputs/` and `APK_DOWNLOAD/`.
6. Uploads the debug APK as a downloadable GitHub Actions artifact (`app-debug-apk`).
7. Updates the distribution folder `APK_DOWNLOAD/app-debug.apk`.

---

## 📄 License

This project is licensed under the Apache 2.0 License.
