package com.economist.demo.pip.screens.fragments

import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.economist.demo.pip.screens.activities.VideoPlayerFullScreenActivity
import com.economist.demo.pip.screens.composables.VideoPlayerScreen

class VideoPlayerFragment : Fragment() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setContent {
            val activity = requireActivity() as VideoPlayerFullScreenActivity

            VideoPlayerScreen(
                onEnterPip = {
                    activity.enterPipMode()
                },
                onEnterFullscreen = {
                    activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                    activity.setOrientation()
                },
                onExitFullscreen = {
                    activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
                    activity.setOrientation()
                }
            )
        }
    }
}
