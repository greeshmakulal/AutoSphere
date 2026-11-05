package com.autosphere.dashboard.data.repository

import com.autosphere.dashboard.domain.model.AudioSource
import com.autosphere.dashboard.domain.model.MediaState
import com.autosphere.dashboard.domain.model.Track
import com.autosphere.dashboard.domain.repository.MediaRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class MediaRepositoryImpl(
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Default)
) : MediaRepository {

    private val _mediaState = MutableStateFlow(MediaState())
    override val mediaState: StateFlow<MediaState> = _mediaState.asStateFlow()

    private var progressJob: Job? = null

    init {
        startProgressTracker()
    }

    private fun startProgressTracker() {
        progressJob?.cancel()
        progressJob = scope.launch {
            while (isActive) {
                delay(1000)
                val current = _mediaState.value
                if (current.isPlaying) {
                    val nextSec = current.playbackPositionSeconds + 1
                    if (nextSec >= current.currentTrack.durationSeconds) {
                        playNext()
                    } else {
                        _mediaState.value = current.copy(playbackPositionSeconds = nextSec)
                    }
                }
            }
        }
    }

    override fun togglePlayPause() {
        val current = _mediaState.value
        _mediaState.value = current.copy(isPlaying = !current.isPlaying)
    }

    override fun playNext() {
        val current = _mediaState.value
        val playlist = current.playlist
        if (playlist.isEmpty()) return

        val currentIndex = playlist.indexOfFirst { it.id == current.currentTrack.id }
        val nextIndex = if (current.isShuffleOn) {
            playlist.indices.random()
        } else {
            (currentIndex + 1) % playlist.size
        }
        val nextTrack = playlist[nextIndex]

        val updatedRecent = (listOf(current.currentTrack) + current.recentlyPlayed).distinctBy { it.id }.take(6)

        _mediaState.value = current.copy(
            currentTrack = nextTrack,
            playbackPositionSeconds = 0,
            isPlaying = true,
            recentlyPlayed = updatedRecent
        )
    }

    override fun playPrevious() {
        val current = _mediaState.value
        val playlist = current.playlist
        if (playlist.isEmpty()) return

        if (current.playbackPositionSeconds > 3) {
            _mediaState.value = current.copy(playbackPositionSeconds = 0)
            return
        }

        val currentIndex = playlist.indexOfFirst { it.id == current.currentTrack.id }
        val prevIndex = if (currentIndex <= 0) playlist.size - 1 else currentIndex - 1
        val prevTrack = playlist[prevIndex]

        _mediaState.value = current.copy(
            currentTrack = prevTrack,
            playbackPositionSeconds = 0,
            isPlaying = true
        )
    }

    override fun seekTo(positionSeconds: Long) {
        val current = _mediaState.value
        val clamped = positionSeconds.coerceIn(0, current.currentTrack.durationSeconds)
        _mediaState.value = current.copy(playbackPositionSeconds = clamped)
    }

    override fun setVolume(volumePercent: Int) {
        val current = _mediaState.value
        _mediaState.value = current.copy(volumePercent = volumePercent.coerceIn(0, 100))
    }

    override fun setAudioSource(source: AudioSource) {
        val current = _mediaState.value
        _mediaState.value = current.copy(currentSource = source)
    }

    override fun selectTrack(track: Track) {
        val current = _mediaState.value
        val updatedRecent = (listOf(current.currentTrack) + current.recentlyPlayed).distinctBy { it.id }.take(6)
        _mediaState.value = current.copy(
            currentTrack = track,
            playbackPositionSeconds = 0,
            isPlaying = true,
            recentlyPlayed = updatedRecent
        )
    }

    override fun toggleShuffle() {
        val current = _mediaState.value
        _mediaState.value = current.copy(isShuffleOn = !current.isShuffleOn)
    }

    override fun toggleRepeat() {
        val current = _mediaState.value
        _mediaState.value = current.copy(isRepeatOn = !current.isRepeatOn)
    }
}
