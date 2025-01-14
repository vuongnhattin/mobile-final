package com.example.mobilefinal.screen

import android.content.Context
import android.media.MediaPlayer
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mobilefinal.viewmodel.MusicItem
import com.example.mobilefinal.viewmodel.MusicViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChooseMusicScreen(viewModel: MusicViewModel = viewModel()) {
    val context = LocalContext.current
    val musicList by viewModel.musicList
    val selectedMusic by viewModel.selectedMusic
    val coroutineScope = rememberCoroutineScope()

    var isPlaying by remember { mutableStateOf(false) }
    var mediaPlayer: MediaPlayer? by remember { mutableStateOf(null) }
    var currentPlayingMusic by remember { mutableStateOf<MusicItem?>(null) }

    // Khôi phục bài hát đã chọn
    LaunchedEffect(Unit) {
        val savedMusicId = getSavedMusicId(context)
        if (savedMusicId != null) {
            val savedMusic = musicList.find { it.id == savedMusicId }
            if (savedMusic != null) {
                viewModel.selectedMusic.value = savedMusic
            }
        }
    }

    // Cleanup khi rời màn hình
    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer?.apply {
                stop()
                release()
            }
            mediaPlayer = null
            isPlaying = false
            currentPlayingMusic = null
        }
    }

    // Xử lý lắc điện thoại
    val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    var lastShakeTime by remember { mutableStateOf(0L) }

    DisposableEffect(sensorManager) {
        val sensorListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                event?.let {
                    val x = event.values[0]
                    val y = event.values[1]
                    val z = event.values[2]

                    val acceleration = Math.sqrt((x * x + y * y + z * z).toDouble()).toFloat()
                    val currentTime = System.currentTimeMillis()

                    if (acceleration > 12 && currentTime - lastShakeTime > 1000) {
                        lastShakeTime = currentTime
                        coroutineScope.launch(Dispatchers.Main) {
                            if (!isPlaying) {
                                // Lần lắc đầu tiên - phát bài hát đang chọn
                                selectedMusic?.let { music ->
                                    playMusic(music, context, mediaPlayer) { player, isPlayingNow ->
                                        mediaPlayer = player
                                        isPlaying = isPlayingNow
                                        currentPlayingMusic = music
                                    }
                                }
                            } else {
                                // Lần lắc tiếp theo - chuyển và chọn bài tiếp theo
                                val nextMusic = getNextMusic(musicList, currentPlayingMusic)
                                viewModel.selectedMusic.value = nextMusic // Chọn bài tiếp theo
                                saveSelectedMusic(context, nextMusic.id) // Lưu lựa chọn
                                playMusic(nextMusic, context, mediaPlayer) { player, isPlayingNow ->
                                    mediaPlayer = player
                                    isPlaying = isPlayingNow
                                    currentPlayingMusic = nextMusic
                                }
                            }
                        }
                    }
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }
        sensorManager.registerListener(sensorListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
        onDispose {
            sensorManager.unregisterListener(sensorListener)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Nhạc chuông") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Hiển thị thông tin bài hát đang phát và nút điều khiển
            if (isPlaying && currentPlayingMusic != null) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Đang phát: ${currentPlayingMusic?.name}",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Button(
                            onClick = {
                                mediaPlayer?.apply {
                                    stop()
                                    release()
                                }
                                mediaPlayer = null
                                isPlaying = false
                                currentPlayingMusic = null
                            },
                            modifier = Modifier.padding(top = 8.dp)
                        ) {
                            Text("Dừng phát")
                        }
                    }
                }
            }

            // Danh sách nhạc
            musicList.forEach { music ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            // Chọn và phát nhạc khi click
                            viewModel.selectedMusic.value = music
                            saveSelectedMusic(context, music.id)
                            playMusic(music, context, mediaPlayer) { player, isPlayingNow ->
                                mediaPlayer = player
                                isPlaying = isPlayingNow
                                currentPlayingMusic = music
                            }
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
                        onClick = null
                    )
                }
                Divider()
            }
        }
    }
}

private fun playMusic(
    music: MusicItem?,
    context: Context,
    currentMediaPlayer: MediaPlayer?,
    onMusicStateChanged: (MediaPlayer?, Boolean) -> Unit
) {
    music?.let {
        currentMediaPlayer?.apply {
            stop()
            release()
        }
        val newMediaPlayer = MediaPlayer.create(context, it.resourceId).apply {
            start()
        }
        onMusicStateChanged(newMediaPlayer, true)
    }
}

private fun getNextMusic(musicList: List<MusicItem>, currentMusic: MusicItem?): MusicItem {
    val currentIndex = musicList.indexOf(currentMusic)
    return musicList[(currentIndex + 1) % musicList.size]
}

// Lưu id bài hát đã chọn vào SharedPreferences
private fun saveSelectedMusic(context: Context, musicId: Int) {
    val sharedPreferences = context.getSharedPreferences("music_prefs", Context.MODE_PRIVATE)
    sharedPreferences.edit().putInt("selected_music_id", musicId).apply()
}

// Lấy id bài hát đã lưu từ SharedPreferences
private fun getSavedMusicId(context: Context): Int? {
    val sharedPreferences = context.getSharedPreferences("music_prefs", Context.MODE_PRIVATE)
    return if (sharedPreferences.contains("selected_music_id")) {
        sharedPreferences.getInt("selected_music_id", -1).takeIf { it != -1 }
    } else null
}