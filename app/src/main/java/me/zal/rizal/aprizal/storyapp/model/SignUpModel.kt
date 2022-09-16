package me.zal.rizal.aprizal.storyapp.model

import com.google.gson.annotations.SerializedName

data class SignUpModel(

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("password")
    val password: String

)

data class SignUpResponse(

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String

)
