package me.zal.rizal.aprizal.storyapp.retrofit

import me.zal.rizal.aprizal.storyapp.model.Login
import me.zal.rizal.aprizal.storyapp.model.LoginResponse
import me.zal.rizal.aprizal.storyapp.model.Register
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST


interface ApiService {
    @POST("register")
    fun register(@Body register: Register?): Call<Register?>?

    @POST("login")
    fun login(@Body login: Login?): Call<LoginResponse?>?
}