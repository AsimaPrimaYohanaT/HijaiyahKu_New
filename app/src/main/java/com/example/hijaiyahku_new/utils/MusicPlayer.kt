package com.example.hijaiyahku_new.utils

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri

class MusicPlayer(private val fileName: String,private  val context: Context) {

   var mediaPlayer :MediaPlayer

    init {
        mediaPlayer = MediaPlayer.create(context, Uri.parse("assets://bgm.mp3"))
    }

    public fun play() {

        mediaPlayer!!.setVolume(100f, 100f)
        mediaPlayer.start()
    }

    // Method untuk pause music
    fun pause() {
        mediaPlayer.pause()
    }
    fun onDestroy() {
        mediaPlayer!!.stop()
        mediaPlayer!!.release()
    }

    // Method untuk stop music
    fun stop() {
        mediaPlayer.stop()
    }
}