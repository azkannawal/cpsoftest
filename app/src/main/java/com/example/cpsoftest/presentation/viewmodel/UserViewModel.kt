package com.example.cpsoftest.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cpsoftest.domain.model.AddUserRequest
import com.example.cpsoftest.domain.model.City
import com.example.cpsoftest.domain.model.User
import com.example.cpsoftest.domain.usecase.AddUserUseCase
import com.example.cpsoftest.domain.usecase.GetCitiesUseCase
import com.example.cpsoftest.domain.usecase.GetUsersUseCase
import com.example.cpsoftest.domain.usecase.RefreshCitiesUseCase
import com.example.cpsoftest.domain.usecase.RefreshUsersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class SortOrder { ASCENDING, DESCENDING, NONE }

sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
    object Idle : UiState<Nothing>()
}

@HiltViewModel
class UserViewModel @Inject constructor(
    private val getUsersUseCase: GetUsersUseCase,
    private val refreshUsersUseCase: RefreshUsersUseCase,
    private val addUserUseCase: AddUserUseCase,
    private val getCitiesUseCase: GetCitiesUseCase,
    private val refreshCitiesUseCase: RefreshCitiesUseCase
) : ViewModel() {

    private val _allUsers = MutableStateFlow<List<User>>(emptyList())
    private val _cities = MutableStateFlow<List<City>>(emptyList())

    val searchQuery = MutableStateFlow("")
    val selectedCity = MutableStateFlow<String?>(null)
    val sortOrder = MutableStateFlow(SortOrder.NONE)

    private val _usersUiState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val usersUiState: StateFlow<UiState<Unit>> = _usersUiState.asStateFlow()

    private val _addUserState = MutableStateFlow<UiState<User>>(UiState.Idle)
    val addUserState: StateFlow<UiState<User>> = _addUserState.asStateFlow()

    val cities: StateFlow<List<City>> = _cities.asStateFlow()

    val filteredUsers: StateFlow<List<User>> = combine(
        _allUsers, searchQuery, selectedCity, sortOrder
    ) { users, query, city, sort ->
        users
            .filter { user ->
                query.isBlank() || user.name.contains(query, ignoreCase = true) ||
                        user.email.contains(query, ignoreCase = true) ||
                        user.city.contains(query, ignoreCase = true)
            }
            .filter { user ->
                city == null || user.city.equals(city, ignoreCase = true)
            }
            .let { filtered ->
                when (sort) {
                    SortOrder.ASCENDING  -> filtered.sortedBy { it.name.lowercase() }
                    SortOrder.DESCENDING -> filtered.sortedByDescending { it.name.lowercase() }
                    SortOrder.NONE       -> filtered
                }
            }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    init {
        // Collect dari Room (otomatis update saat data berubah)
        viewModelScope.launch {
            getUsersUseCase().collect { users ->
                _allUsers.value = users
            }
        }
        viewModelScope.launch {
            getCitiesUseCase().collect { cities ->
                _cities.value = cities
            }
        }
        // Refresh dari API
        loadUsers()
        loadCities()
    }

    fun loadUsers() {
        viewModelScope.launch {
            _usersUiState.value = UiState.Loading
            refreshUsersUseCase()
                .onSuccess { _usersUiState.value = UiState.Success(Unit) }
                .onFailure { error ->
                    _usersUiState.value = if (_allUsers.value.isEmpty())
                        UiState.Error(error.message ?: "Terjadi kesalahan")
                    else
                        UiState.Success(Unit)
                }
        }
    }

    private fun loadCities() {
        viewModelScope.launch {
            refreshCitiesUseCase()
        }
    }

    fun addUser(request: AddUserRequest) {
        viewModelScope.launch {
            _addUserState.value = UiState.Loading
            addUserUseCase(request)
                .onSuccess { newUser ->
                    _addUserState.value = UiState.Success(newUser)
                }
                .onFailure { error ->
                    _addUserState.value = UiState.Error(error.message ?: "Gagal menambahkan user")
                }
        }
    }

    fun resetAddUserState() { _addUserState.value = UiState.Idle }
    fun toggleSort() {
        sortOrder.value = when (sortOrder.value) {
            SortOrder.NONE       -> SortOrder.ASCENDING
            SortOrder.ASCENDING  -> SortOrder.DESCENDING
            SortOrder.DESCENDING -> SortOrder.NONE
        }
    }
    fun selectCity(city: String?) { selectedCity.value = city }
    fun updateSearch(query: String) { searchQuery.value = query }
}