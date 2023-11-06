package com.example.hijaiyahku_new

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.hijaiyahku_new.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity()  {

    private lateinit var binding: ActivityMainBinding
    private lateinit var backgroundServiceMusicThread: Thread
    private lateinit var intent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding  = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        backgroundServiceMusicThread = Thread {
             intent = Intent(this@MainActivity, BackgroundSoundService::class.java)
            startService(intent)
        }
        PlayBackgroundSound(null)
        playAnimation()

        binding.btnHome.setOnClickListener {
            val mulai = Intent(this@MainActivity, ChooseQuest::class.java)
            startActivity(mulai)
        }
        binding.mscBtn.setOnClickListener {
            if (binding.mscIcnActive.visibility == View.GONE) {
                binding.mscIcnActive.visibility = View.VISIBLE
                binding.mscIcnOff.visibility = View.GONE
                startService(intent)
            }else {
                binding.mscIcnActive.visibility = View.GONE
                binding.mscIcnOff.visibility = View.VISIBLE
                stopService(intent)
            }
        }
    }
    private fun playAnimation() {
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
        ObjectAnimator.ofFloat(binding.asset51, View.TRANSLATION_X, -40f, 40f).apply {
            duration = 2000
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
    private fun PlayBackgroundSound(view: View?) {
        backgroundServiceMusicThread.start()

    }

}