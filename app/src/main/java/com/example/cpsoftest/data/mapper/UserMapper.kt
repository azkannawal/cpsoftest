package com.example.cpsoftest.data.mapper

import com.example.cpsoftest.data.local.entity.CityEntity
import com.example.cpsoftest.data.local.entity.UserEntity
import com.example.cpsoftest.data.model.CityDto
import com.example.cpsoftest.data.model.UserDto
import com.example.cpsoftest.domain.model.City
import com.example.cpsoftest.domain.model.User

fun UserDto.toDomain(): User = User(
    id          = id,
    name        = name,
    address     = address,
    email       = email,
    phoneNumber = phoneNumber,
    city        = city,
    gender      = gender
)

fun CityDto.toDomain(): City = City(
    id   = id,
    name = name
)

fun UserEntity.toDomain(): User = User(
    id          = id,
    name        = name,
    address     = address,
    email       = email,
    phoneNumber = phoneNumber,
    city        = city,
    gender      = gender
)

fun CityEntity.toDomain(): City = City(
    id   = id,
    name = name
)

fun User.toEntity(): UserEntity = UserEntity(
    id          = id,
    name        = name,
    address     = address,
    email       = email,
    phoneNumber = phoneNumber,
    city        = city,
    gender      = gender
)

fun City.toEntity(): CityEntity = CityEntity(
    id   = id,
    name = name
)