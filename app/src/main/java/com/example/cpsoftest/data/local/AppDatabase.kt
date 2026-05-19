package com.example.cpsoftest.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.cpsoftest.data.local.dao.CityDao
import com.example.cpsoftest.data.local.dao.UserDao
import com.example.cpsoftest.data.local.entity.CityEntity
import com.example.cpsoftest.data.local.entity.UserEntity

@Database(
    entities = [UserEntity::class, CityEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun cityDao(): CityDao
}