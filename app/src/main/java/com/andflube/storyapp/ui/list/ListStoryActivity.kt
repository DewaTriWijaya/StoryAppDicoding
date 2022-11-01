package com.andflube.storyapp.ui.list

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.andflube.storyapp.R
import com.andflube.storyapp.ViewModelFactory
import com.andflube.storyapp.databinding.ActivityListStoryBinding
import com.andflube.storyapp.model.UserPreference
import com.andflube.storyapp.paging.LoadingStateAdapter
import com.andflube.storyapp.ui.add.AddStoryActivity
import com.andflube.storyapp.ui.mapActivity.MapsActivity
import com.andflube.storyapp.ui.profile.ProfileActivity

class ListStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListStoryBinding
    private lateinit var listViewModel: ListStoryViewModel

    private lateinit var preference: UserPreference
    private var token: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preference = UserPreference(this)
        token = preference.token

        setupViewModel()
        recyclerview()
        setupAction()
        getAllStory()
    }

    private fun setupViewModel() {
        listViewModel = ViewModelProvider(
            this,
            ViewModelFactory.getInstance(this)
        )[ListStoryViewModel::class.java]
    }

    private fun recyclerview() {
        binding.recyclerviewStory.apply {
            layoutManager = GridLayoutManager(context, 2)
            setHasFixedSize(true)
        }
    }

    private fun setupAction() {
        binding.apply {
            newStory.setOnClickListener {
                startActivity(Intent(this@ListStoryActivity, AddStoryActivity::class.java))
            }
        }
    }

    private fun getAllStory() {
        val adapter = AdapterListStory()
        binding.recyclerviewStory.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        listViewModel.story.observe(this) {
            adapter.submitData(lifecycle, it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.profile -> {
                startActivity(Intent(this@ListStoryActivity, ProfileActivity::class.java))
                return true
            }
            R.id.language -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            }
            R.id.maps -> {
                startActivity(Intent(this@ListStoryActivity, MapsActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        getAllStory()
    }

}