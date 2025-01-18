package com.example.mobilefinal.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun DropdownMenuIcon(
    icon: ImageVector = Icons.Default.Settings,
    items: List<String>,
    onItemClick: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        IconButton(onClick = { expanded = true }) {
            Icon(
                imageVector = icon,
                contentDescription = "More options"
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = { Text(text = item) },
                    onClick = {
                        onItemClick(item)
                        expanded = false
                    }
                )
            }
        }
    }
}