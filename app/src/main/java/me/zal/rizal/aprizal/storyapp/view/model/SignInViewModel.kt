package me.zal.rizal.aprizal.storyapp.view.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.zal.rizal.aprizal.storyapp.model.LoginResult
import me.zal.rizal.aprizal.storyapp.main.UsersPreference

class SignInViewModel(private val pref: UsersPreference) : ViewModel() {

    fun saveSignIn(loginResult: LoginResult) {
        viewModelScope.launch {
            pref.saveSignIn(loginResult)
        }
    }

    fun signIn() {
        viewModelScope.launch {
            pref.signIn()
        }
    }
}