package com.example.cpsoftest.domain.usecase

import com.example.cpsoftest.domain.repository.CityRepository
import javax.inject.Inject

class RefreshCitiesUseCase @Inject constructor(
    private val repository: CityRepository
) {
    suspend operator fun invoke(): Result<Unit> = repository.refreshCities()
}