package com.example.hijaiyahku_new

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.hijaiyahku_new.data.Soal

class DaftarSoalAdapter(
    private val onClick: (Soal) -> Unit
) : PagedListAdapter<Soal, DaftarSoalAdapter.SoalViewHolder>(DIFF_CALLBACK) {

    //TODO 8 : Create and initialize ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SoalViewHolder {
        //throw NotImplementedError("Not yet implemented")
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_soal, parent, false)
        return SoalViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SoalViewHolder, position: Int) {
        //TODO 9 : Get data and bind them to ViewHolder
        val soal = getItem(position) as Soal
        holder.bind(soal)
    }

    inner class SoalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTitle: TextView = itemView.findViewById(R.id.textView)
        lateinit var getSoal: Soal
        fun bind(soal: Soal) {
            getSoal = soal
            tvTitle.text = soal.soal
            itemView.setOnClickListener {
                onClick(soal)
            }

        }

    }

    companion object {

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Soal>() {
            override fun areItemsTheSame(oldItem: Soal, newItem: Soal): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Soal, newItem: Soal): Boolean {
                return oldItem == newItem
            }
        }

    }

}