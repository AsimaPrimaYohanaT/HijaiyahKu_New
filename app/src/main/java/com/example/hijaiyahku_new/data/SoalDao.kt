package com.example.hijaiyahku_new.data

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.sqlite.db.SupportSQLiteQuery

@Dao
interface SoalDao {

    @RawQuery(observedEntities = [Soal::class])
    fun getSoal(query: SupportSQLiteQuery): DataSource.Factory<Int, Soal>

    @Query("SELECT * FROM soal WHERE jenis = :jenisSoal")
    fun getJenis(jenisSoal: Int): LiveData<Soal>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSoal(soal: Soal): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg soal: Soal)

    @Query("SELECT * FROM soal WHERE id = :soalId")
    fun getSoalById(soalId: Int): LiveData<Soal>

}