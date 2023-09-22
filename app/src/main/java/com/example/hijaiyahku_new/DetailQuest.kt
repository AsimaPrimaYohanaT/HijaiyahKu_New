package com.example.hijaiyahku_new

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.hijaiyahku_new.data.Soal
import com.example.hijaiyahku_new.databinding.ActivityDetailQuestBinding
import com.example.hijaiyahku_new.databinding.ActivityMainBinding
import com.example.hijaiyahku_new.fragment.HintFragment
import com.google.android.material.appbar.CollapsingToolbarLayout

class DetailQuest : AppCompatActivity() {

    private lateinit var viewModel: DetailQuestViewModel
    lateinit var binding: ActivityDetailQuestBinding
    private lateinit var selectedSoal: Soal
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivityDetailQuestBinding.inflate(layoutInflater)

        setContentView(binding.root)
<<<<<<< HEAD

        val soalId = intent.getIntExtra("SOAL", 0)

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory).get(DetailQuestViewModel::class.java)

        viewModel.start(soalId)
        viewModel.soal.observe(this) { soal ->
            if (soal != null) {
                selectedSoal = soal
                binding.tvSoal.text = soal.soal
            }
        }

        val customDialog = HintFragment()
        customDialog.show(supportFragmentManager, "CustomDialog")
=======
//
//        val fragmentManager = supportFragmentManager
//        val fragmentTransaction = fragmentManager.beginTransaction()
//        fragmentTransaction.replace(R.id.fragmentContainer,HintFragment())
//        fragmentTransaction.commit()
>>>>>>> a4d4599a4b72a4015759cafe6c9ff1ba05d70903
    }
}