package com.example.mobilefinal.screen

import android.app.DatePickerDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.app.TimePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationCompat
import com.example.mobilefinal.LocalAlarmViewModel
import com.example.mobilefinal.LocalNavController
import com.example.mobilefinal.LocalWaterViewModel
import com.example.mobilefinal.R
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun AlarmScreen() {
    val alarmViewModel = LocalAlarmViewModel.current
    val hours = alarmViewModel.hours
    val minutes = alarmViewModel.minutes
    val day = alarmViewModel.day
    val month = alarmViewModel.month
    val year = alarmViewModel.year
    val timerStarted = alarmViewModel.timerStarted

    val context = LocalContext.current
    val navController = LocalNavController.current
    val scope = rememberCoroutineScope()
    val calendar = Calendar.getInstance()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
//        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .weight(70f),
            verticalArrangement = Arrangement.Center,
        ) {
            Image(
                painterResource(R.drawable.alarm_clock),
                contentDescription = null,
                modifier = Modifier.size(150.dp)
            )
        }
        Column(
            modifier = Modifier
                .weight(100f)
        ) {
            CustomCard() {
                val color = if (timerStarted) Color.Black else Color.Black
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        String.format("%02d:%02d", hours, minutes),
                        style = MaterialTheme.typography.headlineLarge.copy(color = color)
                            .copy(fontWeight = FontWeight.ExtraBold)
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            String.format("%02d/%02d", day, month + 1),
                            style = MaterialTheme.typography.titleMedium.copy(color = color)
                                .copy(fontWeight = FontWeight.ExtraBold)
                        )
                        Spacer(modifier = Modifier.width(20.dp))
                        Switch(
                            checked = timerStarted,
                            onCheckedChange = {
                                if (it) {
                                    alarmViewModel.startTimer()
                                } else {
                                    alarmViewModel.stopTimer()
                                }
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Time Picker Button
                    Button(
                        onClick = {
                            TimePickerDialog(
                                context,
                                { _, hourOfDay, minute ->
                                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                                    calendar.set(Calendar.MINUTE, minute)
                                    alarmViewModel.setTime(hourOfDay, minute)
                                },
                                calendar.get(Calendar.HOUR_OF_DAY),
                                calendar.get(Calendar.MINUTE),
                                false
                            ).show()
                        },
                        enabled = !timerStarted
                    ) {
                        Text("Chọn giờ")
                    }

                    Button(
                        onClick = {
                            DatePickerDialog(
                                context,
                                { _, year, month, dayOfMonth ->
                                    println("dayOfMonth: $dayOfMonth, month: $month, year: $year")
                                    calendar.set(Calendar.YEAR, year)
                                    calendar.set(Calendar.MONTH, month)
                                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                                    println("Calendar.DAY_OF_MONTH: ${calendar.get(Calendar.DAY_OF_MONTH)}")
                                    alarmViewModel.setDate(
                                        calendar.get(Calendar.YEAR),
                                        calendar.get(Calendar.MONTH),
                                        calendar.get(Calendar.DAY_OF_MONTH),
                                    )
                                },
                                calendar.get(Calendar.YEAR),
                                calendar.get(Calendar.MONTH),
                                calendar.get(Calendar.DAY_OF_MONTH)
                            ).show()
                        },
                        enabled = !timerStarted
                    ) {
                        Text("Chọn ngày")
                    }
                }
            }
        }


    }
}

class AlarmService : Service() {
    private var mediaPlayer: MediaPlayer? = null
    private val notificationChannelId = "alarm_service_channel"
    private val notificationId = 2

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


