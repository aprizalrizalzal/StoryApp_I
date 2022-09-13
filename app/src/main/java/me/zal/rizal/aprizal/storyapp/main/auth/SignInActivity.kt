package me.zal.rizal.aprizal.storyapp.main.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import me.zal.rizal.aprizal.storyapp.R
import me.zal.rizal.aprizal.storyapp.databinding.ActivitySignInBinding
import me.zal.rizal.aprizal.storyapp.main.MainActivity
import me.zal.rizal.aprizal.storyapp.model.SignInModel
import me.zal.rizal.aprizal.storyapp.model.SignInResponse
import me.zal.rizal.aprizal.storyapp.model.LoginResult
import me.zal.rizal.aprizal.storyapp.main.UsersPreference
import me.zal.rizal.aprizal.storyapp.retrofit.ApiConfig
import me.zal.rizal.aprizal.storyapp.view.ViewModelFactory
import me.zal.rizal.aprizal.storyapp.view.model.SignInViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding
    private lateinit var email: String
    private lateinit var password: String
    private var validateField: Boolean = false
    private lateinit var signInViewModel: SignInViewModel

    companion object {
        private const val TAG = "SignInActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        playAnimation()

        binding.btnSignIn.setOnClickListener {
            email = binding.tietEmail.text.toString()
            password = binding.tietPassword.text.toString()

            validateField = isEmptyField()

        }

        binding.tvSignInToSignUp.setOnClickListener {
            val intent = Intent(applicationContext, SignUpActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    }

    private fun isEmptyField(): Boolean {
        if (email.isEmpty()) {
            binding.tilEmail.error = getString(R.string.email_required)
            return false
        } else {
            binding.tilEmail.isErrorEnabled = false
        }

        if (password.isEmpty()) {
            binding.tilPassword.error = getString(R.string.password_required)
            return false
        } else {
            binding.tilPassword.isErrorEnabled = false
        }

        sigInUser()
        return true
    }

    private fun sigInUser() {
        val client = ApiConfig.getApiService().login(SignInModel(email, password))
        client?.enqueue(object : Callback<SignInResponse?> {
            override fun onResponse(
                call: Call<SignInResponse?>,
                response: Response<SignInResponse?>
            ) {
                if (response.isSuccessful) {
                    Log.d(TAG, "onResponse: ${response.message()}")
                    val responseBody = response.body()
                    if (responseBody != null) {
                        val loginResult = responseBody.loginResult
                        setupViewModel(loginResult)
                    } else {
                        Toast.makeText(
                            applicationContext,
                            getString(R.string.failed),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<SignInResponse?>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun setupViewModel(loginResult: LoginResult) {
        signInViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UsersPreference.getInstance(dataStore))
        )[SignInViewModel::class.java]

        signInViewModel.saveSignIn(
            LoginResult(
                loginResult.userId,
                loginResult.name,
                loginResult.token
            )
        )
        signInViewModel.signIn()

        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }
}