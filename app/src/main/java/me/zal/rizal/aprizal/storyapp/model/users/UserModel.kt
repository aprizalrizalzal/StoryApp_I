package me.zal.rizal.aprizal.storyapp.model.users

class UserModel(
    val isLogin: Boolean,
    var email: String,
    var password: String,
    var name: String,
    var token: String,
    var userId: String
)