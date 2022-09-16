package me.zal.rizal.aprizal.storyapp.main.auth

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import me.zal.rizal.aprizal.storyapp.R
import me.zal.rizal.aprizal.storyapp.addition.CustomProgressDialog
import me.zal.rizal.aprizal.storyapp.databinding.ActivitySignInBinding
import me.zal.rizal.aprizal.storyapp.main.StoryActivity
import me.zal.rizal.aprizal.storyapp.main.UsersPreference
import me.zal.rizal.aprizal.storyapp.model.users.UserModel
import me.zal.rizal.aprizal.storyapp.view.ViewModelFactory
import me.zal.rizal.aprizal.storyapp.view.model.SignInViewModel

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding
    private lateinit var progressDialog: CustomProgressDialog
    private lateinit var email: String
    private lateinit var password: String
    private var validateField: Boolean = false
    private lateinit var signInViewModel: SignInViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        playAnimation()
        progressDialog = CustomProgressDialog(this)

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

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -32f, 32f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val text = ObjectAnimator.ofFloat(binding.textView, View.ALPHA, 1f).setDuration(500)
        val email = ObjectAnimator.ofFloat(binding.tilEmail, View.ALPHA, 1f).setDuration(500)
        val password = ObjectAnimator.ofFloat(binding.tilPassword, View.ALPHA, 1f).setDuration(500)
        val signIn = ObjectAnimator.ofFloat(binding.btnSignIn, View.ALPHA, 1f).setDuration(500)
        val signUp =
            ObjectAnimator.ofFloat(binding.tvSignInToSignUp, View.ALPHA, 1f).setDuration(500)

        val together = AnimatorSet().apply {
            playTogether(signIn, signUp)
        }

        AnimatorSet().apply {
            playSequentially(text, email, password, together)
            start()
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

        setupSignInViewModel()
        return true
    }

    private fun setupSignInViewModel() {
        signInViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UsersPreference.getInstance(dataStore))
        )[SignInViewModel::class.java]

        signInViewModel.setSignIn(email, password)
        signInViewModel.getSignInResponse().observe(this) { signInResponse ->
            if (signInResponse != null) {
                val intent = Intent(applicationContext, StoryActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        signInViewModel.getLoginResult().observe(this) { loginResult ->
            if (loginResult != null) {
                signInViewModel.saveSignIn(
                    UserModel(
                        true,
                        loginResult.name,
                        loginResult.token,
                        loginResult.userId
                    )
                )
            }
        }

        signInViewModel.getIsProgress().observe(this) { showProgress(it) }
        signInViewModel.getMessage().observe(this) { message ->
            Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun showProgress(state: Boolean) {
        if (state) {
            progressDialog.showProgressDialog()
        } else {
            progressDialog.dismissProgressDialog()
        }
    }
}