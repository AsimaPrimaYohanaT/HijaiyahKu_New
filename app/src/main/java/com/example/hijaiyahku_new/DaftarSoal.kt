package com.example.hijaiyahku_new

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.hijaiyahku_new.databinding.ActivityChooseQuestBinding
import com.example.hijaiyahku_new.databinding.ActivityDaftarSoalBinding

class DaftarSoal : AppCompatActivity() {

    private lateinit var recycler: RecyclerView
    private lateinit var viewModel: DaftarSoalViewModel

    private lateinit var binding: ActivityDaftarSoalBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDaftarSoalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recycler = binding.rvSoal

        recycler.layoutManager = GridLayoutManager(this, 1)

        initAction()

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory).get(DaftarSoalViewModel::class.java)

        val adapter = DaftarSoalAdapter { soal ->
            val detailIntent = Intent(this@DaftarSoal, DetailQuest::class.java)
            detailIntent.putExtra("SOAL", soal.id)
            startActivity(detailIntent)
        }
        viewModel.soal.observe(this) { pagedList ->
            adapter.submitList(pagedList)
        }
        recycler.adapter = adapter
    }

    private fun initAction() {
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.Callback() {
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                return makeMovementFlags(0, ItemTouchHelper.RIGHT)
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//                val habit = (viewHolder as SoalAdapter.SoalViewHolder).getSoal
//                viewModel.deleteHabit(habit)
            }

        })
        itemTouchHelper.attachToRecyclerView(recycler)
    }
}