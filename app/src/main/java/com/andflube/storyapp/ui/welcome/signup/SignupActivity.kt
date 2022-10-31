package com.andflube.storyapp.ui.welcome.signup

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
import com.andflube.storyapp.databinding.ActivitySignupBinding
import com.andflube.storyapp.network.ResultResponse
import com.andflube.storyapp.network.response.RegisterResponse
import com.andflube.storyapp.ui.welcome.login.LoginActivity

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var signupViewModel: SignupViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()
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

    private fun setupViewModel() {
        signupViewModel = ViewModelProvider(
            this,
            ViewModelFactory.getInstance(this)
        )[SignupViewModel::class.java]
    }

    private fun setupAction() {
        binding.btnSignup.setOnClickListener {
            val name = binding.edRegisterName.text.toString()
            val email = binding.edRegisterEmail.text.toString()
            val password = binding.edRegisterPassword.text.toString()

            val request = RegisterResponse(
                name, email, password
            )
            registerUser(request)
        }
    }

    private fun registerUser(newUser: RegisterResponse) {
        signupViewModel.getRegisterUser(newUser).observe(this) { response ->
            when (response) {
                is ResultResponse.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is ResultResponse.Success -> {
                    binding.progressBar.visibility = View.GONE
                    AlertDialog.Builder(this).apply {
                        setTitle(context.getString(R.string.yes))
                        setMessage(context.getString(R.string.success_signup))
                        setPositiveButton(context.getString(R.string.next)) { _, _ ->
                            val intent = Intent(context, LoginActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                            finish()
                        }
                        create()
                        show()
                    }
                }
                is ResultResponse.Error -> {
                    binding.progressBar.visibility = View.GONE
                    AlertDialog.Builder(this).apply {
                        setTitle(context.getString(R.string.no))
                        setMessage(context.getString(R.string.wrong_signup))
                        setNegativeButton(getString(R.string.oke), null)
                        create()
                        show()
                    }
                }
                else -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setMyButtonEnable() {
        binding.edRegisterName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val result = binding.edRegisterName.text
                binding.btnSignup.isEnabled = result != null && result.toString().isNotEmpty()
            }

            override fun afterTextChanged(p0: Editable?) {
                //
            }
        })

        binding.edRegisterEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val result = binding.edRegisterEmail.text
                binding.btnSignup.isEnabled = result != null && result.toString().isNotEmpty()
            }

            override fun afterTextChanged(p0: Editable?) {
                //
            }
        })

        binding.edRegisterPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val result = binding.edRegisterPassword.text
                binding.btnSignup.isEnabled = result != null && result.toString().isNotEmpty()
            }

            override fun afterTextChanged(p0: Editable?) {
                //
            }
        })
    }
}