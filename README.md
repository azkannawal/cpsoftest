# CPSOFTEST (Aplikasi Manajemen Data User)

Aplikasi Android untuk menampilkan dan mengelola data user, dibangun menggunakan **Jetpack Compose** dengan Clean architecture.

---

## 📱 Cara Penggunaan Aplikasi

### 1. Halaman Daftar User (`UserListScreen`)

Halaman utama yang otomatis terbuka saat aplikasi dijalankan.

- **Memuat data**: Aplikasi secara otomatis mengambil data user dari API saat pertama kali dibuka. Jika tidak ada koneksi internet, data yang tersimpan secara lokal (cache) tetap ditampilkan.
- **Mencari user**: Ketuk kolom pencarian di bagian atas, lalu ketik nama, email, atau kota user yang ingin dicari. Hasil filter muncul secara real-time.
- **Filter berdasarkan kota**: Geser chip kota di bawah header secara horizontal, lalu ketuk nama kota untuk memfilter user berdasarkan kota tersebut. Ketuk "Semua Kota" untuk menghapus filter.
- **Mengurutkan user**: Ketuk tombol **"Urutkan"** untuk beralih antara urutan A→Z, Z→A, dan tanpa urutan.
- **Reset filter**: Saat filter atau pencarian aktif, muncul banner di bawah chip kota. Ketuk tombol **"Reset"** untuk menghapus semua filter sekaligus.
- **Refresh data**: Jika terjadi error jaringan dan data kosong, halaman error akan menampilkan tombol **"Refresh"** untuk mencoba memuat ulang dari Room secara offline.

### 2. Halaman Tambah User (`AddUserScreen`)

Diakses dengan menekan tombol **+** di pojok kanan bawah halaman daftar user.

- **Mengisi form**: Isi kolom **Nama Lengkap**, **Email**, dan **Nomor Telepon** pada bagian "Informasi Pribadi".
- **Memilih jenis kelamin**: Ketuk kartu **Laki-laki** atau **Perempuan** pada bagian "Jenis Kelamin".
- **Mengisi alamat**: Isi kolom **Alamat**, lalu ketuk dropdown **Kota** untuk memilih kota dari daftar yang tersedia.
- **Menyimpan data**: Ketuk tombol **"Simpan User"** di bagian bawah. Tombol akan menampilkan indikator loading selama proses penyimpanan berlangsung.
- **Validasi form**: Jika ada kolom wajib yang belum diisi atau format tidak valid (misal email salah format, nomor telepon terlalu pendek), pesan error akan muncul tepat di bawah kolom tersebut.
- **Kembali**: Ketuk ikon panah di kiri atas untuk kembali ke halaman daftar tanpa menyimpan.

---

## 🛠 Teknologi yang Digunakan

### Arsitektur & Desain

| Teknologi | Keterangan |
|---|---|
| **Clean Architecture** | Proyek dibagi menjadi tiga layer: `data`, `domain`, dan `presentation`. Setiap layer hanya bergantung ke arah dalam sehingga mudah diuji dan diubah secara independen. |
| **SOLID Principle** | Setiap class memiliki satu tanggung jawab (SRP). ViewModel bergantung pada abstraksi use case/repository, bukan implementasinya (DIP). Repository user dan kota dipisah sebagai interface tersendiri (ISP). |

### UI & Navigasi

| Teknologi | Keterangan |
|---|---|
| **Jetpack Compose** | Seluruh UI dibangun secara deklaratif menggunakan Compose. |
| **Navigation Compose** | Navigasi antar halaman (`UserListScreen` ↔ `AddUserScreen`) dikelola oleh `NavHost` dengan animasi slide dan fade yang dikonfigurasi secara terpusat di `MainActivity`. |
| **Material 3** | Komponen UI mengikuti panduan Material You: `Scaffold`, `Card`, `OutlinedTextField`, `ExposedDropdownMenuBox`, `FloatingActionButton`, dll. |

### Dependency Injection

| Teknologi | Keterangan |
|---|---|
| **Hilt (Dagger Hilt)** | Seluruh dependency (Retrofit, Room, Repository, UseCase) disuntikkan secara otomatis. `AppModule` menyediakan semua binding dengan scope `Singleton` untuk komponen yang berumur panjang. |

### Reactive Programming

| Teknologi | Keterangan |
|---|---|
| **Kotlin Coroutines** | Semua operasi jaringan dan database berjalan di background menggunakan `suspend` function di dalam `viewModelScope`. |
| **Coroutine Flow** | Data dari Room dikembalikan sebagai `Flow` agar UI terupdate otomatis saat data berubah. `StateFlow` dan `combine()` digunakan di ViewModel untuk menggabungkan state pencarian, filter, dan sorting secara reaktif. |

### Jaringan & Data

| Teknologi | Keterangan |
|---|---|
| **Retrofit 2** | Klien HTTP untuk komunikasi dengan REST API (`mockapi.io`). |
| **Gson Converter** | Parsing JSON response dari API ke Kotlin data class (`UserDto`, `CityDto`). |
| **OkHttp + Logging Interceptor** | Menambahkan logging request/response HTTP untuk keperluan debugging. |
| **Room Database** | Penyimpanan lokal untuk user (`users`) dan kota (`cities`) agar data tetap tersedia secara offline. Menggunakan `@Upsert` untuk sinkronisasi data dari API tanpa duplikasi. |

### Pola & Lainnya

| Teknologi | Keterangan |
|---|---|
| **Repository Pattern** | `UserRepositoryImpl` dan `CityRepositoryImpl` menjadi sumber data tunggal (single source of truth), yakni data dari API disinkronkan ke Room, lalu UI membaca dari Room. |
| **Use Case** | Logika bisnis dikemas dalam use case terpisah: `GetUsersUseCase`, `RefreshUsersUseCase`, `AddUserUseCase`, `GetCitiesUseCase`, `RefreshCitiesUseCase`. |
| **Mapper** | Konversi antar model (DTO ↔ Domain ↔ Entity) dilakukan oleh fungsi ekstensi di `UserMapper.kt` agar setiap layer tidak saling bergantung pada model layer lain. |

---

## 🎨 Alasan Tampilan & Interaksi

### Keputusan Desain Visual

**Warna utama biru navy (`#1A3C6E`) dan biru cerah (`#2563EB`)**
Dipilih untuk mencerminkan kesan profesional dan korporat sesuai konteks aplikasi manajemen data internal (CPSSOFT). Gradien dari keduanya digunakan pada header untuk menciptakan kedalaman visual tanpa terlalu ramai.

**Card dengan elevation dan sudut membulat**
Setiap user ditampilkan dalam card terpisah agar mudah dibaca secara sekilas (scannable). Sudut membulat (`16.dp`) mengikuti panduan Material You yang terasa lebih modern dan ramah di mata.

**Avatar berbasis inisial**
Karena tidak ada foto profil dari API, inisial nama (maksimal 2 karakter) dengan latar gradien digunakan sebagai pengganti avatar. Ini lebih bermakna dibanding ikon generik dan membantu pengguna mengidentifikasi user dengan cepat.

**Badge gender berwarna**
Biru untuk laki-laki, merah muda untuk perempuan.

### Keputusan Interaksi

**Filter kota sebagai chip horizontal scroll**
Chip lebih efisien daripada dropdown untuk filter dengan jumlah pilihan sedikit karena semua opsi langsung terlihat. Horizontal scroll dipilih agar tidak memakan ruang vertikal yang berharga.

**Banner aktif filter**
Saat filter atau pencarian aktif, muncul banner kecil yang menampilkan filter yang sedang berlaku beserta tombol reset. Ini memberikan feedback eksplisit kepada pengguna bahwa hasil yang ditampilkan adalah hasil filter, bukan keseluruhan data.

**Shimmer loading sebagai pengganti spinner**
Skeleton/shimmer card ditampilkan saat data sedang dimuat, bukan hanya spinner polos. Ini memberikan gambaran awal tentang struktur konten yang akan muncul, sehingga terasa lebih cepat secara persepsi.

**Validasi inline pada form**
Pesan error ditampilkan langsung di bawah kolom yang bermasalah (bukan di popup/snackbar), sehingga pengguna tahu persis kolom mana yang perlu diperbaiki tanpa harus menebak-nebak.

**Tombol submit dengan loading state**
Saat data sedang dikirim ke API, tombol berubah menampilkan spinner dan teks "Menyimpan..." serta menjadi tidak dapat diklik. Ini mencegah pengiriman data ganda dan memberikan feedback bahwa proses sedang berjalan.

**Animasi navigasi slide + fade**
Navigasi dari daftar ke form menggunakan slide dari kanan (masuk) dan slide ke kiri (keluar), yang merupakan pola navigasi Android/iOS yang sudah sangat familiar bagi pengguna. Ini menciptakan hierarki visual yang jelas: layar baru "mendorong" layar lama.

**Offline-first dengan fallback**
Saat jaringan terputus namun data lokal tersedia, aplikasi tetap menampilkan data tanpa error. Error hanya ditampilkan jika data lokal juga kosong, sehingga aplikasi tetap berguna meski dalam kondisi koneksi buruk.

---

## 📂 Struktur Proyek

```
cpsoftest/
├── data/
│   ├── local/          # Room: AppDatabase, DAO, Entity
│   ├── mapper/         # Konversi DTO ↔ Domain ↔ Entity
│   ├── model/          # DTO (UserDto, CityDto)
│   ├── remote/         # Retrofit API Services
│   └── repository/     # Implementasi Repository
├── di/
│   └── AppModule.kt    # Hilt Dependency Injection
├── domain/
│   ├── model/          # Domain Model (User, City, AddUserRequest)
│   ├── repository/     # Interface Repository
│   └── usecase/        # Business Logic Use Cases
├── presentation/
│   ├── component/      # Komponen UI reusable
│   ├── screen/         # Halaman utama (UserListScreen, AddUserScreen)
│   └── viewmodel/      # UserViewModel, UiState, SortOrder
├── ui/theme/           # Warna, Tipografi, Theme Material 3
├── MainActivity.kt     # Entry point, NavHost, navigasi
└── MainApplication.kt  # HiltAndroidApp
```

---

## ⚠️ Teknologi yang Belum Diimplementasikan

| Teknologi | Status | Catatan |
|---|---|---|
| **Moshi** | ❌ Belum | Saat ini menggunakan Gson |
| **Adaptive Layout** | ❌ Belum | Belum ada dukungan tablet/landscape |
| **WorkManager** | ❌ Belum | Belum ada background sync terjadwal |
| **Firebase** | ❌ Belum | Belum ada analytics/crashlytics |