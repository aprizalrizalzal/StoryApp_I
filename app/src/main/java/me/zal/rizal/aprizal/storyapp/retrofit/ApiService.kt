package me.zal.rizal.aprizal.storyapp.retrofit

import me.zal.rizal.aprizal.storyapp.model.SignInModel
import me.zal.rizal.aprizal.storyapp.model.SignInResponse
import me.zal.rizal.aprizal.storyapp.model.SignUpModel
import me.zal.rizal.aprizal.storyapp.model.SignUpResponse
import me.zal.rizal.aprizal.storyapp.model.story.StoriesModel
import me.zal.rizal.aprizal.storyapp.model.story.StoriesResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*


interface ApiService {
    @POST("register")
    fun register(
        @Body signUpModel: SignUpModel?
    ): Call<SignUpResponse?>?

    @POST("login")
    fun login(
        @Body signInModel: SignInModel?
    ): Call<SignInResponse?>?

    @Multipart
    @POST("stories")
    fun addNewStory(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
//        @Body storiesModel: StoriesModel?
    ): Call<StoriesModel?>?

    @GET("stories")
    fun getAllStories(
        @Header("Authorization") token: String,
    ): Call<StoriesResponse?>?
}