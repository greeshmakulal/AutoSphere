package com.autosphere.dashboard.domain.repository

import com.autosphere.dashboard.domain.model.AudioSource
import com.autosphere.dashboard.domain.model.MediaState
import com.autosphere.dashboard.domain.model.Track
import kotlinx.coroutines.flow.StateFlow

interface MediaRepository {
    val mediaState: StateFlow<MediaState>
    fun togglePlayPause()
    fun playNext()
    fun playPrevious()
    fun seekTo(positionSeconds: Long)
    fun setVolume(volumePercent: Int)
    fun setAudioSource(source: AudioSource)
    fun selectTrack(track: Track)
    fun toggleShuffle()
    fun toggleRepeat()
}
