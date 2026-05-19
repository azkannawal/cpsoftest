package com.example.cpsoftest.data.model

import com.google.gson.annotations.SerializedName

data class UserDto(
    @SerializedName("id")          val id: String = "",
    @SerializedName("name")        val name: String = "",
    @SerializedName("address")     val address: String = "",
    @SerializedName("email")       val email: String = "",
    @SerializedName("phoneNumber") val phoneNumber: String = "",
    @SerializedName("city")        val city: String = "",
    @SerializedName("gender")      val gender: Int = 0
)

data class CityDto(
    @SerializedName("id")   val id: String = "",
    @SerializedName("name") val name: String = ""
)