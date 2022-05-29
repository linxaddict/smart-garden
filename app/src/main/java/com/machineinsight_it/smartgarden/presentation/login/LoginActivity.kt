package com.machineinsight_it.smartgarden.presentation.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.machineinsight_it.smartgarden.R
import com.machineinsight_it.smartgarden.databinding.ActivityLoginBinding
import com.machineinsight_it.smartgarden.presentation.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ActivityLoginBinding>(
            this,
            R.layout.activity_login
        )
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        viewModel.userLoggedInEvent.observe(this, {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        })
    }
}
