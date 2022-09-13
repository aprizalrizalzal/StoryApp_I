package me.zal.rizal.aprizal.storyapp.model.story

import com.google.gson.annotations.SerializedName

class StoriesModel(

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("photo")
    var photo: String,

    @field:SerializedName("lat")
    var lat: Float,

    @field:SerializedName("lon")
    var lon: Float
)
