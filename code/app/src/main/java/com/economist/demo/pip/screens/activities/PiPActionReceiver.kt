package com.economist.demo.pip.screens.activities

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast


class PiPActionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            VideoPlayerFullScreenActivity.ACTION_PLAY -> {
                // Trigger play logic (you may delegate to a ViewModel or service)
                Toast.makeText(context, "Play clicked", Toast.LENGTH_SHORT).show()
            }

            VideoPlayerFullScreenActivity.ACTION_PAUSE -> {
                // Trigger pause logic
                Toast.makeText(context, "Pause clicked", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
