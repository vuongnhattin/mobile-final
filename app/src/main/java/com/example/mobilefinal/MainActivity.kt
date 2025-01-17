package com.example.mobilefinal

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.mobilefinal.screen.AlarmScreen
import com.example.mobilefinal.screen.AlarmService
import com.example.mobilefinal.screen.BottomNavigationBar
import com.example.mobilefinal.screen.ChooseMusicScreen
import com.example.mobilefinal.screen.MusicService
import com.example.mobilefinal.screen.QuizScreen
import com.example.mobilefinal.screen.WaterScreen
import com.example.mobilefinal.screen.WaterTakePictureScreen
import com.example.mobilefinal.ui.theme.MobileFinalTheme
import com.example.mobilefinal.util.convertToMillis
import com.example.mobilefinal.viewmodel.AlarmViewModel
import com.example.mobilefinal.viewmodel.PictureViewModel
import com.example.mobilefinal.viewmodel.QuizViewModel
import com.example.mobilefinal.viewmodel.WaterViewModel
import kotlinx.coroutines.delay
import java.util.Calendar

val LocalPictureViewModel = staticCompositionLocalOf<PictureViewModel> {
    error("TakePictureViewModel is not provided")
}

val LocalNavController = staticCompositionLocalOf<NavController> {
    error("NavController is not provided")
}

val LocalWaterViewModel = staticCompositionLocalOf<WaterViewModel> {
    error("WaterViewModel is not provided")
}

val LocalAlarmViewModel = staticCompositionLocalOf<AlarmViewModel> {
    error("AlarmViewModel is not provided")
}

val LocalQuizViewModel = staticCompositionLocalOf<QuizViewModel> {
    error("QuizViewModel is not provided")
}

class MainActivity : ComponentActivity() {


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MobileFinalTheme {
                CompositionLocalProvider(
                    LocalPictureViewModel provides viewModel(),
                    LocalWaterViewModel provides WaterViewModel(LocalContext.current),
                    LocalAlarmViewModel provides AlarmViewModel(LocalContext.current),
                    LocalQuizViewModel provides QuizViewModel()
                ) {
                    MyApp()
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MyApp(modifier: Modifier = Modifier) {
    val waterViewModel = LocalWaterViewModel.current
    val context = LocalContext.current
    val hours = waterViewModel.hours
    val minutes = waterViewModel.minutes
    val seconds = waterViewModel.seconds
    val timerStarted = waterViewModel.timerStarted
    LaunchedEffect(waterViewModel.timerStarted) {
        if (timerStarted && (hours > 0 || minutes > 0 || seconds > 0)) {
            val totalMillis = (hours * 3600 + minutes * 60 + seconds) * 1000L
            delay(totalMillis)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(Intent(context, MusicService::class.java))
            } else {
                context.startService(Intent(context, MusicService::class.java))
            }
        }
    }

    val alarmViewModel = LocalAlarmViewModel.current
    val hoursAlarm = alarmViewModel.hours
    val minutesAlarm = alarmViewModel.minutes
    val day = alarmViewModel.day
    val month = alarmViewModel.month
    val year = alarmViewModel.year
    val timerStartedAlarm = alarmViewModel.timerStarted
    LaunchedEffect(alarmViewModel.timerStarted) {
        if (timerStartedAlarm) {
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month) // Calendar.MONTH is zero-based
            calendar.set(Calendar.DAY_OF_MONTH, day)
            calendar.set(Calendar.HOUR_OF_DAY, hoursAlarm)
            calendar.set(Calendar.MINUTE, minutesAlarm)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)

            val currentTime = Calendar.getInstance().timeInMillis
            val alarmTime = calendar.timeInMillis
//            val alarmTime = convertToMillis(year, month, day, hoursAlarm, minutesAlarm)
            println("Alarm time: $alarmTime")
            println("Current time: $currentTime")
            if (alarmTime >= currentTime) {
                val delayMillis = alarmTime - currentTime
                delay(delayMillis)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(Intent(context, AlarmService::class.java))
            } else {
                context.startService(Intent(context, AlarmService::class.java))
            }
        }
    }

    val navController = rememberNavController()

    val screensWithBottomNav = listOf("alarm", "choose-music", "water")

    val currentBackStackEntry = navController.currentBackStackEntryAsState().value
    val currentRoute = currentBackStackEntry?.destination?.route

    CompositionLocalProvider(LocalNavController provides navController) {
        Scaffold(
            bottomBar = {
                if (currentRoute in screensWithBottomNav) {
                    BottomNavigationBar()
                }
            },
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = "alarm",
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None },
                modifier = if (currentRoute in screensWithBottomNav) {
                    Modifier.padding(innerPadding)
                } else {
                    Modifier
                }
            ) {
                composable("alarm") { AlarmScreen() }
                composable("choose-music") { ChooseMusicScreen() }
                composable("water") { WaterScreen() }
                composable("take-picture") { WaterTakePictureScreen() }
                composable("quiz") { QuizScreen() }
            }
        }
    }
}

