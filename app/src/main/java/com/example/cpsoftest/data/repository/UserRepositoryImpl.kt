package com.example.cpsoftest.data.repository

import com.example.cpsoftest.data.local.dao.UserDao
import com.example.cpsoftest.data.mapper.toDomain
import com.example.cpsoftest.data.mapper.toEntity
import com.example.cpsoftest.data.remote.UserService
import com.example.cpsoftest.domain.model.AddUserRequest
import com.example.cpsoftest.domain.model.User
import com.example.cpsoftest.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userService: UserService,
    private val userDao: UserDao
) : UserRepository {

    // Ambil dari Room, return sebagai Flow
    override fun getUsersStream(): Flow<List<User>> =
        userDao.getAllUsers().map { list -> list.map { it.toDomain() } }

    // Sync API ke Room
    override suspend fun refreshUsers(): Result<Unit> {
        return try {
            val response = userService.getUsers()
            if (response.isSuccessful) {
                val users = response.body()
                    ?.distinctBy { it.id }
                    ?.map { it.toDomain() }
                    ?: emptyList()
                userDao.upsertAll(users.map { it.toEntity() })
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error ${response.code()}: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)  // offline
        }
    }

    override suspend fun addUser(request: AddUserRequest): Result<User> {
        return try {
            val response = userService.addUser(request)
            if (response.isSuccessful && response.body() != null) {
                val newUser = response.body()!!.toDomain()
                userDao.insert(newUser.toEntity())  // simpan ke Room
                Result.success(newUser)
            } else {
                Result.failure(Exception("Gagal menambahkan user"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}