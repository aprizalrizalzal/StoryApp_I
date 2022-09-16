package me.zal.rizal.aprizal.storyapp.view.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import me.zal.rizal.aprizal.storyapp.main.UsersPreference
import me.zal.rizal.aprizal.storyapp.model.story.AddStoryResponse
import me.zal.rizal.aprizal.storyapp.model.users.UserModel
import me.zal.rizal.aprizal.storyapp.retrofit.ApiConfig
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddStoryViewModel(private val pref: UsersPreference) : ViewModel() {

    companion object {
        private const val TAG = "AddStoryViewModel"
    }

//    private val addStory = MutableLiveData<AddStoryModel>()
//    fun getAddStory(): LiveData<AddStoryModel> {
//        return addStory
//    }

    private val isProgress = MutableLiveData<Boolean>()
    fun getIsProgress(): LiveData<Boolean> {
        return isProgress
    }

    private val isToast = MutableLiveData<String>()
    fun getIsToast(): LiveData<String> {
        return isToast
    }

    fun setAddStory(
        token: String,
        file: MultipartBody.Part, /*lat: Float, lon: Float*/
        descriptions: RequestBody
    ) {
        isProgress.value = true
        val client = ApiConfig.getApiService()
            .addNewStory(token, file, descriptions)
        client?.enqueue(object : Callback<AddStoryResponse?> {
            override fun onResponse(
                call: Call<AddStoryResponse?>,
                response: Response<AddStoryResponse?>
            ) {
                if (response.isSuccessful) {
                    isProgress.value = false
                    val body = response.body()
                    if (body != null && !body.error) {
                        Log.d(TAG, "onBody: ${body.message}")
                        isToast.value = body.message
                    } else {
                        Log.w(TAG, "onBody: ${response.message()}")
                    }
                } else {
                    Log.w(TAG, "onResponse: ${response.message()}")
                    isProgress.value = false
                    isToast.value = response.message()
                }
            }

            override fun onFailure(call: Call<AddStoryResponse?>, t: Throwable) {
                Log.e(TAG, "onFailure: ", t)
                isProgress.value = false
            }
        })
    }

    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

}
