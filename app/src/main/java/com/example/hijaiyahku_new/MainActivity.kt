package com.example.hijaiyahku_new

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hijaiyahku_new.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnMulai.setOnClickListener {
            val mulai = Intent(this@MainActivity, ChooseQuest::class.java)
            startActivity(mulai)
        }
    }
}