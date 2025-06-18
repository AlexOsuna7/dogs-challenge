# 🐶 Dogs Challenge App

A modern Android application that displays a list of dogs, built following industry best practices, including **Clean Architecture**, **Dependency Injection**, and a reactive UI with **Jetpack Compose**.

---

## 📱 Screenshots

This section showcases how the application handles its three main states: **Success**, **Loading**, and **Error**.

| Success State | Loading State | Error State |
|---------------|----------------|--------------|
| ![success-samsung](https://github.com/user-attachments/assets/c98027e3-8bd9-4da7-9452-5ca3b3a75e91) | ![Loading](https://github.com/user-attachments/assets/c66b85b5-5e28-4abf-8eaa-31c47f684437) | ![error](https://github.com/user-attachments/assets/2960412c-7b5c-4fda-959b-471964194a8b) |

---

### ✅ Success State
Displays the list of dogs with a clean and attractive design.

### ⏳ Loading State
A skeleton loader with a shimmer effect is used to indicate that data is being loaded. This enhances the user experience by showing the UI structure before the content is available.

### ❌ Error State
The error screen is user-friendly, displaying an icon, a clear message, and a **Retry** button, allowing the user to recover from network failures.

---

## 🏛️ Architecture

This project implements a strict **Clean Architecture** to ensure a clear separation of concerns, high testability, and easy maintenance. The application is divided into three main layers:

### 1. 📂 `domain`
The core layer of the application. It is a **pure Kotlin module** with no dependencies on Android frameworks.

- **Responsibility:** Contains the business logic and rules.
- **Components:**
  - `Dog`: The main data model.
  - `DogRepository`: Interface defining the contract for data sources.
  - `GetDogsUseCase`: Use case encapsulating the logic to fetch the dog list.

---

### 2. 📂 `data`
Responsible for providing data to the domain layer.

- **Responsibility:** Implements `DogRepository` and manages data sources.
- **Components:**
  - `remote`: Retrofit code for network (`ApiService`, `DogDto`).
  - `local`: Room persistence layer (`DogEntity`, `DogDao`, `AppDatabase`).
  - `repository`: `DogRepositoryImpl` handles logic to choose between API and local DB.

---

### 3. 📂 `presentation`
The UI layer. The only layer aware of the Android framework.

- **Responsibility:** Display data and handle user interactions.
- **Components:**
  - `ui`: Jetpack Compose UI (`DogListScreen`, `DogCard`).
  - `viewmodel`: `DogListViewModel` connects UI to domain and manages state.
  - `state`: `DogListState` sealed class to represent all UI states (Loading, Success, Error).

---

## ✨ Key Features & Tech Stack

- ✅ 100% **Kotlin**
- 🔄 **Coroutines & Flow** for async operations
- 🧱 **Jetpack Compose** for reactive UI
- 🏗️ **Clean Architecture** for modular, scalable code
- 🛠️ **Hilt** for Dependency Injection
- 📥 **Offline-First** strategy using **Room**
- 🌐 **Retrofit** for network calls
- 🖼️ **Coil** for image loading
- 📊 **Explicit UI State Handling** using `StateFlow`

---

## 🧪 Testing

The project is fully testable with unit tests across key layers:

- **ViewModel (`DogListViewModelTest`)**
  - Validates correct emission of UI states (Loading, Success, Error) using **Turbine**.
- **Repository (`DogRepositoryImplTest`)**
  - Tests the offline-first logic.
- **Testing Frameworks**
  - Uses **JUnit 5** and **Mockito**.

---

## 🚀 How to Run the Project

1. Clone this repository.
2. Open the project in the latest version of **Android Studio**.
3. Sync the project with **Gradle**.
4. Run the application on an emulator or physical device.
