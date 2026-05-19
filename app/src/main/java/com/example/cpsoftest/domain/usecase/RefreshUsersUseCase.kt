package com.example.cpsoftest.domain.usecase

import com.example.cpsoftest.domain.repository.UserRepository
import javax.inject.Inject

class RefreshUsersUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(): Result<Unit> = repository.refreshUsers()
}