package com.example.cpsoftest.domain.usecase

import com.example.cpsoftest.domain.model.City
import com.example.cpsoftest.domain.repository.CityRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCitiesUseCase @Inject constructor(
    private val repository: CityRepository
) {
    operator fun invoke(): Flow<List<City>> = repository.getCitiesStream()
}