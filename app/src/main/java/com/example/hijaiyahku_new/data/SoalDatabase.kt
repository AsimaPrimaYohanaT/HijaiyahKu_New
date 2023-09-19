package com.example.hijaiyahku_new.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.hijaiyahku_new.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

@Database(
    entities = [Soal::class],
    version = 1,
    exportSchema = false
)
abstract class SoalDatabase : RoomDatabase() {

    abstract fun soalDao(): SoalDao

    companion object {

        @Volatile
        private var INSTANCE: SoalDatabase? = null

        fun getInstance(context: Context): SoalDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext,
                    SoalDatabase::class.java, "soal.db")
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            GlobalScope.launch(Dispatchers.IO) {
                                fillWithStartingData(context, getInstance(context).soalDao())
                            }
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private fun fillWithStartingData(context: Context, dao: SoalDao) {
            val jsonArray = loadJsonArray(context)
            try {
                if (jsonArray != null) {
                    for (i in 0 until jsonArray.length()) {
                        val item = jsonArray.getJSONObject(i)
                        dao.insertAll(
                            Soal(
                                item.getInt("id"),
                                item.getString("soal"),
                                item.getInt("jenis"),
                                item.getBoolean("isComplete")
                            )
                        )
                    }
                }
            } catch (exception: JSONException) {
                exception.printStackTrace()
            }
        }

        private fun loadJsonArray(context: Context): JSONArray? {
            val builder = StringBuilder()
            val inputStream = context.resources.openRawResource(R.raw.soal)
            val reader = BufferedReader(InputStreamReader(inputStream))
            var line: String?
            try {
                while (reader.readLine().also { line = it } != null) {
                    builder.append(line)
                }
                val json = JSONObject(builder.toString())
                return json.getJSONArray("soal")
            } catch (exception: IOException) {
                exception.printStackTrace()
            } catch (exception: JSONException) {
                exception.printStackTrace()
            } finally {
                try {
                    inputStream?.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            return null
        }
    }
}
