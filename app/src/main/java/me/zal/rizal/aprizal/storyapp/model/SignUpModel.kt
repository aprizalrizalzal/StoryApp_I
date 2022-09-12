package me.zal.rizal.aprizal.storyapp.model

import com.google.gson.annotations.SerializedName

data class Register(

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("password")
    val password: String

)
