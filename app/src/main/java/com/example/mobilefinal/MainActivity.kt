package com.example.mobilefinal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.mobilefinal.screen.AlarmScreen
import com.example.mobilefinal.screen.BottomNavigationBar
import com.example.mobilefinal.screen.ChooseMusicScreen
import com.example.mobilefinal.screen.WaterScreen
import com.example.mobilefinal.ui.theme.MobileFinalTheme
import com.example.mobilefinal.viewmodel.PictureViewModel

val LocalPictureViewModel = staticCompositionLocalOf<PictureViewModel> {
    error("TakePictureViewModel is not provided")
}

val LocalNavController = staticCompositionLocalOf<NavController> {
    error("NavController is not provided")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MobileFinalTheme {
                val pictureViewModel: PictureViewModel = viewModel()
                CompositionLocalProvider(
                    LocalPictureViewModel provides pictureViewModel
                ) {
                    MyApp()
                }
            }
        }
    }
}

@Composable
fun MyApp(modifier: Modifier = Modifier) {
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
            }
        }

    }
}

