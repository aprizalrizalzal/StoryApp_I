package me.zal.rizal.aprizal.storyapp.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import me.zal.rizal.aprizal.storyapp.view.model.MainViewModel
import me.zal.rizal.aprizal.storyapp.view.model.SignInViewModel
import me.zal.rizal.aprizal.storyapp.view.model.SignupViewModel
import me.zal.rizal.aprizal.storyapp.view.model.StoryViewModel
import me.zal.rizal.aprizal.storyapp.main.UsersPreference

class ViewModelFactory(private val pref: UsersPreference) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(StoryViewModel::class.java) -> {
                StoryViewModel(pref) as T
            }
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(pref) as T
            }
            modelClass.isAssignableFrom(SignupViewModel::class.java) -> {
                SignupViewModel(pref) as T
            }
            modelClass.isAssignableFrom(SignInViewModel::class.java) -> {
                SignInViewModel(pref) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}