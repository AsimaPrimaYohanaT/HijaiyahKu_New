package com.example.hijaiyahku_new.fragment


import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.example.hijaiyahku_new.R

class HintChooseQuest : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Inflate the custom dialog layout
        val builder = AlertDialog.Builder(requireActivity())
        val inflater = requireActivity().layoutInflater
        val dialogView = inflater.inflate(R.layout.fragment_hint_choose_quest, null)
        builder.setView(dialogView)

//        // Handle dialog button click or other interactions
//        val closeButton = dialogView.findViewById<Button>(R.id.btnClose)
//        closeButton.setOnClickListener {
//            dismiss() // Close the dialog
//        }

        return builder.create()
    }
}