package com.example.hijaiyahku_new


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
        binding  = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        PlayBackgroundSound(null)
        playAnimation()
        binding.btnHome.setOnClickListener {
            val mulai = Intent(this@MainActivity, ChooseQuest::class.java)
            startActivity(mulai)
        }

    }

    fun playAnimation(){

        val animator = ObjectAnimator.ofFloat(findViewById(R.id.btn_home), "scaleX", 1f, 1.1f)
        val animator1 = ObjectAnimator.ofFloat(findViewById(R.id.btn_home), "scaleY", 1f, 1.1f)
        animator.repeatCount = ObjectAnimator.INFINITE
        animator.repeatMode = ObjectAnimator.REVERSE
        animator.duration = 1000L
        animator.start()
        animator1.repeatCount = ObjectAnimator.INFINITE

        animator1.repeatMode = ObjectAnimator.REVERSE
        animator1.duration = 1000L
        animator1.start()
    }
    fun PlayBackgroundSound(view: View?) {
        val backgroundThread = Thread {
            val intent = Intent(this@MainActivity, BackgroundSoundService::class.java)
            startService(intent)
        }
        backgroundThread.start()
    }

}