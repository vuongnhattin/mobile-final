package com.example.mobilefinal.model

import androidx.compose.runtime.mutableStateOf
import com.example.mobilefinal.R

data class MusicItem(
    val id: Int,
    val name: String,
    val resourceId: Int,
    val is3D: Boolean = false
)

object MusicData {
    val musicList = listOf(
        MusicItem(1, "Morning", R.raw.alarm_sound, is3D = true),
        MusicItem(2, "Dusty Plain", R.raw.dusty_plain, is3D = true),
        MusicItem(3, "Funny", R.raw.funny, is3D = true),
        MusicItem(4, "Happy", R.raw.happy, is3D = true),
        MusicItem(5, "Dance", R.raw.pixel_dance, is3D = true),
        MusicItem(6, "Da Lat", R.raw.sanmay, is3D = true),
        MusicItem(7, "Song Bird", R.raw.song_bird, is3D = true),
        MusicItem(8, "The Big Adventure", R.raw.the_big_adventure, is3D = true),
    )

    var currentMusic = musicList[0]
}

