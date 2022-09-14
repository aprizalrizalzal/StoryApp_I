package me.zal.rizal.aprizal.storyapp.view.model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import me.zal.rizal.aprizal.storyapp.model.SignUpModel
import me.zal.rizal.aprizal.storyapp.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupViewModel : ViewModel() {

    companion object {
        private const val TAG = "SignupViewModel"
    }

    private val signUpModel = MutableLiveData<SignUpModel>()

    fun getSignUpModel(): MutableLiveData<SignUpModel> {
        return signUpModel
    }

    fun signUp(name: String, email: String, password: String) {
        val client = ApiConfig.getApiService().register(SignUpModel(name, email, password))
        client?.enqueue(object : Callback<SignUpModel?> {
            override fun onResponse(
                call: Call<SignUpModel?>,
                response: Response<SignUpModel?>
            ) {
                if (response.isSuccessful) {
                    signUpModel.value = response.body()
                    Log.d(TAG, "onResponse: signUp ${response.message()}")
                } else {
                    Log.e(TAG, "onFailure: signUp ${response.message()}")
                }
            }

            override fun onFailure(call: Call<SignUpModel?>, t: Throwable) {
                Log.e(TAG, "onFailure: signUp ${t.message}")
            }
        })
    }
}