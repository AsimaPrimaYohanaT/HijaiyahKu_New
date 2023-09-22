package com.example.hijaiyahku_new

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.example.hijaiyahku_new.data.Soal
import com.example.hijaiyahku_new.data.SoalRepository

class DetailQuestViewModel(private val soalRepository: SoalRepository): ViewModel() {

    private val _soalId = MutableLiveData<Int>()

    private val _soal = _soalId.switchMap { id ->
        soalRepository.getSoalById(id)
    }
    val soal: LiveData<Soal> = _soal

    fun start(habitId: Int) {
        if (habitId == _soalId.value) {
            return
        }
        _soalId.value = habitId
    }

}