package com.example.hijaiyahku_new

import android.animation.ObjectAnimator
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hijaiyahku_new.databinding.ActivityChooseQuestBinding
import com.example.hijaiyahku_new.fragment.HintChooseQuest

class ChooseQuest : AppCompatActivity() {
    private lateinit var binding: ActivityChooseQuestBinding


    override fun onPause() {
        super.onPause()
        if(isBackgroundServiceRunning(BackgroundSoundService::class.java)) {
            Thread {
                intent = Intent(this@ChooseQuest, BackgroundSoundService::class.java)
                stopService(intent)


            }.start()
        }
    }

    override fun onStart() {
        super.onStart()
        if(isBackgroundServiceRunning(BackgroundSoundService::class.java)) {
            Thread {
                intent = Intent(this@ChooseQuest, BackgroundSoundService::class.java)
                startService(intent)


            }.start()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChooseQuestBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        playAnimation()

        binding.btnPetunjuk.setOnClickListener {
            val chooseQuestFragment = HintChooseQuest()
            chooseQuestFragment.show(supportFragmentManager,"ChooseQuestFragment")
        }

        binding.btnPisah.setOnClickListener {
            val toHurufPisah = Intent(this@ChooseQuest, DaftarSoal::class.java)
            toHurufPisah.putExtra("jenis", "pisah")
            startActivity(toHurufPisah)
        }

        binding.btnSambung.setOnClickListener {
            val toHurufSambung = Intent(this@ChooseQuest, DaftarSoal::class.java)
            toHurufSambung.putExtra("jenis", "sambung")
            startActivity(toHurufSambung)
        }


//        binding.fabTambah.setOnClickListener {
//            val toTambah = Intent(this@ChooseQuest, TambahSoal::class.java)
//            startActivity(toTambah)
//        }
        binding.btnBack.setOnClickListener {
            val back = Intent(this@ChooseQuest, MainActivity::class.java)
            startActivity(back)
        }


    }

    override fun onBackPressed() {
        super.onBackPressed()
        val back = Intent(this@ChooseQuest, MainActivity::class.java)
        startActivity(back)

    }
    private fun isBackgroundServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }

    fun playAnimation(){
        val animator = ObjectAnimator.ofFloat(findViewById(R.id.girl), "rotation", 0f, 20f, -20f)
        animator.repeatCount = ObjectAnimator.INFINITE
        animator.repeatMode = ObjectAnimator.REVERSE
        animator.duration = 2500L
        animator.start()
    }
}