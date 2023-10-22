package com.example.hijaiyahku_new

import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hijaiyahku_new.databinding.ActivityDaftarSoalBinding
import com.example.hijaiyahku_new.fragment.HintFragment
import com.example.hijaiyahku_new.fragment.SuccessFragment
import com.example.hijaiyahku_new.utils.SoalSortType

class DaftarSoal : AppCompatActivity() {

    private lateinit var recycler: RecyclerView
    private lateinit var viewModel: DaftarSoalViewModel
    private lateinit var binding: ActivityDaftarSoalBinding
    private val hintDialog = HintFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDaftarSoalBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        recycler = binding.rvSoal
        recycler.layoutManager = GridLayoutManager(this, 2)

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory).get(DaftarSoalViewModel::class.java)

        val jenis = intent.getStringExtra("jenis")

        binding.info.setOnClickListener {
            hintDialog.show(supportFragmentManager, "CustomDialog")
        }

        if (jenis == "pisah") {
            viewModel.filter(SoalSortType.TYPE_1)
        } else if (jenis == "sambung") {
            viewModel.filter(SoalSortType.TYPE_2)
        }

        val adapter = DaftarSoalAdapter(viewModel) {  soal,nextSoal ->
            val detailIntent = Intent(this@DaftarSoal, DetailQuest::class.java)
            detailIntent.putExtra("SOAL", soal.id)

            if(nextSoal != null){
                detailIntent.putExtra("NEXT_SOAL",nextSoal.id)
            }

            startActivity(detailIntent)
        }

        viewModel.soal.observe(this) { pagedList ->
            adapter.submitList(pagedList)
            recycler.adapter = adapter
        }
    }

}
