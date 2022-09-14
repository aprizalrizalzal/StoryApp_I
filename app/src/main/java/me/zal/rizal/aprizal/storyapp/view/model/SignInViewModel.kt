package me.zal.rizal.aprizal.storyapp.view.model

import android.util.Log
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
    private val loginResult = MutableLiveData<LoginResult>()

    fun getSignInResponse(): MutableLiveData<SignInResponse> {
        return signInResponse
    }

    fun getLoginResult(): MutableLiveData<LoginResult> {
        return loginResult
    }

    fun signIn(email: String, password: String) {
        val client = ApiConfig.getApiService().login(SignInModel(email, password))
        client?.enqueue(object : Callback<SignInResponse?> {
            override fun onResponse(
                call: Call<SignInResponse?>,
                response: Response<SignInResponse?>
            ) {
                if (response.isSuccessful) {
                    Log.d(TAG, "onResponse: signInResponse ${response.message()}")
                    signInResponse.value = response.body()

                    val body = response.body()
                    if (body != null) {
                        Log.d(TAG, "onResponse: loginResult ${response.message()}")
                        loginResult.value = body.loginResult
                    } else {
                        Log.w(TAG, "onFailure: loginResult ${response.message()}")
                    }

                } else {
                    Log.e(TAG, "onFailure: signInResponse ${response.message()}")
                }
            }

            override fun onFailure(call: Call<SignInResponse?>, t: Throwable) {
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