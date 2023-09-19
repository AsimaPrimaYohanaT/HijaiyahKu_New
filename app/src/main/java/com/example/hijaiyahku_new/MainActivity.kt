package com.example.hijaiyahku_new

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.hijaiyahku_new.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val btnHome = findViewById<View>(R.id.btnHome)
        btnHome.setOnClickListener {
            Log.d("tag","klik")
            val intent = Intent(this, ChooseQuest::class.java)
            startActivity(intent)

        }

    }
}