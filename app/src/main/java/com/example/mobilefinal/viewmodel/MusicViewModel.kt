package com.example.mobilefinal.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.mobilefinal.model.MusicData
import com.example.mobilefinal.model.MusicItem


class MusicViewModel : ViewModel() {
    var selectedMusic by mutableStateOf<MusicItem?>(null)
        private set

    fun setMusic(musicItem: MusicItem) {
        selectedMusic = musicItem
        MusicData.currentMusic = musicItem
    }
}
