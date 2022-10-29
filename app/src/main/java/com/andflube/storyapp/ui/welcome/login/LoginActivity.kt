package com.andflube.storyapp.ui.welcome.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.andflube.storyapp.R
import com.andflube.storyapp.ViewModelFactory
import com.andflube.storyapp.databinding.ActivityLoginBinding
import com.andflube.storyapp.model.UserPreference
import com.andflube.storyapp.model.UserPreference.Companion.EMAIL_KEY
import com.andflube.storyapp.model.UserPreference.Companion.IS_LOGIN
import com.andflube.storyapp.model.UserPreference.Companion.NAME_KEY
import com.andflube.storyapp.model.UserPreference.Companion.TOKEN_KEY
import com.andflube.storyapp.model.UserPreference.Companion.USER_ID_KEY
import com.andflube.storyapp.network.ResultResponse
import com.andflube.storyapp.network.response.LoginResponse
import com.andflube.storyapp.ui.list.ListStoryActivity


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var pref: UserPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pref = UserPreference(this)

        setupView()
        playAnimation()
        setupAction()
        setMyButtonEnable()
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

    private fun playAnimation() {
        val title = ObjectAnimator.ofFloat(binding.tvTitle, View.ALPHA, 1f).setDuration(500)
        val emailText = ObjectAnimator.ofFloat(binding.tvEmail, View.ALPHA, 1f).setDuration(500)
        val emailEdit =
            ObjectAnimator.ofFloat(binding.edLoginEmail, View.ALPHA, 1f).setDuration(500)
        val passwordText =
            ObjectAnimator.ofFloat(binding.tvPassword, View.ALPHA, 1f).setDuration(500)
        val passwordEdit =
            ObjectAnimator.ofFloat(binding.edLoginPassword, View.ALPHA, 1f).setDuration(500)
        val login = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(title, emailText, emailEdit, passwordText, passwordEdit, login)
            start()
        }
    }

    private fun setupAction() {
        binding.btnLogin.setOnClickListener {
            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()

            pref = UserPreference(this)

            val request = LoginResponse(
                email, password
            )
            loginUser(request, email)
        }
    }

    private fun loginUser(loginResponse: LoginResponse, email: String) {
        loginViewModel = ViewModelProvider(
            this,
            ViewModelFactory.getInstance(this)
        )[LoginViewModel::class.java]

        loginViewModel.loginUser(loginResponse).observe(this) {
            when (it) {
                is ResultResponse.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is ResultResponse.Success -> {
                    try {
                        binding.progressBar.visibility = View.GONE
                        val userData = it.data.loginResult
                        pref.apply {
                            setStringPreference(USER_ID_KEY, userData.userId)
                            setStringPreference(TOKEN_KEY, userData.token)
                            setStringPreference(NAME_KEY, userData.name)
                            setStringPreference(EMAIL_KEY, email)
                            setBooleanPreference(IS_LOGIN, true)

                        }
                    } finally {
                        AlertDialog.Builder(this).apply {
                            setTitle(context.getString(R.string.yes))
                            setMessage(context.getString(R.string.success_login))
                            setPositiveButton(context.getString(R.string.next)) { _, _ ->
                                val intent = Intent(context, ListStoryActivity::class.java)
                                intent.flags =
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                startActivity(intent)
                                finish()
                            }
                            create()
                            show()
                        }
                    }
                }
                is ResultResponse.Error -> {
                    binding.progressBar.visibility = View.GONE
                    AlertDialog.Builder(this).apply {
                        setTitle(context.getString(R.string.no))
                        setMessage(context.getString(R.string.wrong_login))
                        setNegativeButton(getString(R.string.oke), null)
                        create()
                        show()
                    }
                } else -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                }

            }
        }
    }

    private fun setMyButtonEnable() {
        binding.edLoginPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val result = binding.edLoginPassword.text
                binding.btnLogin.isEnabled = result != null && result.toString().isNotEmpty()
            }

            override fun afterTextChanged(p0: Editable?) {
                //
            }
        })

        binding.edLoginEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val result = binding.edLoginEmail.text
                binding.btnLogin.isEnabled = result != null && result.toString().isNotEmpty()
            }

            override fun afterTextChanged(p0: Editable?) {
                //
            }
        })
    }
}