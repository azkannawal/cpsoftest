package com.example.cpsoftest.domain.model

data class User(
    val id: String = "",
    val name: String = "",
    val address: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val city: String = "",
    val gender: Int = 0
)

data class City(
    val id: String = "",
    val name: String = ""
)

data class AddUserRequest(
    val name: String,
    val address: String,
    val email: String,
    val phoneNumber: String,
    val city: String,
    val gender: Int
)