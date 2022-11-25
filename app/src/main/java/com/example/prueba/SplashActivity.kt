package com.example.prueba

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import com.example.prueba.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        this.supportActionBar?.hide()

        startTimer()
    }

    private fun startTimer() {
        binding.ivLogo.animate().alpha(0f).translationY(-300f).scaleX(0f).scaleY(0f).setDuration(0)
            .withEndAction {
                binding.ivLogo.animate().alpha(1f).translationY(0f).scaleX(1f).scaleY(1f)
                    .setDuration(1800).withEndAction {
                    val i = Intent(applicationContext, LoginActivity::class.java)
                    startActivity(i)
                    finish()
                }
            }
    }
}
