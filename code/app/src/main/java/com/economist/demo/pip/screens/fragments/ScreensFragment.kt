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
import com.economist.demo.pip.screens.ScreensNavHost
import com.economist.demo.pip.screens.activities.BaseScreenActivity
import com.economist.demo.pip.screens.activities.VideoPlayerFullScreenActivity

class ScreensFragment : Fragment() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setContent {

            val activity = requireActivity() as BaseScreenActivity

            ScreensNavHost(
                launchPlayerScreen = {
                    activity.launchPlayerActivity()
                },
                onBackPressed = {
                    activity.onBackPressedDispatcher.onBackPressed()
                },
            )
        }
    }
}
