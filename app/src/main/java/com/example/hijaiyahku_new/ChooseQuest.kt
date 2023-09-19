package com.example.hijaiyahku_new

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hijaiyahku_new.databinding.ActivityChooseQuestBinding

class ChooseQuest : AppCompatActivity() {

    private lateinit var binding: ActivityChooseQuestBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChooseQuestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnPisah.setOnClickListener {
            val toHurufPisah = Intent(this@ChooseQuest, DaftarSoal::class.java)
            intent.putExtra("jenis", "pisah")
            startActivity(toHurufPisah)
        }

        binding.btnSambung.setOnClickListener {
            val toHurufSambung = Intent(this@ChooseQuest, DaftarSoal::class.java)
            intent.putExtra("jenis", "sambung")
            startActivity(toHurufSambung)
        }
    }
}