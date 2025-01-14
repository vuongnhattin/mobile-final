package com.example.mobilefinal.screen

import android.media.MediaPlayer
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mobilefinal.viewmodel.MusicViewModel

@OptIn(ExperimentalMaterial3Api::class) // Xử lý cảnh báo Material API Experimental
@Composable
fun ChooseMusicScreen(viewModel: MusicViewModel = viewModel()) {
    val musicList by viewModel.musicList
    val selectedMusic by viewModel.selectedMusic

    // Lấy context trong ngữ cảnh @Composable
    val context = LocalContext.current

    // Quản lý MediaPlayer state
    var mediaPlayer: MediaPlayer? by remember { mutableStateOf(null) }

    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer?.release() // Giải phóng MediaPlayer khi màn hình bị huỷ
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nhạc chuông") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            musicList.forEach { music ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            viewModel.selectedMusic.value = music
                            mediaPlayer?.release() // Giải phóng MediaPlayer cũ
                            mediaPlayer = MediaPlayer.create(context, music.resourceId) // Tạo MediaPlayer mới
                            mediaPlayer?.start() // Phát nhạc
                        }
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = music.name,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.weight(1f)
                    )
                    if (music.is3D) {
                        Text(
                            text = "3D",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                    }
                    RadioButton(
                        selected = selectedMusic?.id == music.id,
                        onClick = null // Xử lý click từ Row
                    )
                }
                Divider() // Thêm đường kẻ phân cách
            }
        }
    }
}
