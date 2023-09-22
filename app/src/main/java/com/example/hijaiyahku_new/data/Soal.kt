package com.example.hijaiyahku_new.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "soal")
data class Soal(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val soal: String,
    val jawaban1: String,
    val jawaban2: String,
    val jenis: Int,
    val isComplete: Boolean
): Parcelable