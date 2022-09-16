package me.zal.rizal.aprizal.storyapp.main

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import me.zal.rizal.aprizal.storyapp.databinding.ActivityDetailBinding
import me.zal.rizal.aprizal.storyapp.model.story.ListStoryItem

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val storyItem = intent.getParcelableExtra<ListStoryItem>("extra_list_story_item")

        if (storyItem != null) {
            Glide.with(applicationContext)
                .load(storyItem.photoUrl)
                .apply(RequestOptions().override(512, 512))
                .into(binding.imgCardStory)
            binding.tvName.text = storyItem.name
            binding.tvDescription.text = storyItem.description
            Toast.makeText(applicationContext, storyItem.name, Toast.LENGTH_SHORT).show()
        }
    }
}