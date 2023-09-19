package com.example.hijaiyahku_new.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.hijaiyahku_new.utils.SoalSortType
import com.example.hijaiyahku_new.utils.SortUtils
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class SoalRepository(private val soalDao: SoalDao, private val executor: ExecutorService) {

    companion object {

        @Volatile
        private var instance: SoalRepository? = null

        fun getInstance(context: Context): SoalRepository {
            return instance ?: synchronized(this) {
                if (instance == null) {
                    val database = SoalDatabase.getInstance(context)
                    instance = SoalRepository(
                        database.soalDao(),
                        Executors.newSingleThreadExecutor()
                    )
                }
                return instance as SoalRepository
            }

        }
    }

//    //TODO 4 : Use SortUtils.getSortedQuery to create sortable query and build paged list
    fun getSoal(filter: SoalSortType): LiveData<PagedList<Soal>> {
        //throw NotImplementedError("Not yet implemented")
        val sortQuery = SortUtils.getSorteredQuery(filter)

        val configPage = PagedList.Config.Builder().setEnablePlaceholders(true).setPageSize(
            30).build()

        return LivePagedListBuilder(soalDao.getSoal(sortQuery),configPage).build()
    }

    fun insertSoal(newSoal: Soal) {
        //throw NotImplementedError("Not yet implemented")
        executor.execute {
            soalDao.insertSoal(newSoal)
        }
    }

    //TODO 5 : Complete other function inside repository
    fun getJenis(jenis: Int): LiveData<Soal> {
        //throw NotImplementedError("Not yet implemented")
        return soalDao.getJenis(jenis)
    }


}