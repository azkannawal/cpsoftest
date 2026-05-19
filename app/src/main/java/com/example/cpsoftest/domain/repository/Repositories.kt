package com.example.cpsoftest.domain.repository

import com.example.cpsoftest.domain.model.AddUserRequest
import com.example.cpsoftest.domain.model.City
import com.example.cpsoftest.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUsersStream(): Flow<List<User>>
    suspend fun refreshUsers(): Result<Unit>
    suspend fun addUser(request: AddUserRequest): Result<User>
}

interface CityRepository {
    fun getCitiesStream(): Flow<List<City>>
    suspend fun refreshCities(): Result<Unit>
}