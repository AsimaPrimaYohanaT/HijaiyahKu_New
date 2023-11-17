package com.example.hijaiyahku_new

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.hijaiyahku_new.R
import com.example.hijaiyahku_new.databinding.ActivityTambahSoalBinding

class TambahSoal : AppCompatActivity() {

    private lateinit var binding: ActivityTambahSoalBinding
    private lateinit var tambahSoalViewModel: TambahSoalViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityTambahSoalBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val items= listOf("ALIF","BA")

        val adapter = ArrayAdapter(this, R.layout.dropdown_items,items)

        binding.autoComplete.setAdapter(adapter)

        binding.autoComplete.onItemClickListener = AdapterView.OnItemClickListener{
            adapterView,view,i,l ->

            val itemSelected = adapterView.getItemAtPosition(i)
            Toast.makeText(this,"Item: $itemSelected", Toast.LENGTH_SHORT).show()
        }


    }
}