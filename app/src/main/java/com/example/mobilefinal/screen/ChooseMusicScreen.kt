package com.example.mobilefinal.screen

import android.content.Context
import android.media.MediaPlayer
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mobilefinal.LocalMusicViewModel
import com.example.mobilefinal.icon.MusicNote
import com.example.mobilefinal.icon.PauseCircle
import com.example.mobilefinal.icon.Smartphone
import com.example.mobilefinal.model.MusicData
import com.example.mobilefinal.model.MusicItem
import com.example.mobilefinal.viewmodel.MusicViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChooseMusicScreen() {
    val viewModel = LocalMusicViewModel.current
    val context = LocalContext.current
    val selectedMusic = viewModel.selectedMusic
    val coroutineScope = rememberCoroutineScope()
    val musicList = MusicData.musicList

    var isPlaying by remember { mutableStateOf(false) }
    var mediaPlayer: MediaPlayer? by remember { mutableStateOf(null) }
    var currentPlayingMusic by remember { mutableStateOf<MusicItem?>(null) }

    val lazyListState = rememberLazyListState() // Thêm LazyListState

    LaunchedEffect(Unit) {
        val savedMusicId = getSavedMusicId(context)
        if (savedMusicId != null) {
            val savedMusic = musicList.find { it.id == savedMusicId }
            if (savedMusic != null) {
                viewModel.setMusic(savedMusic)
                val index = musicList.indexOf(savedMusic)
                lazyListState.scrollToItem(index)
            }
        }
    }

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
                                viewModel.setMusic(nextMusic) // Chọn bài tiếp theo
                                saveSelectedMusic(context, nextMusic.id) // Lưu lựa chọn
                                playMusic(nextMusic, context, mediaPlayer) { player, isPlayingNow ->
                                    mediaPlayer = player
                                    isPlaying = isPlayingNow
                                    currentPlayingMusic = nextMusic
                                }

                                val index = musicList.indexOf(nextMusic)
                                lazyListState.animateScrollToItem(index)
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
            CenterAlignedTopAppBar(
                title = { Text("Music Player",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                ) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Player Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .padding(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    // Hướng dẫn gesture
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        Icon(
                            imageVector = Smartphone,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = if (isPlaying) "Lắc để chuyển bài" else "Lắc để phát nhạc",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }

                    // Thông tin bài hát
                    if (isPlaying && currentPlayingMusic != null) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Đang phát",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = currentPlayingMusic?.name ?: "",
                                style = MaterialTheme.typography.titleMedium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }
                    }

                    // Nút điều khiển
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        if (isPlaying) {
                            FilledTonalIconButton(
                                onClick = {
                                    mediaPlayer?.apply {
                                        stop()
                                        release()
                                    }
                                    mediaPlayer = null
                                    isPlaying = false
                                    currentPlayingMusic = null
                                }
                            ) {
                                Icon(
                                    imageVector = PauseCircle,
                                    contentDescription = "Dừng phát",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        } else {
                            FilledTonalIconButton(
                                onClick = {
                                    selectedMusic?.let { music ->
                                        playMusic(music, context, mediaPlayer) { player, isPlayingNow ->
                                            mediaPlayer = player
                                            isPlaying = isPlayingNow
                                            currentPlayingMusic = music
                                        }
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = "Phát nhạc",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }

            // Tiêu đề danh sách
            Text(
                text = "Danh sách nhạc",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                color = MaterialTheme.colorScheme.primary
            )

            // Danh sách nhạc
            LazyColumn(
                state = lazyListState,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            ) {
                items(musicList) { music ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp, horizontal = 8.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .clickable {
                                viewModel.setMusic(music)
                                saveSelectedMusic(context, music.id)
                                playMusic(music, context, mediaPlayer) { player, isPlayingNow ->
                                    mediaPlayer = player
                                    isPlaying = isPlayingNow
                                    currentPlayingMusic = music
                                }
                            },
                        colors = CardDefaults.cardColors(
                            containerColor = if (selectedMusic?.id == music.id)
                                MaterialTheme.colorScheme.primaryContainer
                            else
                                MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                if (currentPlayingMusic?.id == music.id) {
                                    Icon(
                                        imageVector = MusicNote,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.padding(end = 12.dp)
                                    )
                                }

                                Column {
                                    Text(
                                        text = music.name,
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = if (selectedMusic?.id == music.id)
                                            MaterialTheme.colorScheme.primary
                                        else
                                            MaterialTheme.colorScheme.onSurface
                                    )
                                    if (music.is3D) {
                                        Text(
                                            text = "3D Audio",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.padding(top = 4.dp)
                                        )
                                    }
                                }
                            }

                            RadioButton(
                                selected = selectedMusic?.id == music.id,
                                onClick = null,
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = MaterialTheme.colorScheme.primary
                                )
                            )
                        }
                    }
                }
                // Thêm padding cuối cùng
                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    }
}

// Các hàm utility giữ nguyên

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

fun getNextMusic(musicList: List<MusicItem>, currentMusic: MusicItem?): MusicItem {
    val currentIndex = musicList.indexOf(currentMusic)
    return musicList[(currentIndex + 1) % musicList.size]
}

// Lưu id bài hát đã chọn vào SharedPreferences
fun saveSelectedMusic(context: Context, musicId: Int) {
    val sharedPreferences = context.getSharedPreferences("music_prefs", Context.MODE_PRIVATE)
    sharedPreferences.edit().putInt("selected_music_id", musicId).apply()
}

// Lấy id bài hát đã lưu từ SharedPreferences
fun getSavedMusicId(context: Context): Int? {
    val sharedPreferences = context.getSharedPreferences("music_prefs", Context.MODE_PRIVATE)
    return if (sharedPreferences.contains("selected_music_id")) {
        sharedPreferences.getInt("selected_music_id", MusicData.musicList[0].resourceId).takeIf { it != -1 }
    } else null
}