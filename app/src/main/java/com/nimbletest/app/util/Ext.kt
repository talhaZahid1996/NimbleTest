package com.nimbletest.app.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun String.isNotBlankOrEmpty(): Boolean {
    return this.isNotBlank() && this.isNotEmpty()
}

// extension function to convert date to time ago format eg: 2022-11-04 to 1 year ago
fun String.toTimeAgo(): String {
    if (this.isEmpty()) return ""
    val date = this.toLocalDate()
    val now = LocalDate.now()
    val years = now.year - date.year
    val months = now.monthValue - date.monthValue
    val days = now.dayOfMonth - date.dayOfMonth

    return when {
        years > 0 -> "$years year${if (years > 1) "s" else ""} ago"
        months > 0 -> "$months month${if (months > 1) "s" else ""} ago"
        days > 0 -> "$days day${if (days > 1) "s" else ""} ago"
        else -> "Today"
    }
}

fun String.toNamedDateFormat(): String {
    if (this.isEmpty()) return ""
    val date = this.toLocalDate()
    val pattern = DateTimeFormatter.ofPattern("EEEE, MMMM dd")

    return date.format(pattern)
}

fun String.toLocalDate(): LocalDate {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'")
    return LocalDate.parse(this, formatter)
}