package com.example.cpsoftest.data.local.dao

import androidx.room.*
import com.example.cpsoftest.data.local.entity.CityEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CityDao {
    @Query("SELECT * FROM cities")
    fun getAllCities(): Flow<List<CityEntity>>

    @Upsert
    suspend fun upsertAll(cities: List<CityEntity>)
}