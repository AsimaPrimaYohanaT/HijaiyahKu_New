package com.example.hijaiyahku_new.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.hijaiyahku_new.DetailQuest
import com.example.hijaiyahku_new.DetailQuestViewModel
import com.example.hijaiyahku_new.R
import com.example.hijaiyahku_new.ViewModelFactory

class SuccessFragment: DialogFragment(){

    companion object {
        fun newInstance(id:Int,arrId: ArrayList<Int>): SuccessFragment {
            val fragment = SuccessFragment()
            val bundle = Bundle()
            bundle.putIntegerArrayList("arrId", arrId)
            bundle.putInt("id",id)
            fragment.arguments = bundle
            return fragment
        }
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val arrId = arguments?.getInt("arrId")
        val id = arguments?.getInt("id")
        // Inflate the custom dialog layout
        val builder = AlertDialog.Builder(requireActivity())
        val inflater = requireActivity().layoutInflater
        val dialogView = inflater.inflate(R.layout.custom_dialog_2, null)
        builder.setView(dialogView)

        // Handle dialog button click or other interactions
        val closeButton = dialogView.findViewById<Button>(R.id.btnClose)
        closeButton.setOnClickListener {

            val detailIntent = Intent(context, DetailQuest::class.java)
            detailIntent.putExtra("SOAL", id)
            val bundle = Bundle()
            bundle.putIntegerArrayList("arrId", arrId?.let { it1 -> ArrayList(it1) })
            detailIntent.putExtras(bundle)


                startActivity(detailIntent)




            dismiss() // Close the dialog
        }

        return builder.create()
    }
}