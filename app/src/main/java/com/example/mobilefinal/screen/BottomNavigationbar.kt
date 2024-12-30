package com.example.mobilefinal.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.key.Key.Companion.Home
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.mobilefinal.LocalNavController
import com.example.mobilefinal.R
import com.example.mobilefinal.icon.Alarm
import com.example.mobilefinal.icon.Music_note
import com.example.mobilefinal.icon.ShieldCheck
import com.example.mobilefinal.icon.Water_loss

data class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector,
)

@Composable
fun BottomNavigationBar(
) {
    val items = listOf(
        BottomNavItem("alarm", "Báo thức", Alarm),
        BottomNavItem("choose-music", "Nhạc chuông", Music_note),
        BottomNavItem("water", "Uống nước", Water_loss),
    )

    val navController = LocalNavController.current

    NavigationBar(
//        containerColor = Color.Transparent,
        contentColor = Color.Black,
    ) {
        val currentDestination = navController.currentBackStackEntryAsState().value?.destination

        items.forEachIndexed() { index, item ->
            NavigationBarItem(
                selected = currentDestination?.route == item.route,
                onClick = { navController.navigate(item.route) },
                label = { Text(item.label) },
                icon = { Icon(item.icon, contentDescription = null) },
//                colors = NavigationBarItemDefaults.colors(
//                    selectedIconColor = Color.Black,
//                    selectedTextColor = Color.Black,
//                    unselectedIconColor = Color.Gray,
//                    unselectedTextColor = Color.Gray,
//                    indicatorColor = Color.Transparent
//                )
            )
        }
    }
}