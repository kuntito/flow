package com.example.flow.data.models

import com.example.flow.data.remote.response_models.SongWithUrl

data class Song(
    val id: Int,
    val title: String,
    val artistStr: String,
    val durationMillis: Int,
    val albumArtUrl: String? = null,
    val songUrl: String,
)

fun SongWithUrl.toSong() = Song(
    id = id,
    title = title,
    artistStr = artistStr,
    durationMillis = durationMillis,
    albumArtUrl = albumArtUrl,
    songUrl = songUrl,
)

val dummySong = Song(
    id = 0,
    title = "Monica Lewinsky",
    artistStr = "SAINt JHN",
    durationMillis = 150000,
    songUrl = "",
)

val dummySongList = listOf(
    Song(
        id = 0,
        title = "Monica Lewinsky",
        artistStr = "SAINt JHN",
        durationMillis = 150000,
        songUrl = "",
    ),
    Song(
        id = 1,
        title = "Switched Up",
        artistStr = "Nasty C",
        durationMillis = 150000,
        songUrl = "",
    ),
    Song(
        id = 2,
        title = "Storage",
        artistStr = "Conor Maynard",
        durationMillis = 150000,
        songUrl = "",
    ),
    Song(
        id = 3,
        title = "Understand",
        artistStr = "Omah Lay",
        durationMillis = 150000,
        songUrl = "",
    ),
    Song(
        id = 4,
        title = "Waka Jeje",
        artistStr = "BNXN (feat. Majeeed)",
        durationMillis = 150000,
        songUrl = "",
    ),
    Song(
        id = 5,
        title = "Naira Marley",
        artistStr = "Zinoleesky",
        durationMillis = 150000,
        songUrl = "",
    ),
    Song(
        id = 6,
        title = "Champion",
        artistStr = "Elina",
        durationMillis = 150000,
        songUrl = "",
    ),
    Song(
        id = 7,
        title = "Again",
        artistStr = "Sasha Sloan",
        durationMillis = 150000,
        songUrl = "",
    ),
    Song(
        id = 8,
        title = "smiling when i die",
        artistStr = "Sasha Sloan",
        durationMillis = 150000,
        songUrl = "",
    ),
    Song(
        id = 9,
        title = "Dealer",
        artistStr = "Ayo Maff (feat. FireboyDML)",
        durationMillis = 150000,
        songUrl = "",
    ),
    Song(
        id = 10,
        title = "365 Days",
        artistStr = "Tml Vibez",
        durationMillis = 150000,
        songUrl = "",
    ),
    Song(
        id = 11,
        title = "Design",
        artistStr = "Olivetheboy",
        durationMillis = 150000,
        songUrl = "",
    ),
    Song(
        id = 12,
        title = "Rara",
        artistStr = "Tml Vibez",
        durationMillis = 150000,
        songUrl = "",
    ),
    Song(
        id = 13,
        title = "Fall Back",
        artistStr = "Lithe",
        durationMillis = 150000,
        songUrl = "",
    ),
    Song(
        id = 14,
        title = "Can't Breathe",
        artistStr = "Llona",
        durationMillis = 150000,
        songUrl = "",
    ),
    Song(
        id = 15,
        title = "23",
        artistStr = "Burna Boy",
        durationMillis = 150000,
        songUrl = "",
    ),
    Song(
        id = 16,
        title = "HBP (Remix)",
        artistStr = "Llona (feat. Bella Shmurda)",
        durationMillis = 150000,
        songUrl = "",
    ),
    Song(
        id = 17,
        title = "Trees",
        artistStr = "Olivetheboy",
        durationMillis = 150000,
        songUrl = "",
    ),
    Song(
        id = 18,
        title = "Worst Luck",
        artistStr = "6LACK",
        durationMillis = 150000,
        songUrl = "",
    ),
    Song(
        id = 19,
        title = "Dreams",
        artistStr = "NF",
        durationMillis = 150000,
        songUrl = "",
    ),
)