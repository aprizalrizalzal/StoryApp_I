package me.zal.rizal.aprizal.storyapp.view.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.zal.rizal.aprizal.storyapp.main.UsersPreference

class StoryViewModel(private val pref: UsersPreference) : ViewModel() {
    fun signOut() {
        viewModelScope.launch {
            pref.signOut()
        }
    }
}