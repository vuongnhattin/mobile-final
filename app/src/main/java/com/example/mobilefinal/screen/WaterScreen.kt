package com.example.mobilefinal.screen

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationCompat
import com.example.mobilefinal.LocalNavController
import com.example.mobilefinal.LocalWaterViewModel
import com.example.mobilefinal.R
import com.example.mobilefinal.model.MusicData

@Composable
fun TimePicker(
    hour: Int?,
    minute: Int?,
    second: Int?,
    onTimeChanged: (Int?, Int?, Int?) -> Unit,
    enabled: Boolean
) {
    val hourState = remember { mutableStateOf(hour?.toString() ?: "") }
    val minuteState = remember { mutableStateOf(minute?.toString() ?: "") }
    val secondState = remember { mutableStateOf(second?.toString() ?: "") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        NumberInput(
            value = hourState.value,
            onValueChange = { newValue ->
                hourState.value = newValue
                onTimeChanged(
                    newValue.toIntOrNull(),
                    minuteState.value.toIntOrNull(),
                    secondState.value.toIntOrNull()
                )
            },
            label = "Giờ",
            enabled = enabled
        )
        Spacer(modifier = Modifier.width(8.dp))
        NumberInput(
            value = minuteState.value,
            onValueChange = { newValue ->
                minuteState.value = newValue
                onTimeChanged(
                    hourState.value.toIntOrNull(),
                    newValue.toIntOrNull(),
                    secondState.value.toIntOrNull()
                )
            },
            label = "Phút",
            enabled = enabled
        )
        Spacer(modifier = Modifier.width(8.dp))
        NumberInput(
            value = secondState.value,
            onValueChange = { newValue ->
                secondState.value = newValue
                onTimeChanged(
                    hourState.value.toIntOrNull(),
                    minuteState.value.toIntOrNull(),
                    newValue.toIntOrNull()
                )
            },
            label = "Giây",
            enabled = enabled
        )
    }
}


@Composable
fun NumberInput(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    enabled: Boolean
) {
    OutlinedTextField(
        value = value,
        onValueChange = {
            onValueChange(it)
        },
        label = { Text(label) },
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        enabled = enabled,  // Disable the field if timerStarted is true
        modifier = Modifier.width(80.dp)
    )
}


@Composable
fun WaterScreen() {
    val waterViewModel = LocalWaterViewModel.current
    val hours = waterViewModel.hours
    val minutes = waterViewModel.minutes
    val seconds = waterViewModel.seconds
    val timerStarted = waterViewModel.timerStarted

    val context = LocalContext.current
    val navController = LocalNavController.current
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painterResource(R.drawable.cup),
            contentDescription = "Cup",
            modifier = Modifier
                .padding(top = 32.dp)
        )
        OutlinedCard() {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TimePicker(
                    hour = hours,
                    minute = minutes,
                    second = seconds,
                    onTimeChanged = { newHour, newMinute, newSecond ->
                        waterViewModel.setTime(newHour ?: 0, newMinute ?: 0, newSecond ?: 0)
                    },
                    enabled = !timerStarted
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text("Bật nhắc nhở",
                        style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.width(20.dp))
                    Switch(
                        checked = timerStarted,
                        onCheckedChange = {
                            if (it) {
                                waterViewModel.startTimer()
                            } else {
                                waterViewModel.stopTimer()
                            }
                        },
                        enabled = (hours > 0 || minutes > 0 || seconds > 0
                                )
                    ) }
            }
        }
        CustomCard {
            Text("Bạn sẽ được nhắc nhở uống nước sau mỗi khoảng thời gian bạn đặt",
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
        }

    }
}


class MusicService : Service() {
    private var mediaPlayer: MediaPlayer? = null
    private val notificationChannelId = "music_service_channel"
    private val notificationId = 1

    override fun onCreate() {
        super.onCreate()

        // Create notification channel (required for Android 8.0 and higher)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                notificationChannelId,
                "Music Service",
                NotificationManager.IMPORTANCE_LOW
            )
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }

        // Create the notification to show the user that the service is running
        val notification = NotificationCompat.Builder(this, notificationChannelId)
            .setContentTitle("Music Playing")
            .setContentText("The timer is up, and music is playing.")
            .setSmallIcon(R.drawable.ic_launcher_background) // Replace with an appropriate icon
            .build()

        // Start the service in the foreground
        startForeground(notificationId, notification)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val musicResourceId = intent?.getIntExtra("MUSIC_RESOURCE_ID", -1)

        if (musicResourceId != null && musicResourceId != -1) {
            playNewMusic(musicResourceId)
        }
        return START_STICKY
    }

    private fun playNewMusic(resourceId: Int) {
        // Stop and release the current media player if it's playing
        mediaPlayer?.apply {
            if (isPlaying) stop()
            release()
        }

        // Initialize a new media player with the new music
        mediaPlayer = MediaPlayer.create(this, resourceId).apply {
            isLooping = true
            start()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.apply {
            stop()
            release()
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}


@Composable
fun NumberPicker(
    value: Int,
    onValueChange: (Int) -> Unit,
    range: IntRange,
    label: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            IconButton(onClick = {
                if (value > range.first) onValueChange(value - 1)
            }) {
                Icon(Icons.Default.KeyboardArrowDown, "Decrease")
            }
            Text(
                text = value.toString().padStart(2, '0'),
                style = MaterialTheme.typography.headlineMedium
            )
            IconButton(onClick = {
                if (value < range.last) onValueChange(value + 1)
            }) {
                Icon(Icons.Default.KeyboardArrowUp, "Increase")
            }
        }
    }
}


