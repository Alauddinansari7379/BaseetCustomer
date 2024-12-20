package com.amtech.baseetcustomer.Notification

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import com.amtech.baseetcustomer.R

class SoundService : Service() {
    private var mediaPlayer: MediaPlayer? = null
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
//        mediaPlayer = MediaPlayer.create(this, R.raw.song)
//        mediaPlayer!!.isLooping = true
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        mediaPlayer!!.start()
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer!!.stop()
        mediaPlayer!!.release()
        mediaPlayer = null
    }

    companion object {
        private const val TAG = "MyService"
    }
}
