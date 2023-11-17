package com.example.hijaiyahku_new

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.hijaiyahku_new.data.Soal

class DaftarSoalAdapter(
   private val viewModel: DaftarSoalViewModel,
    private val onClick: (Soal,MutableList<Int>) -> Unit
) : PagedListAdapter<Soal, DaftarSoalAdapter.SoalViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SoalViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_soal, parent, false)
        return SoalViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SoalViewHolder, position: Int) {
        val soal = getItem(position) as Soal
        val arrId:MutableList<Int> = mutableListOf<Int>()
        for (i in 0..(itemCount-1)){
            getItem(i)?.let { arrId.add(it.id) }
        }
        holder.bind(soal,arrId)
    }


    inner class SoalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTitle: TextView = itemView.findViewById(R.id.nomor_soal)
        private val lock: ImageView = itemView.findViewById<ImageView>(R.id.lock)
        lateinit var getSoal: Soal
        fun bind(soal: Soal,arrid : MutableList<Int>) {
            getSoal = soal
            val nomorSoal = position + 1
            tvTitle.text = nomorSoal.toString()

            if(soal.isComplete){
                lock.setVisibility(View.GONE)
            }else{
                lock.setVisibility(View.VISIBLE)
            }
            itemView.setOnClickListener {
                onClick(soal,arrid)
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