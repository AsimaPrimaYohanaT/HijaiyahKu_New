package com.example.hijaiyahku_new

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.widget.LinearLayoutCompat

class ChooseQuest : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_quest)
        val btnPisah = findViewById<LinearLayoutCompat>(R.id.btnPisah)
        val btnSambung = findViewById<LinearLayoutCompat>(R.id.btnSambung)

        btnPisah.setOnClickListener {
            val intent = Intent(this, DaftarSoal::class.java)
            intent.putExtra("jenis", "pisah")
            startActivity(intent)
        }


        btnSambung.setOnClickListener {
            val intent = Intent(this, DaftarSoal::class.java)
            intent.putExtra("jenis", "sambung")
            startActivity(intent)
        }

    }
}