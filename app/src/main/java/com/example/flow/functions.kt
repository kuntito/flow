package com.example.flow

import com.example.flow.data.models.Song


/**
 * formats remaining milliseconds as a short time.
 *
 * under an hour, it's minute words: "one", "two", ... "thirty".
 * past thirty minutes, the number: "45".
 * under a minute, "<1".
 *
 * from an hour up, it's short form, combining what's left:
 * "2h", "2h 15m", "1d", "1d 3h", "1d 3h 20m".
 * parts that are zero are left out, so 2 hours flat is just "2h".
 */
fun formatRemainingTime(remainingMs: Long): String {
    val totalMinutes = (remainingMs / 60_000).toInt()
    if (totalMinutes < 1) return "<1"

    val minutesPerDay = 24 * 60

    val days = totalMinutes / minutesPerDay
    val hours = (totalMinutes % minutesPerDay) / 60
    val minutes = totalMinutes % 60

    val isUnderAnHour = days == 0 && hours == 0
    if (isUnderAnHour) {
        val words = listOf(
            "one", "two", "three", "four", "five",
            "six", "seven", "eight", "nine", "ten",
            "eleven", "twelve", "thirteen", "fourteen", "fifteen",
            "sixteen", "seventeen", "eighteen", "nineteen", "twenty",
            "twenty one", "twenty two", "twenty three", "twenty four",
            "twenty five", "twenty six", "twenty seven", "twenty eight",
            "twenty nine", "thirty"
        )

        return words.getOrElse(minutes - 1) {
            minutes.toString()
        }
    }

    val parts = buildList {
        if (days > 0) add("${days}d")
        if (hours > 0) add("${hours}h")
        if (minutes > 0) add("${minutes}m")
    }

    return parts.joinToString(" ")
}
