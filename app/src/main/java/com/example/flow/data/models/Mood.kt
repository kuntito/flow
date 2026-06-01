package com.example.flow.data.models

data class Mood(
    val tagId: Int,
    val name: String,
    val durationMs: Long,
)

val dummyMoodItem = Mood(
    tagId = 1,
    name = "mist",
    durationMs = 100000,
)

val dummyMoodList = listOf(
    Mood(
        tagId = 1,
        name = "mist",
        durationMs = 100000,
    ),
    Mood(
        tagId = 2,
        name = "clouds",
        durationMs = 100000,
    ),
    Mood(
        tagId = 3,
        name = "234",
        durationMs = 100000,
    ),
    Mood(
        tagId = 4,
        name = "gym",
        durationMs = 100000,
    ),
    Mood(
        tagId = 5,
        name = "sad",
        durationMs = 100000,
    ),
)