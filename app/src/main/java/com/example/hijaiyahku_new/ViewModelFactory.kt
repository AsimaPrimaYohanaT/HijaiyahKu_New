package com.example.hijaiyahku_new

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.hijaiyahku_new.data.SoalRepository

class ViewModelFactory private constructor(private val soalRepository: SoalRepository) :
    ViewModelProvider.Factory{

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(
                    SoalRepository.getInstance(context)
                )
            }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        when {
            modelClass.isAssignableFrom(DaftarSoalViewModel::class.java) -> {
                DaftarSoalViewModel(soalRepository) as T
            }
            modelClass.isAssignableFrom(DetailQuestViewModel::class.java) -> {
                DetailQuestViewModel(soalRepository) as T
            }
            else -> throw Throwable("Unknown ViewModel class: " + modelClass.name)
        }
}