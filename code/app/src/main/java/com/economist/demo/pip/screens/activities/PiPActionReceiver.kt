package com.economist.demo.pip.screens.activities

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast


class PiPActionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            VideoPlayerFullScreenActivity.ACTION_PLAY -> {
                VideoPlayerFullScreenActivity.pipActionHandler?.onPlay()
            }
            VideoPlayerFullScreenActivity.ACTION_PAUSE -> {
                VideoPlayerFullScreenActivity.pipActionHandler?.onPause()
            }
        }
    }
}

