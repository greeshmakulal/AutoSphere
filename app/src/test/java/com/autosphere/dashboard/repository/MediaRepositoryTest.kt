package com.autosphere.dashboard.repository

import com.autosphere.dashboard.data.repository.MediaRepositoryImpl
import com.autosphere.dashboard.domain.model.AudioSource
import com.autosphere.dashboard.domain.model.Track
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MediaRepositoryTest {

    private lateinit var repository: MediaRepositoryImpl
    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @Before
    fun setUp() {
        repository = MediaRepositoryImpl(testScope)
    }

    @Test
    fun testInitialMediaState() {
        val state = repository.mediaState.value
        assertTrue(state.isPlaying)
        assertEquals(AudioSource.SPOTIFY, state.currentSource)
        assertTrue(state.playlist.isNotEmpty())
        assertEquals(45, state.volumePercent)
    }

    @Test
    fun testTogglePlayPause() {
        repository.togglePlayPause()
        assertFalse(repository.mediaState.value.isPlaying)

        repository.togglePlayPause()
        assertTrue(repository.mediaState.value.isPlaying)
    }

    @Test
    fun testNextAndPreviousTrack() {
        val initialTrack = repository.mediaState.value.currentTrack
        repository.playNext()
        val nextTrack = repository.mediaState.value.currentTrack
        assertTrue(initialTrack.id != nextTrack.id)
        assertEquals(0L, repository.mediaState.value.playbackPositionSeconds)

        repository.playPrevious()
        assertEquals(initialTrack.id, repository.mediaState.value.currentTrack.id)
    }

    @Test
    fun testVolumeClamping() {
        repository.setVolume(75)
        assertEquals(75, repository.mediaState.value.volumePercent)

        repository.setVolume(150)
        assertEquals(100, repository.mediaState.value.volumePercent)

        repository.setVolume(-20)
        assertEquals(0, repository.mediaState.value.volumePercent)
    }

    @Test
    fun testAudioSourceSelection() {
        repository.setAudioSource(AudioSource.BLUETOOTH)
        assertEquals(AudioSource.BLUETOOTH, repository.mediaState.value.currentSource)

        repository.setAudioSource(AudioSource.RADIO)
        assertEquals(AudioSource.RADIO, repository.mediaState.value.currentSource)
    }

    @Test
    fun testSelectTrack() {
        val newTrack = Track(
            id = "custom_1",
            title = "Synthetic Dawn",
            artist = "Solaris",
            album = "Cosmic EV",
            durationSeconds = 180
        )
        repository.selectTrack(newTrack)
        assertEquals(newTrack.id, repository.mediaState.value.currentTrack.id)
        assertTrue(repository.mediaState.value.recentlyPlayed.any { it.id == "track_1" })
    }
}
