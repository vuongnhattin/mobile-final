package com.example.mobilefinal.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.mobilefinal.R

data class MusicItem(
    val id: Int,
    val name: String,
    val resourceId: Int,
    val is3D: Boolean = false
)

class MusicViewModel : ViewModel() {
    val musicList = mutableStateOf(
        listOf(
            MusicItem(1, "Chúng ta không thuộc về nhau", R.raw.chungtakhongthuocvenhau, is3D = true),
            MusicItem(2, "Ngôi đền", R.raw.alarm_sound, is3D = true)
        )
    )

    var selectedMusic = mutableStateOf<MusicItem?>(null)
}
