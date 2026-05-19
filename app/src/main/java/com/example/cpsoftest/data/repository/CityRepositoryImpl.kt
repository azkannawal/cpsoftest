package com.example.cpsoftest.data.repository

import com.example.cpsoftest.data.local.dao.CityDao
import com.example.cpsoftest.data.mapper.toDomain
import com.example.cpsoftest.data.mapper.toEntity
import com.example.cpsoftest.data.remote.CityService
import com.example.cpsoftest.domain.model.City
import com.example.cpsoftest.domain.repository.CityRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CityRepositoryImpl @Inject constructor(
    private val cityService: CityService,
    private val cityDao: CityDao
) : CityRepository {

    override fun getCitiesStream(): Flow<List<City>> =
        cityDao.getAllCities().map { list -> list.map { it.toDomain() } }

    override suspend fun refreshCities(): Result<Unit> {
        return try {
            val response = cityService.getCities()
            if (response.isSuccessful) {
                val cities = response.body()?.map { it.toDomain() } ?: emptyList()
                cityDao.upsertAll(cities.map { it.toEntity() })
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}