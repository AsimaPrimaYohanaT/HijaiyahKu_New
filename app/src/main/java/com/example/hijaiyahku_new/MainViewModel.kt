package com.example.hijaiyahku_new

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.hijaiyahku_new.data.MusicPreference
import com.example.hijaiyahku_new.data.Status
import kotlinx.coroutines.launch

class MainViewModel(private val pref: MusicPreference): ViewModel() {
    fun saveStatus(status: Status){
        viewModelScope.launch {
            pref.saveState(Status(status.music))
        }
    }

    fun getStatus(): LiveData<Status>{
        return pref.getStatus().asLiveData()
    }
}

class MainViewModelFactory(private val pref: MusicPreference, private val context: Context) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(pref) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}