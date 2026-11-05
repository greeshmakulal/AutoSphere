package com.autosphere.dashboard.presentation.media

import androidx.lifecycle.ViewModel
import com.autosphere.dashboard.domain.model.AudioSource
import com.autosphere.dashboard.domain.model.MediaState
import com.autosphere.dashboard.domain.model.Track
import com.autosphere.dashboard.domain.usecase.MediaPlaybackUseCase
import kotlinx.coroutines.flow.StateFlow

class MediaViewModel(
    private val mediaUseCase: MediaPlaybackUseCase
) : ViewModel() {

    val mediaState: StateFlow<MediaState> = mediaUseCase.mediaState

    fun togglePlayPause() = mediaUseCase.togglePlayPause()
    fun playNext() = mediaUseCase.playNext()
    fun playPrevious() = mediaUseCase.playPrevious()
    fun seekTo(seconds: Long) = mediaUseCase.seekTo(seconds)
    fun setVolume(volume: Int) = mediaUseCase.setVolume(volume)
    fun setAudioSource(source: AudioSource) = mediaUseCase.setAudioSource(source)
    fun selectTrack(track: Track) = mediaUseCase.selectTrack(track)
    fun toggleShuffle() = mediaUseCase.toggleShuffle()
    fun toggleRepeat() = mediaUseCase.toggleRepeat()
}
