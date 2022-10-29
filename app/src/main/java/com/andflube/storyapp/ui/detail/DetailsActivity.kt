package com.andflube.storyapp.ui.detail

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import com.andflube.storyapp.R
import com.andflube.storyapp.databinding.ActivityDetailsBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding

    companion object {
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_CREATED = "extra_created"
        const val EXTRA_DESC = "extra_description"
        const val EXTRA_IMAGE = "extra_image"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupUI()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupUI() {
        val extras = intent.extras
        if (extras != null) {
            binding.apply {
                tvDetailName.text = extras.getString(EXTRA_NAME) ?: applicationContext.getString(R.string.not_name)
                tvDetailCreated.text = extras.getString(EXTRA_CREATED) ?: applicationContext.getString(R.string.not_publish)
                tvDetailDescription.text = extras.getString(EXTRA_DESC) ?: applicationContext.getString(R.string.not_description)

                Glide.with(this@DetailsActivity)
                    .load(extras.getString(EXTRA_IMAGE))
                    .apply(RequestOptions.placeholderOf(R.drawable.ic_loading)
                        .error(R.drawable.ic_error))
                    .into(ivDetailPhoto)
            }
        }
    }

}