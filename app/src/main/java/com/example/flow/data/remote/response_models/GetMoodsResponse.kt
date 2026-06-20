package com.example.flow.data.remote.response_models

import com.example.flow.data.models.Mood

data class MoodApi(
    val moodId: Int,
    val moodName: String,
    val durationMillis: Long,
)

fun MoodApi.toMood(): Mood = Mood(
    moodId = moodId,
    name = moodName,
    durationMs = durationMillis,
)

data class GetMoodsResponse(
    val success: Boolean,
    val moodCount: Int?,
    val moods: List<MoodApi>?,
    val debug: Map<String, String>?,
)