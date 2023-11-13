package com.example.hijaiyahku_new.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MusicPreference private constructor(private val dataStore: DataStore<Preferences>){

    fun getStatus(): Flow<Status>{
        return dataStore.data.map{ preferences ->
            Status(
                preferences[STATUS]?: false
            )
        }
    }

    suspend fun saveState(state: Status){
        dataStore.edit{preferences ->
            preferences[STATUS] = state.music
        }
    }

    companion object{
        @Volatile
        private var INSTANCE: MusicPreference?= null

        private val STATUS = booleanPreferencesKey("status")

        fun getInstance(dataStore: DataStore<Preferences>): MusicPreference{
            return INSTANCE?: synchronized(this){
                val instance = MusicPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}