package com.andflube.storyapp.ui

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowInsets
import android.view.WindowManager
import com.andflube.storyapp.databinding.ActivityFirstBinding
import com.andflube.storyapp.model.UserPreference
import com.andflube.storyapp.ui.list.ListStoryActivity
import com.andflube.storyapp.ui.welcome.WelcomeActivity

class FirstActivity : AppCompatActivity() {

    private lateinit var preference: UserPreference
    private lateinit var binding: ActivityFirstBinding
    private val delayNumber = 2000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFirstBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()

        Handler(Looper.getMainLooper()).postDelayed({
            run {
                setupPreference()
                finish()
            }
        }, delayNumber.toLong())
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

    private fun setupPreference() {
        preference = UserPreference(this)

        val pref = preference.isLogin
        if (pref) {
            startActivity(Intent(this, ListStoryActivity::class.java))
        } else {
            startActivity(Intent(this, WelcomeActivity::class.java))
            finish()
        }
    }
}
