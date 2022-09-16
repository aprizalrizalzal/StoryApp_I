package me.zal.rizal.aprizal.storyapp.view.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.zal.rizal.aprizal.storyapp.main.UsersPreference
import me.zal.rizal.aprizal.storyapp.model.LoginResult
import me.zal.rizal.aprizal.storyapp.model.SignInModel
import me.zal.rizal.aprizal.storyapp.model.SignInResponse
import me.zal.rizal.aprizal.storyapp.model.users.UserModel
import me.zal.rizal.aprizal.storyapp.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignInViewModel(private val pref: UsersPreference) : ViewModel() {

    companion object {
        private const val TAG = "SignInViewModel"
    }

    private val signInResponse = MutableLiveData<SignInResponse>()
    fun getSignInResponse(): LiveData<SignInResponse> {
        return signInResponse
    }

    private val loginResult = MutableLiveData<LoginResult>()
    fun getLoginResult(): LiveData<LoginResult> {
        return loginResult
    }

    private val isProgress = MutableLiveData<Boolean>()
    fun getIsProgress(): LiveData<Boolean> {
        return isProgress
    }

    private val message = MutableLiveData<String>()
    fun getMessage(): LiveData<String> {
        return message
    }

    fun setSignIn(email: String, password: String) {
        isProgress.value = true
        val client = ApiConfig.getApiService().login(SignInModel(email, password))
        client?.enqueue(object : Callback<SignInResponse?> {
            override fun onResponse(
                call: Call<SignInResponse?>,
                response: Response<SignInResponse?>
            ) {
                if (response.isSuccessful) {
                    Log.d(TAG, "onResponse: ${response.message()}")
                    isProgress.value = false
                    val body = response.body()
                    signInResponse.value = response.body()

                    if (body != null) {
                        Log.d(TAG, "onBody: ${response.message()}")
                        loginResult.value = body.loginResult
                        message.value = body.message
                    } else {
                        Log.w(TAG, "onBody: ${response.message()}")
                    }

                } else{
                    Log.w(TAG, "onResponse: ${response.message()}")
                    isProgress.value = false
                }
            }

            override fun onFailure(call: Call<SignInResponse?>, t: Throwable) {
                isProgress.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun saveSignIn(userModel: UserModel) {
        viewModelScope.launch {
            pref.saveSignIn(userModel)
        }
    }
}