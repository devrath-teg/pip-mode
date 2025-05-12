package com.economist.demo.pip

import android.content.Context
import android.net.Uri
import androidx.annotation.OptIn
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ClippingMediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class VideoPlayerPipViewModel : ViewModel() {

    private val _player = MutableStateFlow<ExoPlayer?>(null)
    val player: StateFlow<ExoPlayer?> = _player

    private val _currentPosition = MutableStateFlow(0L)
    val currentPosition: StateFlow<Long> = _currentPosition

    private val _duration = MutableStateFlow(1L)
    val duration: StateFlow<Long> = _duration

    private val _isMuted = MutableStateFlow(true)
    val isMuted: StateFlow<Boolean> = _isMuted

    private val _isPlaying = MutableStateFlow(true)
    val isPlaying: StateFlow<Boolean> = _isPlaying

    private val _videoUrl = MutableStateFlow("https://bestvpn.org/html5demos/assets/dizzy.mp4")
    val videoUrl: StateFlow<String> = _videoUrl

    private val _isBuffering = MutableStateFlow(true)
    val isBuffering: StateFlow<Boolean> = _isBuffering

    private val _showControls = MutableStateFlow(true)
    val showControls: StateFlow<Boolean> = _showControls

    private var progressJob: Job? = null
    private var isInitializedInternal = false
    val isInitialized: Boolean
        get() = isInitializedInternal

    fun playNewVideo(context: Context, videoUri: Uri) {
        if (isInitialized && _videoUrl.value == videoUri.toString()) return

        releasePlayer()

        _videoUrl.value = videoUri.toString()

        val exoPlayer = setUpExoPlayer(context, videoUri)
        _player.value = exoPlayer

        exoPlayer.volume = if (_isMuted.value) 0f else 1f
        exoPlayer.playWhenReady = _isPlaying.value

        _duration.value = exoPlayer.duration.takeIf { it > 0 } ?: 1L

        progressJob = viewModelScope.launch {
            while (true) {
                _currentPosition.value = exoPlayer.currentPosition
                _duration.value = exoPlayer.duration.takeIf { it > 0 } ?: 1L
                delay(500L)
            }
        }

        isInitializedInternal = true
    }

    @OptIn(UnstableApi::class)
    private fun setUpExoPlayer(context: Context, videoUri: Uri): ExoPlayer {
        val mediaItem = MediaItem.fromUri(videoUri)
        val dataSourceFactory = DefaultDataSource.Factory(context)
        val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(mediaItem)

        return ExoPlayer.Builder(context).build().apply {
            setMediaSource(mediaSource)
            prepare()
            seekTo(_currentPosition.value)
            playWhenReady = true

            addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(state: Int) {
                    _isBuffering.value = state == Player.STATE_BUFFERING
                }

                override fun onIsLoadingChanged(isLoading: Boolean) {
                    _isBuffering.value = isLoading
                }
            })
        }
    }

    private fun releasePlayer() {
        progressJob?.cancel()
        progressJob = null
        _player.value?.release()
        _player.value = null
        _currentPosition.value = 0L
        _duration.value = 1L
        _isBuffering.value = true
        isInitializedInternal = false
    }

    fun toggleMute() {
        _isMuted.value = !_isMuted.value
        _player.value?.volume = if (_isMuted.value) 0f else 1f
    }

    fun togglePlayPause() {
        _isPlaying.value = !_isPlaying.value
        _player.value?.playWhenReady = _isPlaying.value
    }

    fun restartVideo() {
        _player.value?.seekTo(0)
        _player.value?.playWhenReady = true
        _isPlaying.value = true
    }

    fun hideControls() {
        _showControls.value = false
    }

    fun showControls() {
        _showControls.value = true
    }

    fun release() {
        releasePlayer()
    }

    override fun onCleared() {
        super.onCleared()
        releasePlayer()
    }

    fun hideActions() {
        println("Dev")
    }

    fun showActions() {
        println("Devrath")
    }
}

