package me.zal.rizal.aprizal.storyapp.view.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.zal.rizal.aprizal.storyapp.model.users.UserModel
import me.zal.rizal.aprizal.storyapp.main.UsersPreference

class SignupViewModel(private val pref: UsersPreference) : ViewModel() {
    fun saveSignUp(user: UserModel) {
        viewModelScope.launch {
            pref.saveSignUp(user)
        }
    }
}