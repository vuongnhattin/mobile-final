package com.example.mobilefinal.screen

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.mobilefinal.LocalNavController
import com.example.mobilefinal.LocalPictureViewModel
import com.example.mobilefinal.LocalWaterViewModel
import com.example.mobilefinal.api.ApiState
import com.example.mobilefinal.icon.BellSlash
import com.example.mobilefinal.icon.Photo_camera
import com.example.mobilefinal.icon.ShieldCheck
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WaterTakePictureScreen() {
    val threshold = 0.5f

    val context = LocalContext.current
    val navController = LocalNavController.current
    val scope = rememberCoroutineScope()
    val waterViewModel = LocalWaterViewModel.current
    val hours = waterViewModel.hours
    val minutes = waterViewModel.minutes
    val seconds = waterViewModel.seconds

    val takePictureViewModel = LocalPictureViewModel.current
    val imageBitmap = takePictureViewModel.imageBitmap
    val base64Image = takePictureViewModel.base64EncodedImage
    val detectionResult = takePictureViewModel.detectionResult

    var found by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var dialogShown by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }

    val boxWidth = remember { mutableFloatStateOf(0f) }
    val boxHeight = remember { mutableFloatStateOf(0f) }

    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { capturedBitmap ->
        if (capturedBitmap != null) {
            takePictureViewModel.setBitmap(capturedBitmap)
        }
    }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Tắt nhạc chuông",
                        style = MaterialTheme.typography.titleMedium
                    )
                },
//                navigationIcon = {
//                    IconButton(onClick = {
//                        navController.popBackStack()
////                        waterViewModel.startTimer()
//                    }) {
//                        Icon(Icons.Default.ArrowBack, contentDescription = null)
//                    }
//                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                val roundedRadius = 16.dp
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .aspectRatio(0.75f)
                        .border(2.dp, Color.Black, shape = RoundedCornerShape(roundedRadius))
                ) {
                    if (imageBitmap != null) {
                        Box(modifier = Modifier
                            .fillMaxSize()
                            .onSizeChanged { size ->
                                boxWidth.floatValue = size.width.toFloat()
                                boxHeight.floatValue = size.height.toFloat()
                            }) {
                            Image(
                                bitmap = imageBitmap.asImageBitmap(),
                                contentDescription = "Captured Image",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(roundedRadius)),
                                contentScale = ContentScale.Fit,
                            )

                            if (detectionResult is ApiState.Success) {
                                val response = detectionResult.data!!
                                Canvas(modifier = Modifier.fillMaxSize()) {
                                    // Calculate the scaled image dimensions
                                    val imageAspectRatio =
                                        response.image.width.toFloat() / response.image.height.toFloat()
                                    val boxAspectRatio = boxWidth.value / boxHeight.value

                                    val scaledImageWidth: Float
                                    val scaledImageHeight: Float
                                    val offsetX: Float
                                    val offsetY: Float

                                    if (imageAspectRatio > boxAspectRatio) {
                                        // Image is wider than box
                                        scaledImageWidth = boxWidth.value
                                        scaledImageHeight = boxWidth.value / imageAspectRatio
                                        offsetX = 0f
                                        offsetY = (boxHeight.value - scaledImageHeight) / 2
                                    } else {
                                        // Image is taller than box
                                        scaledImageHeight = boxHeight.value
                                        scaledImageWidth = boxHeight.value * imageAspectRatio
                                        offsetX = (boxWidth.value - scaledImageWidth) / 2
                                        offsetY = 0f
                                    }

                                    val scaleX = scaledImageWidth / response.image.width
                                    val scaleY = scaledImageHeight / response.image.height

                                    response.predictions.forEach { prediction ->
                                        // Calculate scaled positions and dimensions
                                        val scaledX =
                                            (prediction.x * scaleX - (prediction.width * scaleX / 2)) + offsetX
                                        val scaledY =
                                            (prediction.y * scaleY - (prediction.height * scaleY / 2)) + offsetY
                                        val scaledWidth = prediction.width * scaleX
                                        val scaledHeight = prediction.height * scaleY

                                        // Draw bounding box
                                        drawRect(
                                            color = if (prediction.confidence > threshold) Color.Green else Color.Red,
                                            topLeft = Offset(scaledX, scaledY),
                                            size = Size(scaledWidth, scaledHeight),
                                            style = androidx.compose.ui.graphics.drawscope.Stroke(
                                                width = 4f
                                            )
                                        )

                                        // Draw label background
                                        drawRect(
                                            color = if (prediction.confidence > threshold) Color.Green.copy(
                                                alpha = 0.3f
                                            )
                                            else Color.Red.copy(alpha = 0.3f),
                                            topLeft = Offset(scaledX, scaledY - 30f),
                                            size = Size(200f, 30f)
                                        )

                                        // Draw label text
                                        drawIntoCanvas { canvas ->
                                            val paint = android.graphics.Paint().apply {
                                                color = android.graphics.Color.WHITE
                                                textSize = 30f
                                            }
                                            canvas.nativeCanvas.drawText(
                                                "${prediction.`class`} ${(prediction.confidence * 100).toInt()}%",
                                                scaledX + 10f,
                                                scaledY - 5f,
                                                paint
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            val horizontalSpace = 8.dp
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {
                        takePictureViewModel.resetDetectionResult()
                        found = false
                        dialogShown = false
                        takePictureLauncher.launch()
                    },
                ) {
                    Icon(Photo_camera, contentDescription = null)
                    Spacer(modifier = Modifier.width(horizontalSpace))
                    Text(text = "Chụp ảnh")
                }

                Button(
                    onClick = { takePictureViewModel.detectObject(base64Image!!) },
                    enabled = base64Image != null && detectionResult is ApiState.Empty,
                ) {
                    Icon(ShieldCheck, contentDescription = null)
                    Spacer(modifier = Modifier.width(horizontalSpace))
                    Text("Kiểm tra")
                    if (isLoading) {
                        Spacer(modifier = Modifier.width(horizontalSpace))
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            strokeWidth = 1.dp
                        )
                    }
                }
            }

            Card() {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Để tắt nhạc chuông, hãy chụp một bức ảnh có chai nước, sau đó nhấn nút \"Kiểm tra\".",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }

            Button(
                onClick = {
                    found = false
                    waterViewModel.stopRinging()
                    takePictureViewModel.resetDetectionResult()
                    takePictureViewModel.resetImage()
                    context.stopService(Intent(context, MusicService::class.java))
                    waterViewModel.stopTimer()
                    scope.launch {
                        waterViewModel.stopTimer()
                        delay(100)
                        waterViewModel.startTimer()
                    }
                    navController.navigate("water")
                },
                modifier = Modifier.fillMaxWidth(), enabled = found
            ) {
                Icon(BellSlash, contentDescription = null)
                Spacer(modifier = Modifier.width(horizontalSpace))
                Text("Tắt nhạc chuông")
            }

            when (detectionResult) {
                is ApiState.Empty -> {}
                is ApiState.Loading -> {
                    isLoading = true
                }

                is ApiState.Success -> {
                    val response = detectionResult.data!!
                    found = response.predictions.any { it.confidence > threshold }
                    isLoading = false
                    if (!dialogShown) {
                        showDialog = true // Show dialog if it hasn't been shown yet
                        dialogShown = true // Mark dialog as shown
                    }
                    dialogMessage =
                        if (found) "Đã tìm thấy nước, bạn có thể tắt nhạc chuông!" else "Chưa tìm thấy nước, vui lòng chụp lại!"
                }

                is ApiState.Error -> {
                    val errorMessage = detectionResult.message
                    isLoading = false
                    if (!dialogShown) {
                        showDialog = true // Show dialog if it hasn't been shown yet
                        dialogShown = true // Mark dialog as shown
                    }
                    dialogMessage = "Đã xảy ra lỗi: $errorMessage"
                }
            }
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Kết quả") },
                text = {
                    Text(text = dialogMessage)
                },
                confirmButton = {
                    Button(onClick = { showDialog = false }) {
                        Text("Đồng ý")
                    }
                },
            )
        }
    }

}