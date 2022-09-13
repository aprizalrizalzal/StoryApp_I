package me.zal.rizal.aprizal.storyapp.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import me.zal.rizal.aprizal.storyapp.databinding.ActivityAddStoryBinding
import me.zal.rizal.aprizal.storyapp.databinding.ActivityStoryBinding
import me.zal.rizal.aprizal.storyapp.view.model.AddStoryViewModel

class AddStoryActivity : AppCompatActivity() {

    private lateinit var addStoryViewModel: AddStoryViewModel
    private lateinit var binding : ActivityAddStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCamera.setOnClickListener {

        }

        binding.btnGallery.setOnClickListener {

        }

        binding.btnUpload.setOnClickListener {

        }

//        playAnimation()

    }
}