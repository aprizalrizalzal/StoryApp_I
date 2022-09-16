package me.zal.rizal.aprizal.storyapp.main

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import me.zal.rizal.aprizal.storyapp.R
import me.zal.rizal.aprizal.storyapp.addition.CustomProgressDialog
import me.zal.rizal.aprizal.storyapp.addition.createCustomTempFile
import me.zal.rizal.aprizal.storyapp.addition.reduceFileImage
import me.zal.rizal.aprizal.storyapp.addition.uriToFile
import me.zal.rizal.aprizal.storyapp.databinding.ActivityAddStoryBinding
import me.zal.rizal.aprizal.storyapp.view.ViewModelFactory
import me.zal.rizal.aprizal.storyapp.view.model.AddStoryViewModel
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class AddStoryActivity : AppCompatActivity() {

    private var getFile: File? = null
    private lateinit var setDescriptions: String
    private var validateField: Boolean? = false
    private lateinit var addStoryViewModel: AddStoryViewModel
    private lateinit var binding: ActivityAddStoryBinding
    private lateinit var progressDialog: CustomProgressDialog
    private lateinit var currentPhotoPath: String

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    getString(R.string.not_getting_permission),
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        progressDialog = CustomProgressDialog(this)
        binding.btnCamera.setOnClickListener {
            startTakePhoto()
        }

        binding.btnGallery.setOnClickListener {
            startGallery()
        }


        binding.btnUpload.setOnClickListener {
            setDescriptions = binding.tietDescription.text.toString()
            validateField = isEmptyField()
        }

    }

    private fun isEmptyField(): Boolean {
        if (getFile == null) {
            Toast.makeText(
                applicationContext,
                getString(R.string.insert_the_image_first),
                Toast.LENGTH_SHORT
            ).show()
            return false
        } else if (setDescriptions.isEmpty()) {
            binding.tilDescription.error = getString(R.string.image_description_cannot_be_empty)
            return false
        } else {
            binding.tilDescription.isErrorEnabled = false
            uploadImage()
        }

        return true
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val setFile = File(currentPhotoPath)
            getFile = setFile

            val result = BitmapFactory.decodeFile(setFile.path)
            binding.imgCardStory.setImageBitmap(result)

        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val setFile = uriToFile(selectedImg, applicationContext)
            getFile = setFile

            binding.imgCardStory.setImageURI(selectedImg)
        }
    }

    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)
        createCustomTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                applicationContext,
                "me.zal.rizal.aprizal.storyapp",
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private fun uploadImage() {
        val setFile = reduceFileImage(getFile as File)

        val description = setDescriptions.toRequestBody("text/plain".toMediaType())
        val requestImageFile = setFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "photo",
            setFile.name,
            requestImageFile
        )

        addStoryViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UsersPreference.getInstance(dataStore))
        )[AddStoryViewModel::class.java]

        addStoryViewModel.getIsProgress().observe(this) { showProgress(it) }
        addStoryViewModel.getUser().observe(this) { users ->
            val token = users.token
            addStoryViewModel.setAddStory(
                " Bearer $token",
                imageMultipart,
                description
            )
        }

        addStoryViewModel.getIsToast().observe(this) { message ->
            Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
        }

    }

    private fun showProgress(it: Boolean) {
        if (it) {
            progressDialog.showProgressDialog()
        } else {
            progressDialog.runCatching { dismissProgressDialog() }
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(Intent(applicationContext, StoriesActivity::class.java))
            finish()
        }
    }

}