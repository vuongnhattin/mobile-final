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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
    val calendar =  Calendar.getInstance()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
//        TimePicker(
//            hour = hours,
//            minute = minutes,
//            second = seconds,
//            onTimeChanged = { newHour, newMinute, newSecond ->
//                alarmViewModel.setTime(newHour ?: 0, newMinute ?: 0, newSecond ?: 0)
//            },
//            enabled = !timerStarted
//        )

        Text(
            text = "Alarm set for: $day/${month + 1}/$year at $hours:$minutes",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(32.dp))


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
                    true
                ).show()
            },
            enabled = !timerStarted
        ) {
            Text("Chọn giờ")
        }


        Button(
            onClick = {
                if (!timerStarted) {
                    alarmViewModel.startTimer()
                }
            },
            enabled = !timerStarted && (hours > 0 || minutes > 0)
        ) {
            Text("Đặt báo thức")
        }
        Button(
            onClick = {
                alarmViewModel.stopTimer()
            },
            enabled = timerStarted
        ) {
            Text("Huỷ báo thức")
        }
        Button(
            onClick = {
                navController.navigate("quiz")
            },
            enabled = timerStarted
        ) {
            Text("Tắt chuông")
        }
    }
}

class AlarmService : Service() {
    private lateinit var mediaPlayer: MediaPlayer
    private val notificationChannelId = "alarm_service_channel"
    private val notificationId = 2

    override fun onCreate() {
        super.onCreate()

        // Initialize media player
        mediaPlayer = MediaPlayer.create(this, R.raw.alarm_sound) // Replace with your audio file
        mediaPlayer.isLooping = true

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
        mediaPlayer.start()
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.stop()
        mediaPlayer.release()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}


