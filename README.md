# 🌧️ RainCheckApp

**RainCheckApp** is an Android weather forecast application built using **Jetpack Compose** and **MVVM architecture**. It allows users to select a future date and checks whether it will rain in their current location on that day. It uses the **OpenWeatherMap 5-day forecast API** and **Nominatim reverse geocoding** for location name resolution.

---

## 🚀 Features

- 📍 Detects the user's current location using `FusedLocationProviderClient`
- 📅 Allows future date selection (up to 7 days ahead)
- 🔄 Fetches 3-hour interval forecast for the selected date
- 🌦️ Displays weather details including:
    - Temperature
    - Humidity
    - Weather condition
    - Rain prediction
- 🗺️ Reverse geocodes coordinates into a human-readable city/town name
- 🌐 Uses **Retrofit** with Gson/Moshi converters
- 🧪 Handles loading, success, and error states gracefully

---

## 📸 UI Screenshots

| Main Screen | Date Picker | Weather Info |
|-------------|-------------|---------------|
| *(Add screenshots here)* | *(Add screenshots here)* | *(Add screenshots here)* |

---

## 🧱 Architecture

- **MVVM (Model-View-ViewModel)**
- **Jetpack Compose** for declarative UI
- **Coroutines** for asynchronous operations
- **Retrofit** for API communication
- **Modular structure**:
    - `data/`: models, network, repository
    - `domain/`: business models
    - `ui/`: view and ViewModel
    - `utils/`: constants and utility functions

---

## 📦 Dependencies

```kotlin
implementation("androidx.compose.ui:ui:<version>")
implementation("com.google.accompanist:accompanist-permissions:<version>")
implementation("com.squareup.retrofit2:retrofit:<version>")
implementation("com.squareup.retrofit2:converter-gson:<version>")
implementation("com.squareup.moshi:moshi-kotlin:<version>")
implementation("com.google.android.gms:play-services-location:<version>")
```

## 🗂️ Folder Structure
````
RainCheckApp/
├── data/
│   ├── model/
│   ├── models/
│   ├── network/
│   └── repository/
├── domain/
│   └── model/
├── ui/
│   ├── components/
│   ├── view/
│   └── viewmodel/
├── utils/
│   ├── Constants.kt
│   ├── Extensions.kt
│   └── WeatherUtils.kt
└── MainActivity.kt

````

## 🧩 APIs Used

1. **OpenWeatherMap 5-day Forecast API**
   - URL: `https://api.openweathermap.org/data/2.5/forecast`
   - Returns 3-hour weather data for 5 days.
   - Fields used: `dt`, `main.temp`, `main.humidity`, `weather.description`, `rain["3h"]`.

2. **Nominatim Reverse Geocoding**
   - URL: `https://nominatim.openstreetmap.org/reverse`
   - Converts latitude and longitude into a human-readable address (city/town name).
- ---

### 💡 How It Works (High-level Flow)

```markdown
## 💡 How It Works

1. App starts and asks for location permissions.
2. It gets the current location coordinates using `FusedLocationProviderClient`.
3. Reverse geocoding fetches the human-readable location name (e.g., "Berlin").
4. User selects a future date (max 5 days ahead).
5. App calls OpenWeatherMap API and filters forecasts for that date.
6. Uses `willItRain()` utility to determine if it will rain.
7. Displays weather info and rain prediction in a clean UI.

```

## 🐞 Troubleshooting

- ❗**No weather shown?**
  - Make sure you have enabled location permission.
  - Check that your API key is valid and not over quota.

- ⚠️**Location not detected?**
  - Try running on a real device instead of the emulator.
  - Ensure GPS/location services are on.