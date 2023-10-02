package com.example.hijaiyahku_new


import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.hijaiyahku_new.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        PlayBackgroundSound(null)
//       val view = findViewById<View>(R.id.layer2)
//
//val valueAnimator = ValueAnimator.ofFloat(0f, 30f, 0f,-30f)
//valueAnimator.repeatCount = ValueAnimator.INFINITE
//valueAnimator.duration = 5000
//valueAnimator.addUpdateListener { animator ->
//    // Update the animation state.
//    view.rotation = animator.animatedValue as Float
//}
//
//valueAnimator.start()
//        valueAnimator.removeAllUpdateListeners()
        binding.btnHome.setOnClickListener {
            val mulai = Intent(this@MainActivity, ChooseQuest::class.java)
            startActivity(mulai)
        }

    }
    fun PlayBackgroundSound(view: View?) {
        val intent = Intent(this@MainActivity, BackgroundSoundService::class.java)
        startService(intent)
    }

}