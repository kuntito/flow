package com.example.flow.data.models

data class SongSearchItem(
    val id: Int,
    val title: String,
    val artistStr: String,
    val albumArtUrl: String,
    val durationMillis: Int,
)

val dummySongSearchItem = SongSearchItem(
    id = 0,
    title = "Monica Lewinsky",
    artistStr = "SAINt JHN",
    albumArtUrl = "",
    durationMillis = 300000
)

val dummySearchResults = listOf(
    SongSearchItem(
        id = 0,
        title = "Monica Lewinsky",
        artistStr = "SAINt JHN",
        albumArtUrl = "",
        durationMillis = 300000
    ),
    SongSearchItem(
        id = 1,
        title = "Switched Up",
        artistStr = "Nasty C",
        albumArtUrl = "",
        durationMillis = 300000
    ),
    SongSearchItem(
        id = 2,
        title = "Storage",
        artistStr = "Conor Maynard",
        albumArtUrl = "",
        durationMillis = 300000
    ),
    SongSearchItem(
        id = 3,
        title = "Understand",
        artistStr = "Omah Lay",
        albumArtUrl = "",
        durationMillis = 300000
    ),
    SongSearchItem(
        id = 4,
        title = "Waka Jeje",
        artistStr = "BNXN (feat. Majeeed)",
        albumArtUrl = "",
        durationMillis = 300000
    ),
    SongSearchItem(
        id = 5,
        title = "Naira Marley",
        artistStr = "Zinoleesky",
        albumArtUrl = "",
        durationMillis = 300000
    ),
    SongSearchItem(
        id = 6,
        title = "Champion",
        artistStr = "Elina",
        albumArtUrl = "",
        durationMillis = 300000
    ),
    SongSearchItem(
        id = 7,
        title = "Again",
        artistStr = "Sasha Sloan",
        albumArtUrl = "",
        durationMillis = 300000
    ),
    SongSearchItem(
        id = 8,
        title = "smiling when i die",
        artistStr = "Sasha Sloan",
        albumArtUrl = "",
        durationMillis = 300000
    ),
    SongSearchItem(
        id = 9,
        title = "Dealer",
        artistStr = "Ayo Maff (feat. FireboyDML)",
        albumArtUrl = "",
        durationMillis = 300000
    ),
    SongSearchItem(
        id = 10,
        title = "365 Days",
        artistStr = "Tml Vibez",
        albumArtUrl = "",
        durationMillis = 300000
    ),
    SongSearchItem(
        id = 11,
        title = "Design",
        artistStr = "Olivetheboy",
        albumArtUrl = "",
        durationMillis = 300000
    ),
    SongSearchItem(
        id = 12,
        title = "Rara",
        artistStr = "Tml Vibez",
        albumArtUrl = "",
        durationMillis = 300000
    ),
    SongSearchItem(
        id = 13,
        title = "Fall Back",
        artistStr = "Lithe",
        albumArtUrl = "",
        durationMillis = 300000
    ),
    SongSearchItem(
        id = 14,
        title = "Can't Breathe",
        artistStr = "Llona",
        albumArtUrl = "",
        durationMillis = 300000
    ),
    SongSearchItem(
        id = 15,
        title = "23",
        artistStr = "Burna Boy",
        albumArtUrl = "",
        durationMillis = 300000
    ),
    SongSearchItem(
        id = 16,
        title = "HBP (Remix)",
        artistStr = "Llona (feat. Bella Shmurda)",
        albumArtUrl = "",
        durationMillis = 300000
    ),
    SongSearchItem(
        id = 17,
        title = "Trees",
        artistStr = "Olivetheboy",
        albumArtUrl = "",
        durationMillis = 300000
    ),
    SongSearchItem(
        id = 18,
        title = "Worst Luck",
        artistStr = "6LACK",
        albumArtUrl = "",
        durationMillis = 300000
    ),
    SongSearchItem(
        id = 19,
        title = "Dreams",
        artistStr = "NF",
        albumArtUrl = "",
        durationMillis = 300000
    ),
)