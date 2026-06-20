package com.example.flow.data.models

data class Mood(
    val moodId: Int,
    val name: String,
    val durationMs: Long,
)

val dummyMoodItem = Mood(
    moodId = 1,
    name = "mist",
    durationMs = 100000,
)

val dummyMoodList = listOf(
    Mood(
        moodId = 1,
        name = "mist",
        durationMs = 100000,
    ),
    Mood(
        moodId = 2,
        name = "clouds",
        durationMs = 100000,
    ),
    Mood(
        moodId = 3,
        name = "234",
        durationMs = 100000,
    ),
    Mood(
        moodId = 4,
        name = "gym",
        durationMs = 100000,
    ),
    Mood(
        moodId = 5,
        name = "sad",
        durationMs = 100000,
    ),
)