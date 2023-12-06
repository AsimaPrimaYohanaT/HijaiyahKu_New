package com.example.hijaiyahku_new

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import androidx.paging.PagedList
import com.example.hijaiyahku_new.data.MusicPreference
import com.example.hijaiyahku_new.data.Soal
import com.example.hijaiyahku_new.data.SoalRepository
import com.example.hijaiyahku_new.data.Status
import com.example.hijaiyahku_new.utils.SoalSortType

class DaftarSoalViewModel(private val soalRepository: SoalRepository) : ViewModel() {

    private val _filter = MutableLiveData<SoalSortType>()

    val soal: LiveData<PagedList<Soal>> = _filter.switchMap {
        soalRepository.getSoal(it)
    }

    init {
        _filter.value = SoalSortType.TYPE_1
    }

    fun filter(filterType: SoalSortType) {
        _filter.value = filterType
    }


}