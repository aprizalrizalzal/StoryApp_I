package me.zal.rizal.aprizal.storyapp.model

import com.google.gson.annotations.SerializedName

data class SignInModel(

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("password")
    val password: String
)

data class SignInResponse(

    @field:SerializedName("loginResult")
    val loginResult: LoginResult,

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
)

data class LoginResult(

    @field:SerializedName("userId")
    val userId: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("token")
    val token: String
)