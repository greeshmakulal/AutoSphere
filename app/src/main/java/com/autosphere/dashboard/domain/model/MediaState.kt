package com.autosphere.dashboard.domain.model

enum class AudioSource(val displayName: String) {
    SPOTIFY("Spotify"),
    BLUETOOTH("Bluetooth Audio"),
    RADIO("FM / DAB+"),
    USB("USB Drive")
}

data class Track(
    val id: String,
    val title: String,
    val artist: String,
    val album: String,
    val durationSeconds: Long,
    val genre: String = "Electronic",
    val artworkResId: Int? = null
)

data class MediaState(
    val currentTrack: Track = Track(
        id = "track_1",
        title = "Midnight Drive (EV Synth)",
        artist = "Kavinsky & Daft Sound",
        album = "Outrun Horizons 2026",
        durationSeconds = 245
    ),
    val isPlaying: Boolean = true,
    val playbackPositionSeconds: Long = 84,
    val volumePercent: Int = 45,
    val currentSource: AudioSource = AudioSource.SPOTIFY,
    val isShuffleOn: Boolean = false,
    val isRepeatOn: Boolean = false,
    val playlist: List<Track> = defaultPlaylist(),
    val recentlyPlayed: List<Track> = defaultRecentTracks()
) {
    val progressFraction: Float
        get() = if (currentTrack.durationSeconds > 0) {
            (playbackPositionSeconds.toFloat() / currentTrack.durationSeconds.toFloat()).coerceIn(0f, 1f)
        } else 0f

    companion object {
        fun defaultPlaylist(): List<Track> = listOf(
            Track("track_1", "Midnight Drive (EV Synth)", "Kavinsky & Daft Sound", "Outrun Horizons 2026", 245),
            Track("track_2", "Electric Horizon", "Tycho", "Simulcast", 212),
            Track("track_3", "Cyberpunk Autobahn", "Lorn", "The Maze", 278),
            Track("track_4", "Neon Cascade", "Com Truise", "Galactic Melt", 230),
            Track("track_5", "Starlight Cruise", "M83", "Hurry Up, We're Dreaming", 305)
        )

        fun defaultRecentTracks(): List<Track> = listOf(
            Track("track_6", "Aerodynamic Resonance", "Kraftwerk Remixed", "Tour de Sound", 290),
            Track("track_7", "Velocity Vector", "The Midnight", "Monsters", 255),
            Track("track_8", "Solar Wind", "Jon Hopkins", "Singularity", 380)
        )
    }
}
