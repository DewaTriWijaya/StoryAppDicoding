package com.andflube.storyapp.ui.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.andflube.storyapp.R
import com.andflube.storyapp.databinding.ActivityProfileBinding
import com.andflube.storyapp.model.UserPreference
import com.andflube.storyapp.ui.welcome.WelcomeActivity

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var preference: UserPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = getString(R.string.menu_profile)

        setupUI()
    }

    private fun setupUI() {
        preference = UserPreference(this)
        val pref = preference.name

        binding.apply {
            profileName.text = pref

            btnLogout.setOnClickListener {
                AlertDialog.Builder(this@ProfileActivity).apply {
                    setTitle(getString(R.string.logout))
                    setMessage(getString(R.string.show_logout))
                    setPositiveButton(context.getString(R.string.next)) { _, _ ->
                        preference.clearPreferences()
                        val intent = Intent(context, WelcomeActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                        finish()
                    }
                    setNegativeButton(getString(R.string.not_logout), null)
                    create()
                    show()
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}