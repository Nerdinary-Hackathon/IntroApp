package com.example.introapp.presentation.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import coil.load
import com.example.introapp.R
import com.example.introapp.databinding.ActivitySplashBinding
import com.example.introapp.presentation.viewmodel.OnBoardingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    private val onBoardingViewModel: OnBoardingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.splashMain)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        lifecycleScope.launch {
            onBoardingViewModel.getSavedUserId().collect { userId ->
                Timber.d("## [userId] userId : $userId")
                delay(3000L)
//                startActivity(
//                    Intent(this@SplashActivity, MainActivity::class.java)
//                )
                if (userId != null) {
                    startActivity(
                        Intent(this@SplashActivity, HomeActivity::class.java)
                    )
                } else {
                    startActivity(
                        Intent(this@SplashActivity, MainActivity::class.java)
                    )
                }

                finish()
            }
        }

        binding.ivSplash.load(R.drawable.logo)
    }
}