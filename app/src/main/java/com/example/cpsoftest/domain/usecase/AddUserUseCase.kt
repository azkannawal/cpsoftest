package com.example.cpsoftest.domain.usecase

import com.example.cpsoftest.domain.model.AddUserRequest
import com.example.cpsoftest.domain.model.User
import com.example.cpsoftest.domain.repository.UserRepository
import javax.inject.Inject

class AddUserUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(request: AddUserRequest): Result<User> =
        repository.addUser(request)
}