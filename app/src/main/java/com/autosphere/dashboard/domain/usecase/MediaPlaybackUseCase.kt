package com.autosphere.dashboard.domain.usecase

import com.autosphere.dashboard.domain.model.AudioSource
import com.autosphere.dashboard.domain.model.MediaState
import com.autosphere.dashboard.domain.model.Track
import com.autosphere.dashboard.domain.repository.MediaRepository
import kotlinx.coroutines.flow.StateFlow

class MediaPlaybackUseCase(
    private val repository: MediaRepository
) {
    val mediaState: StateFlow<MediaState> = repository.mediaState

    fun togglePlayPause() = repository.togglePlayPause()
    fun playNext() = repository.playNext()
    fun playPrevious() = repository.playPrevious()
    fun seekTo(seconds: Long) = repository.seekTo(seconds)
    fun setVolume(volume: Int) = repository.setVolume(volume)
    fun setAudioSource(source: AudioSource) = repository.setAudioSource(source)
    fun selectTrack(track: Track) = repository.selectTrack(track)
    fun toggleShuffle() = repository.toggleShuffle()
    fun toggleRepeat() = repository.toggleRepeat()
}
