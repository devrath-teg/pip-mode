package com.economist.demo.pip.screens.composables

import android.app.Activity
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.economist.demo.R
import com.economist.demo.pip.VideoPlayerPipViewModel
import com.economist.demo.pip.screens.composables.player.PipVideoPlayer

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun VideoPlayerScreen(
    onEnterPip: () -> Unit,
    onEnterFullscreen: () -> Unit,
    onExitFullscreen: () -> Unit
) {
    val viewModel: VideoPlayerPipViewModel = viewModel()
    val videoUrl by viewModel.videoUrl.collectAsState()
    val uri = remember(videoUrl) { videoUrl.toUri() }

    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val screenHeight = configuration.screenHeightDp.dp

    LaunchedEffect(isLandscape) {
        val window = (context as? Activity)?.window ?: return@LaunchedEffect
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

    LaunchedEffect(Unit) {
        if (!viewModel.isInitialized) {
            viewModel.playNewVideo(context, uri)
        }
    }

    val videoList = listOf(
        "https://bestvpn.org/html5demos/assets/dizzy.mp4",
        "https://files.testfile.org/Video%20MP4%2FRoad%20-%20testfile.org.mp4",
        "https://files.testfile.org/Video%20MP4%2FSand%20-%20testfile.org.mp4",
        "https://files.testfile.org/Video%20MP4%2FRiver%20-%20testfile.org.mp4",
        "https://files.testfile.org/Video%20MP4%2FRecord%20-%20testfile.org.mp4",
        "https://files.testfile.org/Video%20MP4%2FPeople%20-%20testfile.org.mp4"
    )

    if (isLandscape) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            PipVideoPlayer(
                videoUri = uri,
                viewModel = viewModel,
                modifier = Modifier.fillMaxSize()
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
            ) {
                PipVideoPlayer(
                    videoUri = uri,
                    viewModel = viewModel,
                    modifier = Modifier.fillMaxSize()
                )

                IconButton(
                    onClick = {
                        //requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
                        onExitFullscreen.invoke()
                    },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(
                            top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding(),
                            end = 8.dp
                        )
                ) {
                    androidx.compose.material.Icon(
                        painter = painterResource(id = R.drawable.ic_fullscreen_exit), // Add this icon in drawable
                        contentDescription = "Exit Fullscreen",
                        tint = Color.White
                    )
                }
            }
        }
    } else {
        Column(
            modifier = Modifier.fillMaxSize().background(Color.Black)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(screenHeight / 3)
            ) {
                PipVideoPlayer(videoUri = uri, viewModel = viewModel, modifier = Modifier.matchParentSize())

                Row(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(
                            top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding(),
                            end = 8.dp
                        )
                ) {
                    IconButton(
                        onClick = { onEnterPip.invoke()  },
                    ) {
                        androidx.compose.material.Icon(
                            painter = painterResource(id = R.drawable.ic_picture_in_picture),
                            contentDescription = "Enter PiP",
                            tint = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    IconButton(
                        onClick = {
                            //requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                            onEnterFullscreen.invoke()
                        }
                    ) {
                        androidx.compose.material.Icon(
                            painter = painterResource(id = R.drawable.ic_full_screen),
                            contentDescription = "Enter Fullscreen",
                            tint = Color.White
                        )
                    }
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                itemsIndexed(videoList) { index, url ->
                    val selected = url == videoUrl
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                viewModel.release()
                                viewModel.playNewVideo(context, Uri.parse(url))
                            }
                            .background(if (selected) Color.DarkGray else Color.Transparent)
                            .padding(12.dp)
                    ) {
                        Text(
                            text = "Track ${index + 1}",
                            color = Color.White,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }
        }
    }
}
