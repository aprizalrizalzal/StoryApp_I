package me.zal.rizal.aprizal.storyapp.view.model

import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import me.zal.rizal.aprizal.storyapp.main.UsersPreference
import me.zal.rizal.aprizal.storyapp.model.story.ListStoryItem
import me.zal.rizal.aprizal.storyapp.model.story.StoriesResponse
import me.zal.rizal.aprizal.storyapp.model.users.UserModel
import me.zal.rizal.aprizal.storyapp.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoriesViewModel(private val pref: UsersPreference) : ViewModel() {

    companion object {
        private const val TAG = "StoriesViewModel"
    }

    private val stories = MutableLiveData<List<ListStoryItem>>()
    fun getAllStories(): LiveData<List<ListStoryItem>> {
        return stories
    }

    private val isProgress = MutableLiveData<Boolean>()
    fun getIsProgress(): LiveData<Boolean> {
        return isProgress
    }

    fun setAllStories(token: String) {
        isProgress.value = true
        val client = ApiConfig.getApiService().getAllStories(token)
        client?.enqueue(object : Callback<StoriesResponse?> {
            override fun onResponse(
                call: Call<StoriesResponse?>,
                response: Response<StoriesResponse?>
            ) {
                if (response.isSuccessful) {
                    Log.d(TAG, "onResponse: ${response.message()}")
                    isProgress.value = false
                    val body = response.body()
                    if (body != null && !body.error) {
                        Log.d(TAG, "onBody: ${body.message}")
                        stories.value = body.listStory
                    } else {
                        Log.w(TAG, "onBody: ${response.message()}")
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    isProgress.value = false
                }
            }

            override fun onFailure(call: Call<StoriesResponse?>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
                isProgress.value = false
            }
        })
    }

    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    fun signOut() {
        viewModelScope.launch {
            pref.signOut()
        }
    }
}