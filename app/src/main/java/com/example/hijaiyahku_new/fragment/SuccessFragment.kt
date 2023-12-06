package com.example.hijaiyahku_new.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.example.hijaiyahku_new.DaftarSoal
import com.example.hijaiyahku_new.DetailQuest
import com.example.hijaiyahku_new.R

class SuccessFragment: DialogFragment(){

    companion object {
        fun newInstance(id:Int?,arrId: ArrayList<Int>, jenis:String): SuccessFragment {
            val fragment = SuccessFragment()
            val bundle = Bundle()
            bundle.putIntegerArrayList("arrId", arrId)
            if(id == null){
                bundle.putInt("id",0)
            }else{
                bundle.putInt("id",id)
            }
            bundle.putString("jenis",jenis)
            fragment.arguments = bundle
            return fragment
        }
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val arrId = arguments?.getIntegerArrayList("arrId")
        val id = arguments?.getInt("id")
        val jenis = arguments?.getString("jenis")
        // Inflate the custom dialog layout
        val builder = AlertDialog.Builder(requireActivity())
        val inflater = requireActivity().layoutInflater
        val dialogView = inflater.inflate(R.layout.custom_dialog_2, null)
        builder.setView(dialogView)

        // Handle dialog button click or other interactions
        val closeButton = dialogView.findViewById<Button>(R.id.btnClose)
        closeButton.setOnClickListener {
            if(id == 0){
                val daftarSoalIntent = Intent(context,DaftarSoal::class.java)
                startActivity(daftarSoalIntent)
            }else{
                val detailIntent = Intent(context, DetailQuest::class.java)
                detailIntent.putExtra("SOAL", id)
                detailIntent.putExtra("jenis",jenis)
                val bundle = Bundle()
                bundle.putIntegerArrayList("arrId", arrId?.let { it1 -> ArrayList(it1) })
                Log.d("oke",id.toString())
                detailIntent.putExtras(bundle)
                startActivity(detailIntent)

            }
            dismiss()
        }

        return builder.create()
    }
}