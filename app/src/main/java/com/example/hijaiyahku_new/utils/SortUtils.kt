package com.example.hijaiyahku_new.utils

import androidx.sqlite.db.SimpleSQLiteQuery

object SortUtils {

    fun getSorteredQuery(filter: SoalSortType): SimpleSQLiteQuery {
        val simpleQuery = StringBuilder().append("SELECT * FROM soal ")
        when (filter) {
            SoalSortType.TYPE_1-> {
                simpleQuery.append("WHERE jenis = 1")
            }
            SoalSortType.TYPE_2 -> {
                simpleQuery.append("WHERE jenis = 2")
            }
        }
        return SimpleSQLiteQuery(simpleQuery.toString())
    }
}