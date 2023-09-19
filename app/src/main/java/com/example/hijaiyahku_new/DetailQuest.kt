package com.example.hijaiyahku_new

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hijaiyahku_new.databinding.ActivityDetailQuestBinding
import com.example.hijaiyahku_new.databinding.ActivityMainBinding
import com.example.hijaiyahku_new.fragment.HintFragment

class DetailQuest : AppCompatActivity() {
    lateinit var binding: ActivityDetailQuestBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivityDetailQuestBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainer,HintFragment())
        fragmentTransaction.commit()
    }
}