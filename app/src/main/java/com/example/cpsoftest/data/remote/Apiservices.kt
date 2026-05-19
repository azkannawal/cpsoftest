package com.example.cpsoftest.data.remote

import com.example.cpsoftest.data.model.CityDto
import com.example.cpsoftest.data.model.UserDto
import com.example.cpsoftest.domain.model.AddUserRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UserService {
    @GET("user")
    suspend fun getUsers(): Response<List<UserDto>>

    @POST("user")
    suspend fun addUser(@Body request: AddUserRequest): Response<UserDto>
}

interface CityService {
    @GET("city")
    suspend fun getCities(): Response<List<CityDto>>
}