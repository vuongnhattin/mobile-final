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
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.mobilefinal.model.MusicData
import com.example.mobilefinal.model.MusicData.musicList
import com.example.mobilefinal.screen.AlarmScreen
import com.example.mobilefinal.screen.AlarmService
import com.example.mobilefinal.screen.BottomNavigationBar
import com.example.mobilefinal.screen.ChooseMusicScreen
import com.example.mobilefinal.screen.MusicService
import com.example.mobilefinal.screen.QuizScreen
import com.example.mobilefinal.screen.WaterScreen
import com.example.mobilefinal.screen.WaterTakePictureScreen
import com.example.mobilefinal.screen.getSavedMusicId
import com.example.mobilefinal.ui.theme.MobileFinalTheme
import com.example.mobilefinal.viewmodel.AlarmViewModel
import com.example.mobilefinal.viewmodel.MusicViewModel
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

val LocalMusicViewModel = staticCompositionLocalOf<MusicViewModel> {
    error("MusicViewModel is not provided")
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
                    LocalQuizViewModel provides QuizViewModel(),
                    LocalMusicViewModel provides MusicViewModel()
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
    val navController = rememberNavController()
    val waterViewModel = LocalWaterViewModel.current
    val musicViewModel = LocalMusicViewModel.current
    val context = LocalContext.current
    val hours = waterViewModel.hours
    val minutes = waterViewModel.minutes
    val seconds = waterViewModel.seconds
    val timerStarted = waterViewModel.timerStarted
    var showDialog by remember { mutableStateOf(waterViewModel.ringing) }

    LaunchedEffect(waterViewModel.timerStarted) {
        if (timerStarted && !waterViewModel.ringing && (hours > 0 || minutes > 0 || seconds > 0)) {
            val totalMillis = (hours * 3600 + minutes * 60 + seconds) * 1000L
            delay(totalMillis)
            waterViewModel.startRinging()
            showDialog = true
            val intent = Intent(context, MusicService::class.java).apply {
                putExtra(
                    "MUSIC_RESOURCE_ID",
                    musicViewModel.selectedMusic?.resourceId ?: -1
                ) // Replace with the desired resource ID
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
            },
            title = {
                Text("Đã đến giờ uống nước")
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(painterResource(R.drawable.water), contentDescription = "Warning")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Hãy chụp một tấm hình để chứng minh bạn đã uống nước!",
                        textAlign = TextAlign.Center)
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        navController.navigate("take-picture")
                        showDialog = false
                    }
                ) {
                    Text("Let's go!")
                }
            }
        )
    }

    val alarmViewModel = LocalAlarmViewModel.current
    val hoursAlarm = alarmViewModel.hours
    val minutesAlarm = alarmViewModel.minutes
    val day = alarmViewModel.day
    val month = alarmViewModel.month
    val year = alarmViewModel.year
    val timerStartedAlarm = alarmViewModel.timerStarted
    var showDialogAlarm by remember { mutableStateOf(alarmViewModel.ringing) }
    LaunchedEffect(alarmViewModel.timerStarted) {
        if (timerStartedAlarm && !alarmViewModel.ringing) {
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
            alarmViewModel.startRinging()
            showDialogAlarm = true
            val intent = Intent(context, AlarmService::class.java).apply {
                putExtra(
                    "MUSIC_RESOURCE_ID",
                    musicViewModel.selectedMusic?.resourceId ?: -1
                ) // Replace with the desired resource ID
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }
    }

    if (showDialogAlarm) {
        AlertDialog(
            onDismissRequest = {
            },
            title = {
                Text("Đã đến giờ")
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(painterResource(R.drawable.warning), contentDescription = "Warning")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Hãy giải đố để tắt báo thức nào!")
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        navController.navigate("quiz")
                        showDialogAlarm = false
                    }
                ) {
                    Text("Let's go!")
                }
            }
        )
    }
    val musicList = MusicData.musicList
    LaunchedEffect(Unit) {
        val savedMusicId = getSavedMusicId(context) ?: musicList.first().id
        val savedMusic = musicList.find { it.id == savedMusicId }
        if (savedMusic != null) {
            musicViewModel.setMusic(savedMusic)
        }
    }

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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(246, 246, 248))
            ) {
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
}

