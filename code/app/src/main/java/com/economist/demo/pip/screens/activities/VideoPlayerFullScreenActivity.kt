package com.economist.demo.pip.screens.activities

import android.app.PictureInPictureParams
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.Rational
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.media3.common.util.UnstableApi
import com.economist.demo.R
import com.economist.demo.pip.screens.fragments.VideoPlayerFragment

@androidx.annotation.OptIn(UnstableApi::class)
class VideoPlayerFullScreenActivity : AppCompatActivity() {

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val aspectRatio = Rational(16, 9)
            val pipParams = PictureInPictureParams.Builder()
                .setAspectRatio(aspectRatio)
                .build()
            enterPictureInPictureMode(pipParams)
        }
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        enterPipMode()
    }
}



