package com.example.flow.data.remote.response_models

// TODO add JSON mappers, android field names might differ from API field names

/**
 * API response for searched song.
 */
data class SongSearchItem(
    val id: Int,
    val title: String,
    val artistStr: String,
    val albumArtUrl: String,
)

data class SearchSongResponse(
    val success: Boolean,
    val searchResults: List<SongSearchItem>? = null,
    val debug: Map<String, String>? = null
)

val dummySongSearchItem = SongSearchItem(
    id = 0,
    title = "Monica Lewinsky",
    artistStr = "SAINt JHN",
    albumArtUrl = "",
)

val dummySearchResults = listOf(
    SongSearchItem(
        id = 0,
        title = "Monica Lewinsky",
        artistStr = "SAINt JHN",
        albumArtUrl = "",
    ),
    SongSearchItem(
        id = 1,
        title = "Switched Up",
        artistStr = "Nasty C",
        albumArtUrl = "",
    ),
    SongSearchItem(
        id = 2,
        title = "Storage",
        artistStr = "Conor Maynard",
        albumArtUrl = "",
    ),
    SongSearchItem(
        id = 3,
        title = "Understand",
        artistStr = "Omah Lay",
        albumArtUrl = "",
    ),
    SongSearchItem(
        id = 4,
        title = "Waka Jeje",
        artistStr = "BNXN (feat. Majeeed)",
        albumArtUrl = "",
    ),
    SongSearchItem(
        id = 5,
        title = "Naira Marley",
        artistStr = "Zinoleesky",
        albumArtUrl = "",
    ),
    SongSearchItem(
        id = 6,
        title = "Champion",
        artistStr = "Elina",
        albumArtUrl = "",
    ),
    SongSearchItem(
        id = 7,
        title = "Again",
        artistStr = "Sasha Sloan",
        albumArtUrl = "",
    ),
    SongSearchItem(
        id = 8,
        title = "smiling when i die",
        artistStr = "Sasha Sloan",
        albumArtUrl = "",
    ),
    SongSearchItem(
        id = 9,
        title = "Dealer",
        artistStr = "Ayo Maff (feat. FireboyDML)",
        albumArtUrl = "",
    ),
    SongSearchItem(
        id = 10,
        title = "365 Days",
        artistStr = "Tml Vibez",
        albumArtUrl = "",
    ),
    SongSearchItem(
        id = 11,
        title = "Design",
        artistStr = "Olivetheboy",
        albumArtUrl = "",
    ),
    SongSearchItem(
        id = 12,
        title = "Rara",
        artistStr = "Tml Vibez",
        albumArtUrl = "",
    ),
    SongSearchItem(
        id = 13,
        title = "Fall Back",
        artistStr = "Lithe",
        albumArtUrl = "",
    ),
    SongSearchItem(
        id = 14,
        title = "Can't Breathe",
        artistStr = "Llona",
        albumArtUrl = "",
    ),
    SongSearchItem(
        id = 15,
        title = "23",
        artistStr = "Burna Boy",
        albumArtUrl = "",
    ),
    SongSearchItem(
        id = 16,
        title = "HBP (Remix)",
        artistStr = "Llona (feat. Bella Shmurda)",
        albumArtUrl = "",
    ),
    SongSearchItem(
        id = 17,
        title = "Trees",
        artistStr = "Olivetheboy",
        albumArtUrl = "",
    ),
    SongSearchItem(
        id = 18,
        title = "Worst Luck",
        artistStr = "6LACK",
        albumArtUrl = "",
    ),
    SongSearchItem(
        id = 19,
        title = "Dreams",
        artistStr = "NF",
        albumArtUrl = "",
    ),
)