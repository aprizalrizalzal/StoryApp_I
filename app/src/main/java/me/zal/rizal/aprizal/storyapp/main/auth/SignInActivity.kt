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
import me.zal.rizal.aprizal.storyapp.model.Login
import me.zal.rizal.aprizal.storyapp.model.LoginResponse
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

        setupViewModel()

        binding.btnSignIn.setOnClickListener {
            email = binding.tietEmail.text.toString()
            password = binding.tietPassword.text.toString()

            validateField = isEmptyField()

        }

        binding.tvSignInToSignUp.setOnClickListener {
            val intent = Intent(applicationContext, SignUpActivity::class.java)
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
        val client = ApiConfig.getApiService().login(Login(email, password))
        client?.enqueue(object : Callback<LoginResponse?> {
            override fun onResponse(
                call: Call<LoginResponse?>,
                response: Response<LoginResponse?>
            ) {
                if (response.isSuccessful) {
                    Log.d(TAG, "onResponse: ${response.message()}")
                    val responseBody = response.body()
                    if (responseBody != null) {
                        val loginResult = responseBody.loginResult
                        signInViewModel.saveSignIn(
                            LoginResult(
                                loginResult.userId,
                                loginResult.name,
                                loginResult.token
                            )
                        )
                        signInViewModel.signIn()

                        val intent = Intent(applicationContext, MainActivity::class.java)
                        startActivity(intent)
                        finish()
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

            override fun onFailure(call: Call<LoginResponse?>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun setupViewModel() {
        signInViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UsersPreference.getInstance(dataStore))
        )[SignInViewModel::class.java]
    }
}