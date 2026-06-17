# CPSOFTEST (User Data Management Application)

An Android application for displaying and managing user data, built using **Jetpack Compose** with Clean Architecture.

## 🛠 Technologies Used

### Architecture & Design

| Technology | Description |
|---|---|
| **Clean Architecture** | The project is divided into three layers: `data`, `domain`, and `presentation`. Each layer depends only inward, making it easy to test and modify independently. |
| **SOLID Principles** | Each class has a single responsibility (SRP). ViewModel depends on use case/repository abstractions rather than their implementations (DIP). User and city repositories are separated into distinct interfaces (ISP). |

### UI & Navigation

| Technology | Description |
|---|---|
| **Jetpack Compose** | The entire UI is built declaratively using Compose. |
| **Navigation Compose** | Navigation between pages (`UserListScreen` ↔ `AddUserScreen`) is managed by `NavHost` with slide and fade animations configured centrally in `MainActivity`. |
| **Material 3** | UI components follow Material You guidelines: `Scaffold`, `Card`, `OutlinedTextField`, `ExposedDropdownMenuBox`, `FloatingActionButton`, etc. |

### Dependency Injection

| Technology | Description |
|---|---|
| **Hilt (Dagger Hilt)** | All dependencies (Retrofit, Room, Repository, UseCase) are injected automatically. `AppModule` provides all bindings with `Singleton` scope for long-lived components. |

### Reactive Programming

| Technology | Description |
|---|---|
| **Kotlin Coroutines** | All network and database operations run in the background using `suspend` functions within `viewModelScope`. |
| **Coroutine Flow** | Data from Room is returned as a `Flow` so the UI updates automatically when data changes. `StateFlow` and `combine()` are used in the ViewModel to reactively merge search, filter, and sorting state. |

### Networking & Data

| Technology | Description |
|---|---|
| **Retrofit 2** | HTTP client for communicating with the REST API (`mockapi.io`). |
| **Gson Converter** | Parses JSON responses from the API into Kotlin data classes (`UserDto`, `CityDto`). |
| **OkHttp + Logging Interceptor** | Adds HTTP request/response logging for debugging purposes. |
| **Room Database** | Local storage for users (`users`) and cities (`cities`) so data remains available offline. Uses `@Upsert` to sync data from the API without duplication. |

### Patterns & Others

| Technology | Description |
|---|---|
| **Repository Pattern** | `UserRepositoryImpl` and `CityRepositoryImpl` serve as the single source of truth from the API is synced to Room, and the UI then reads from Room. |
| **Use Case** | Business logic is encapsulated in separate use cases: `GetUsersUseCase`, `RefreshUsersUseCase`, `AddUserUseCase`, `GetCitiesUseCase`, `RefreshCitiesUseCase`. |
| **Mapper** | Conversion between models (DTO ↔ Domain ↔ Entity) is handled by extension functions in `UserMapper.kt` so that each layer remains independent of other layers' models. |

## 📂 Project Structure

```
cpsoftest/
├── data/
│   ├── local/          # Room: AppDatabase, DAO, Entity
│   ├── mapper/         # DTO ↔ Domain ↔ Entity conversion
│   ├── model/          # DTOs (UserDto, CityDto)
│   ├── remote/         # Retrofit API Services
│   └── repository/     # Repository implementations
├── di/
│   └── AppModule.kt    # Hilt Dependency Injection
├── domain/
│   ├── model/          # Domain Models (User, City, AddUserRequest)
│   ├── repository/     # Repository interfaces
│   └── usecase/        # Business logic use cases
├── presentation/
│   ├── component/      # Reusable UI components
│   ├── screen/         # Main pages (UserListScreen, AddUserScreen)
│   └── viewmodel/       # UserViewModel, UiState, SortOrder
├── ui/theme/           # Colors, Typography, Material 3 Theme
├── MainActivity.kt     # Entry point, NavHost, navigation
└── MainApplication.kt  # HiltAndroidApp
```

## ⚠️ Technologies Not Yet Implemented

| Technology | Status | Notes |
|---|---|---|
| **Moshi** | ❌ Not yet | Currently using Gson |
| **Adaptive Layout** | ❌ Not yet | No tablet/landscape support yet |
| **WorkManager** | ❌ Not yet | No scheduled background sync yet |
| **Firebase** | ❌ Not yet | No analytics/crashlytics yet |
