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
import me.zal.rizal.aprizal.storyapp.adapter.ListStoriesAdapter
import me.zal.rizal.aprizal.storyapp.addition.CustomProgressDialog
import me.zal.rizal.aprizal.storyapp.databinding.ActivityStoriesBinding
import me.zal.rizal.aprizal.storyapp.main.auth.SignInActivity
import me.zal.rizal.aprizal.storyapp.model.story.ListStoryItem
import me.zal.rizal.aprizal.storyapp.view.ViewModelFactory
import me.zal.rizal.aprizal.storyapp.view.model.StoriesViewModel

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class StoriesActivity : AppCompatActivity() {

    private lateinit var storiesViewModel: StoriesViewModel
    private lateinit var progressDialog: CustomProgressDialog
    private lateinit var binding: ActivityStoriesBinding
    private lateinit var listStoriesAdapter: ListStoriesAdapter

    companion object {
        private const val TAG = "StoriesActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoriesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        progressDialog = CustomProgressDialog(this)

        binding.fab.setOnClickListener {
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(Intent(applicationContext, AddStoryActivity::class.java))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_language) {
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }

        if (item.itemId == R.id.action_sign_out) {
            storiesViewModel.signOut()

            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(Intent(applicationContext, SignInActivity::class.java))
            finish()
        }
        return true
    }

    private fun setupViewModel() {
        storiesViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UsersPreference.getInstance(dataStore))
        )[StoriesViewModel::class.java]

        storiesViewModel.getIsProgress().observe(this) { showProgress(it) }
        storiesViewModel.getUser().observe(this) { user ->
            when {
                user.isLogin && user.token.isNotEmpty() -> {
                    Log.d(TAG, "login: " + user.isLogin + " and token: " + user.token)
                    val token = user.token
                    setStories(token)
                }
                else -> {
                    Log.w(TAG, "login: " + user.isLogin + " and token: " + user.token)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(Intent(applicationContext, SignInActivity::class.java))
                    finish()
                }
            }
        }
    }

    private fun setStories(token: String?) {
        storiesViewModel.setAllStories("Bearer $token")
        storiesViewModel.getAllStories().observe(
            this
        ) { listStoryItem ->
            if (listStoryItem != null) {
                listStoriesAdapter = ListStoriesAdapter(ArrayList(listStoryItem))
                binding.rvStories.layoutManager = LinearLayoutManager(applicationContext)
                binding.rvStories.adapter = listStoriesAdapter
                binding.rvStories.hasFixedSize()
                listStoriesAdapter.setStories(listStoryItem)
                listStoriesAdapter.setOnItemClickCallback(object :
                    ListStoriesAdapter.OnItemClickCallback {
                    override fun onItemClicked(
                        viewHolder: ListStoriesAdapter.ViewHolder,
                        listStoryItem: ListStoryItem
                    ) {
                        showSelectedStories(viewHolder, listStoryItem)
                    }
                })
            }
        }
    }

    private fun showSelectedStories(
        viewHolder: ListStoriesAdapter.ViewHolder,
        listStoryItem: ListStoryItem
    ) {

        val optionsCompat: ActivityOptionsCompat =
            ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                Pair(viewHolder.imgCardStory, "card_story"),
                Pair(viewHolder.tvName, "name"),
                Pair(viewHolder.tvDescription, "description"),
            )

        intent.putExtra("extra_list_story_item", listStoryItem)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(Intent(applicationContext, DetailActivity::class.java), optionsCompat.toBundle())
    }

    private fun showProgress(state: Boolean) {
        if (state) {
            progressDialog.showProgressDialog()
        } else {
            progressDialog.runCatching { dismissProgressDialog() }
        }
    }

}