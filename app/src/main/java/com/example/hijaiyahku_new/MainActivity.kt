package com.example.hijaiyahku_new

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.hijaiyahku_new.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity()  {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        PlayBackgroundSound(null)
        playAnimation()
//        PlayBackgroundSound(null)
//        val animator = ObjectAnimator.ofFloat(findViewById(R.id.btn_home), "scaleX", 1f, 3f)
//        val animator1 = ObjectAnimator.ofFloat(findViewById(R.id.btn_home), "scaleY", 1f, 3f)
//        animator.repeatCount = ObjectAnimator.INFINITE
//        animator.repeatMode = ObjectAnimator.REVERSE
//        animator.duration = 1000L
//        animator.start()
//        animator1.repeatCount = ObjectAnimator.INFINITE
//        animator1.repeatMode = ObjectAnimator.REVERSE
//        animator1.duration = 1000L
//        animator1.start()
        binding.btnHome.setOnClickListener {
            val mulai = Intent(this@MainActivity, ChooseQuest::class.java)
            startActivity(mulai)
        }

    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.asset51, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 3000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val buttonHome = ObjectAnimator.ofFloat(binding.btnHome, View.ALPHA, 1f).setDuration(100)
        val girl = ObjectAnimator.ofFloat(binding.asset33, View.ALPHA, 1f).setDuration(100)
        val boy = ObjectAnimator.ofFloat(binding.asset51, View.ALPHA, 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(
                buttonHome,
                girl,
                boy)
            startDelay = 500
        }.start()
    }
    fun PlayBackgroundSound(view: View?) {
        val intent = Intent(this@MainActivity, BackgroundSoundService::class.java)
        startService(intent)
    }

}