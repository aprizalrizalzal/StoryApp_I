package me.zal.rizal.aprizal.storyapp.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import me.zal.rizal.aprizal.storyapp.R
import me.zal.rizal.aprizal.storyapp.adapter.ListStoryAdapter
import me.zal.rizal.aprizal.storyapp.addition.CustomProgressDialog
import me.zal.rizal.aprizal.storyapp.databinding.ActivityStoryBinding
import me.zal.rizal.aprizal.storyapp.main.auth.SignInActivity
import me.zal.rizal.aprizal.storyapp.model.story.ListStoryItem
import me.zal.rizal.aprizal.storyapp.view.ViewModelFactory
import me.zal.rizal.aprizal.storyapp.view.model.StoryViewModel

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class StoryActivity : AppCompatActivity() {
    private lateinit var storyViewModel: StoryViewModel
    private lateinit var progressDialog: CustomProgressDialog
    private lateinit var binding: ActivityStoryBinding
    private lateinit var listStoryAdapter: ListStoryAdapter

    companion object {
        private const val TAG = "StoryActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        progressDialog = CustomProgressDialog(this)

        binding.fab.setOnClickListener {
            val intent = Intent(applicationContext, AddStoryActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_language) {
            val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(intent)
        }

        if (item.itemId == R.id.action_sign_out) {
            storyViewModel.signOut()

            val intent = Intent(applicationContext, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }
        return true
    }

    private fun setupViewModel() {
        storyViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UsersPreference.getInstance(dataStore))
        )[StoryViewModel::class.java]

        storyViewModel.getIsProgress().observe(this) {
            showProgress(it)
        }
        storyViewModel.getUser().observe(this) { user ->
            when {
                user.isLogin && user.token.isNotEmpty() -> {
                    Log.d(TAG, "login: " + user.isLogin + " and token: " + user.token)
                    val token = user.token
                    setStories(token)
                }
                else -> {
                    Log.w(TAG, "login: " + user.isLogin + " and token: " + user.token)
                    val intent = Intent(applicationContext, SignInActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }

    private fun setStories(token: String?) {
        storyViewModel.setStories("Bearer $token")
        storyViewModel.getStories().observe(
            this
        ) { listStoryItem ->
            if (listStoryItem != null) {
                listStoryAdapter = ListStoryAdapter(ArrayList(listStoryItem))
                binding.rvStories.layoutManager = LinearLayoutManager(applicationContext)
                binding.rvStories.adapter = listStoryAdapter
                binding.rvStories.hasFixedSize()
                listStoryAdapter.setStories(listStoryItem)
                listStoryAdapter.setOnItemClickCallback(object :
                    ListStoryAdapter.OnItemClickCallback {
                    override fun onItemClicked(
                        viewHolder: ListStoryAdapter.ViewHolder,
                        listStoryItem: ListStoryItem
                    ) {
                        showSelectedStory(viewHolder, listStoryItem)
                    }
                })
            }
        }
    }

    private fun showSelectedStory(
        viewHolder: ListStoryAdapter.ViewHolder,
        listStoryItem: ListStoryItem
    ) {

        val optionsCompat: ActivityOptionsCompat =
            ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                Pair(viewHolder.imgCardStory, "card_story"),
                Pair(viewHolder.tvName, "name"),
                Pair(viewHolder.tvDescription, "description"),
            )

        val intent = Intent(applicationContext, DetailActivity::class.java)
        intent.putExtra("extra_list_story_item", listStoryItem)
        startActivity(intent, optionsCompat.toBundle())
    }

    private fun showProgress(state: Boolean) {
        if (state) {
            progressDialog.showProgressDialog()
        } else {
            progressDialog.runCatching { dismissProgressDialog() }
        }
    }

}