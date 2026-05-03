package com.example.flow.data.models

import com.example.flow.data.remote.response_models.SongWithUrl

data class Song(
    val id: Int,
    val title: String,
    val artistStr: String,
    val durationMillis: Int,
    val albumArtUrl: String,
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

private val template = Song(
    id = 0,
    title = "",
    artistStr = "",
    durationMillis = 150000,
    albumArtUrl = "",
    songUrl = "",
)
val dummySong = template.copy(
    id = 0,
    title = "Monica Lewinsky",
    artistStr = "SAINt JHN",
)

val dummySongList = listOf(
    template.copy(
        id = 0,
        title = "Monica Lewinsky",
        artistStr = "SAINt JHN",
    ),
    template.copy(
        id = 1,
        title = "Switched Up",
        artistStr = "Nasty C",
    ),
    template.copy(
        id = 2,
        title = "Storage",
        artistStr = "Conor Maynard",
    ),
    template.copy(
        id = 3,
        title = "Understand",
        artistStr = "Omah Lay",
    ),
    template.copy(
        id = 4,
        title = "Waka Jeje",
        artistStr = "BNXN (feat. Majeeed)",
    ),
    template.copy(
        id = 5,
        title = "Naira Marley",
        artistStr = "Zinoleesky",
    ),
    template.copy(
        id = 6,
        title = "Champion",
        artistStr = "Elina",
    ),
    template.copy(
        id = 7,
        title = "Again",
        artistStr = "Sasha Sloan",
    ),
    template.copy(
        id = 8,
        title = "smiling when i die",
        artistStr = "Sasha Sloan",
    ),
    template.copy(
        id = 9,
        title = "Dealer",
        artistStr = "Ayo Maff (feat. FireboyDML)",
    ),
    template.copy(
        id = 10,
        title = "365 Days",
        artistStr = "Tml Vibez",
    ),
    template.copy(
        id = 11,
        title = "Design",
        artistStr = "Olivetheboy",
    ),
    template.copy(
        id = 12,
        title = "Rara",
        artistStr = "Tml Vibez",
    ),
    template.copy(
        id = 13,
        title = "Fall Back",
        artistStr = "Lithe",
    ),
    template.copy(
        id = 14,
        title = "Can't Breathe",
        artistStr = "Llona",
    ),
    template.copy(
        id = 15,
        title = "23",
        artistStr = "Burna Boy",
    ),
    template.copy(
        id = 16,
        title = "HBP (Remix)",
        artistStr = "Llona (feat. Bella Shmurda)",
    ),
    template.copy(
        id = 17,
        title = "Trees",
        artistStr = "Olivetheboy",
    ),
    template.copy(
        id = 18,
        title = "Worst Luck",
        artistStr = "6LACK",
    ),
    template.copy(
        id = 19,
        title = "Dreams",
        artistStr = "NF",
    ),
)