package me.zal.rizal.aprizal.storyapp.model.story

import com.google.gson.annotations.SerializedName

class AddStoryModel(

    @field:SerializedName("description")
    val description: String,

    /*@field:SerializedName("lat")
    var lat: Float,

    @field:SerializedName("lon")
    var lon: Float*/
)

data class AddStoryResponse(

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
)
