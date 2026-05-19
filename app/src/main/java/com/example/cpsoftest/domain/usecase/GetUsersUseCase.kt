package com.example.cpsoftest.domain.usecase

import com.example.cpsoftest.domain.model.User
import com.example.cpsoftest.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUsersUseCase @Inject constructor(
    private val repository: UserRepository
) {
    operator fun invoke(): Flow<List<User>> = repository.getUsersStream()
}