package com.example.hijaiyahku_new

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import com.example.hijaiyahku_new.databinding.ActivityDetailQuestBinding
import com.example.hijaiyahku_new.databinding.ActivitySplashBinding

class Splash : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        playAnimation()
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this@Splash, MainActivity::class.java))
            finish()
        },3000)
    }


    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView5, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 3000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

    }
}