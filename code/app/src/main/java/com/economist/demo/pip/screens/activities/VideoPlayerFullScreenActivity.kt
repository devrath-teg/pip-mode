package com.economist.demo.pip.screens.activities

import android.app.PendingIntent
import android.app.PictureInPictureParams
import android.app.RemoteAction
import android.content.Intent
import android.content.Intent.*
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Rect
import android.graphics.drawable.Icon
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.Rational
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.media3.common.util.UnstableApi
import com.economist.demo.R
import com.economist.demo.pip.VideoPlayerPipViewModel
import com.economist.demo.pip.screens.fragments.VideoPlayerFragment

@androidx.annotation.OptIn(UnstableApi::class)
class VideoPlayerFullScreenActivity : AppCompatActivity() {

    private val pipReceiver = PiPActionReceiver()
    private val viewModel: VideoPlayerPipViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setOrientation()

        setContentView(R.layout.activity_main) // Ensure it has `fragment_container`

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, VideoPlayerFragment())
                .commit()
        }
    }

    override fun onStart() {
        super.onStart()

        pipActionHandler = object : PiPActionHandler {
            override fun onPlay() {
                actionPlay()
            }

            override fun onPause() {
                actionPause()
            }
        }

        val filter = IntentFilter().apply {
            addAction(ACTION_PLAY)
            addAction(ACTION_PAUSE)
        }

        ContextCompat.registerReceiver(
            this,
            pipReceiver,
            filter,
            ContextCompat.RECEIVER_NOT_EXPORTED
        )
    }

    override fun onStop() {
        super.onStop()
        pipActionHandler = null
        unregisterReceiver(pipReceiver)
    }

    fun setOrientation() {
        val isLandscape = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

        if (isLandscape) {
            WindowCompat.setDecorFitsSystemWindows(window, false)
            WindowInsetsControllerCompat(window, window.decorView).apply {
                hide(WindowInsetsCompat.Type.systemBars())
                systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            WindowCompat.setDecorFitsSystemWindows(window, true)
            WindowInsetsControllerCompat(window, window.decorView)
                .show(WindowInsetsCompat.Type.systemBars())
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun enterPipMode() {
        val aspectRatio = Rational(16, 9)

        val playIntent = PendingIntent.getBroadcast(
            this,
            0,
            Intent(ACTION_PLAY).setPackage(packageName),
            PendingIntent.FLAG_IMMUTABLE
        )
        val playIcon = Icon.createWithResource(this, R.drawable.ic_play)
        val playAction = RemoteAction(playIcon, "Play", "Play video", playIntent)

        val pauseIntent = PendingIntent.getBroadcast(
            this,
            1,
            Intent(ACTION_PAUSE).setPackage(packageName),
            PendingIntent.FLAG_IMMUTABLE
        )
        val pauseIcon = Icon.createWithResource(this, R.drawable.ic_pause)
        val pauseAction = RemoteAction(pauseIcon, "Pause", "Pause video", pauseIntent)

        val pipParams = PictureInPictureParams.Builder()
            .setAspectRatio(aspectRatio)
            .setActions(listOf(playAction, pauseAction))
            .build()

        enterPictureInPictureMode(pipParams)
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            enterPipMode()
        }
    }

    fun actionPlay() {
        Toast.makeText(this, "Play clicked", Toast.LENGTH_SHORT).show()
        // ViewModel or playback logic goes here
    }

    fun actionPause() {
        Toast.makeText(this, "Pause clicked", Toast.LENGTH_SHORT).show()
        // ViewModel or playback logic goes here
        viewModel.togglePlayPause()
    }

    override fun onPictureInPictureModeChanged(
        isInPictureInPictureMode: Boolean,
        newConfig: Configuration
    ) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)

        if (isInPictureInPictureMode) {
            viewModel.hideControls()
        } else {
            viewModel.showControls()
        }
    }

    companion object {
        const val ACTION_PLAY = "com.yourapp.ACTION_PLAY"
        const val ACTION_PAUSE = "com.yourapp.ACTION_PAUSE"

        var pipActionHandler: PiPActionHandler? = null
    }

    override fun onPause() {
        super.onPause()
        if (isInPictureInPictureMode) {
            viewModel.hideControls()
        }
    }

    override fun onResume() {
        super.onResume()
        if (!isInPictureInPictureMode) {
            viewModel.showControls()
        }
    }
}

interface PiPActionHandler {
    fun onPlay()
    fun onPause()
}



