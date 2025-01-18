package com.example.mobilefinal.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.ZoneOffset

@RequiresApi(Build.VERSION_CODES.O)
fun convertToMillis(year: Int, month: Int, day: Int, hour: Int, minute: Int): Long {
    val dateTime = LocalDateTime.of(year, month + 1, day, hour, minute)
    return dateTime.toInstant(ZoneOffset.UTC).toEpochMilli()
}