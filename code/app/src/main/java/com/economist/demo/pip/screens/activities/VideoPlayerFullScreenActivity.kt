package com.economist.demo.pip.screens.activities

import android.app.PendingIntent
import android.app.PictureInPictureParams
import android.app.RemoteAction
import android.content.Intent
import android.content.Intent.*
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Rect
import android.graphics.drawable.Icon
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Rational
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.media3.common.util.UnstableApi
import com.economist.demo.R
import com.economist.demo.pip.screens.fragments.VideoPlayerFragment

@androidx.annotation.OptIn(UnstableApi::class)
class VideoPlayerFullScreenActivity : AppCompatActivity() {

    private val isPipSupported by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            packageManager.hasSystemFeature(PackageManager.FEATURE_PICTURE_IN_PICTURE)
        } else false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setOrientation()
        //setContent { SetContentForScreen() }
        setContentView(R.layout.activity_main) // Must include a container
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, VideoPlayerFragment())
                .commit()
        }
    }

    fun setOrientation() {
        val isLandscape = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

        if (isLandscape) {
            // Hide status bar and navigation bar
            WindowCompat.setDecorFitsSystemWindows(window, false)
            WindowInsetsControllerCompat(window, window.decorView).apply {
                hide(WindowInsetsCompat.Type.systemBars())
                systemBarsBehavior =
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            // Optional: Restore system bars in portrait mode
            WindowCompat.setDecorFitsSystemWindows(window, true)
            WindowInsetsControllerCompat(
                window,
                window.decorView
            ).show(WindowInsetsCompat.Type.systemBars())
        }
    }


    fun enterPipMode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            enterPictureInPictureMode()
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val params = getPipParams()
            enterPictureInPictureMode(params)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getPipParams(isPlaying: Boolean = false): PictureInPictureParams {
        val rect = Rect()
        //binding.player.getGlobalVisibleRect(rect)

        val builder = PictureInPictureParams.Builder()
            .setAspectRatio(Rational(16, 9))
            .setSourceRectHint(rect)
            .setActions(
                listOfNotNull(
                    getPipAction()
                )
            )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            builder.setAutoEnterEnabled(isPlaying)
        }

        return builder.build()
    }

    private fun getPipAction(): RemoteAction? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val actionUri = Uri.parse("https://youtube.com/AndroidDeveloperTips")
            val actionIntent = Intent(ACTION_VIEW, actionUri)
            val actionPendingIntent =
                PendingIntent.getActivity(this, 0, actionIntent, PendingIntent.FLAG_IMMUTABLE)
            val remoteAction = RemoteAction(
                Icon.createWithResource(this, R.drawable.ic_launcher_foreground),
                "More info",
                "More info action",
                actionPendingIntent
            )
            remoteAction
        } else null
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            // the PiP auto enter feature is not available and we should do it manually
            enterPipMode()
        }
    }


    override fun onPictureInPictureModeChanged(isInPictureInPictureMode: Boolean, newConfig: Configuration) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)
        }
        if (isInPictureInPictureMode) {
           // Hide actions

        } else {
            // Show actions
        }
    }
}



