package me.zal.rizal.aprizal.storyapp.view.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import me.zal.rizal.aprizal.storyapp.model.SignUpModel
import me.zal.rizal.aprizal.storyapp.model.SignUpResponse
import me.zal.rizal.aprizal.storyapp.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupViewModel : ViewModel() {

    companion object {
        private const val TAG = "SignupViewModel"
    }

    private val signUpResponse = MutableLiveData<SignUpResponse>()
    fun getSignUpModel(): LiveData<SignUpResponse> {
        return signUpResponse
    }

    private val isProgress = MutableLiveData<Boolean>()
    fun getIsProgress(): LiveData<Boolean> {
        return isProgress
    }

    private val isToast = MutableLiveData<String>()
    fun getIsToast(): LiveData<String> {
        return isToast
    }

    fun setSignUp(name: String, email: String, password: String) {
        isProgress.value = true
        val client = ApiConfig.getApiService().register(SignUpModel(name, email, password))
        client?.enqueue(object : Callback<SignUpResponse?> {
            override fun onResponse(
                call: Call<SignUpResponse?>,
                response: Response<SignUpResponse?>
            ) {
                if (response.isSuccessful) {
                    Log.d(TAG, "onBody: ${response.message()}")
                    isProgress.value = false
                    signUpResponse.value = response.body()
                    isToast.value = response.body()?.message
                } else {
                    Log.w(TAG, "onBody: ${response.message()}")
                    isProgress.value = false
                }
            }

            override fun onFailure(call: Call<SignUpResponse?>, t: Throwable) {
                Log.e(TAG, "onFailure: signUp ${t.message}")
                isProgress.value = false
            }
        })
    }
}