package com.example.hijaiyahku_new

import android.animation.ObjectAnimator
import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hijaiyahku_new.databinding.ActivityChooseQuestBinding
import com.example.hijaiyahku_new.fragment.HintChooseQuest
import com.example.hijaiyahku_new.utils.TambahSoal

class ChooseQuest : AppCompatActivity() {
    private lateinit var binding: ActivityChooseQuestBinding
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
            finish()
        }

        binding.btnSambung.setOnClickListener {
            val toHurufSambung = Intent(this@ChooseQuest, DaftarSoal::class.java)
            toHurufSambung.putExtra("jenis", "sambung")
            startActivity(toHurufSambung)
        }


        binding.fabTambah.setOnClickListener {
            val toTambah = Intent(this@ChooseQuest, TambahSoal::class.java)
            startActivity(toTambah)
        }
        binding.btnBack.setOnClickListener {
           super.onBackPressed()
        }


    }

    override fun onBackPressed() {
        super.onBackPressed()
    }


    fun playAnimation(){
        val animator = ObjectAnimator.ofFloat(findViewById(R.id.girl), "rotation", 0f, 20f, -20f)
        animator.repeatCount = ObjectAnimator.INFINITE
        animator.repeatMode = ObjectAnimator.REVERSE
        animator.duration = 2500L
        animator.start()
    }
}