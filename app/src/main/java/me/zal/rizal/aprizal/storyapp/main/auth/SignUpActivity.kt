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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import me.zal.rizal.aprizal.storyapp.R
import me.zal.rizal.aprizal.storyapp.addition.CustomProgressDialog
import me.zal.rizal.aprizal.storyapp.databinding.ActivitySignUpBinding
import me.zal.rizal.aprizal.storyapp.main.UsersPreference
import me.zal.rizal.aprizal.storyapp.view.ViewModelFactory
import me.zal.rizal.aprizal.storyapp.view.model.SignupViewModel

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var progressDialog: CustomProgressDialog
    private lateinit var name: String
    private lateinit var email: String
    private lateinit var password: String
    private var validateField: Boolean = false
    private lateinit var signupViewModel: SignupViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        playAnimation()
        progressDialog = CustomProgressDialog(this)

        binding.btnSignUp.setOnClickListener {
            name = binding.tietName.text.toString()
            email = binding.tietEmail.text.toString()
            password = binding.tietPassword.text.toString()

            validateField = isEmptyField()

        }

        binding.tvSignUpToSignIn.setOnClickListener {
            val intent = Intent(applicationContext, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -32f, 32f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val text = ObjectAnimator.ofFloat(binding.textView, View.ALPHA, 1f).setDuration(500)
        val name = ObjectAnimator.ofFloat(binding.tilName, View.ALPHA, 1f).setDuration(500)
        val email = ObjectAnimator.ofFloat(binding.tilEmail, View.ALPHA, 1f).setDuration(500)
        val password = ObjectAnimator.ofFloat(binding.tilPassword, View.ALPHA, 1f).setDuration(500)
        val signUp = ObjectAnimator.ofFloat(binding.btnSignUp, View.ALPHA, 1f).setDuration(500)
        val signIn =
            ObjectAnimator.ofFloat(binding.tvSignUpToSignIn, View.ALPHA, 1f).setDuration(500)

        val together = AnimatorSet().apply {
            playTogether(signIn, signUp)
        }

        AnimatorSet().apply {
            playSequentially(text, name, email, password, together)
            start()
        }
    }

    private fun isEmptyField(): Boolean {
        if (name.isEmpty()) {
            binding.tilName.error = getString(R.string.name_required)
            return false
        } else {
            binding.tilName.isErrorEnabled = false
        }

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

        setupViewModel()
        return true
    }

    private fun setupViewModel() {
        signupViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UsersPreference.getInstance(dataStore))
        )[SignupViewModel::class.java]

        signupViewModel.setSignUp(name, email, password)
        signupViewModel.getSignUpModel().observe(this) { signUp ->
            if (signUp != null) {
                val intent = Intent(applicationContext, SignInActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        signupViewModel.getIsProgress().observe(this) { showProgress(it) }
        signupViewModel.getIsToast().observe(this) { message ->
            Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun showProgress(state: Boolean) {
        if (state) {
            progressDialog.showProgressDialog()
        } else {
            progressDialog.runCatching { dismissProgressDialog() }
        }
    }
}