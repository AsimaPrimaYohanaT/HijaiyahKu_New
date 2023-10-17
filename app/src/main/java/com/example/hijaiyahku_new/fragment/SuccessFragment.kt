package com.example.hijaiyahku_new.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.hijaiyahku_new.DetailQuestViewModel
import com.example.hijaiyahku_new.R
import com.example.hijaiyahku_new.ViewModelFactory

class SuccessFragment: DialogFragment(){
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        // Inflate the custom dialog layout
        val builder = AlertDialog.Builder(requireActivity())
        val inflater = requireActivity().layoutInflater
        val dialogView = inflater.inflate(R.layout.custom_dialog_2, null)
        builder.setView(dialogView)

        // Handle dialog button click or other interactions
        val closeButton = dialogView.findViewById<Button>(R.id.btnClose)
        closeButton.setOnClickListener {

            dismiss() // Close the dialog
        }

        return builder.create()
    }
}