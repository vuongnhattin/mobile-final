package com.example.mobilefinal.viewmodel

import android.graphics.Bitmap
import android.util.Base64
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilefinal.api.ApiState
import com.example.mobilefinal.api.DetectionResponse
import com.example.mobilefinal.api.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.ByteArrayOutputStream

class PictureViewModel : ViewModel() {
    var imageBitmap by mutableStateOf<Bitmap?>(null)
        private set

    var base64EncodedImage by mutableStateOf<String?>(null)
        private set

    var detectionResult by mutableStateOf<ApiState<DetectionResponse>>(ApiState.Empty())
        private set

    fun setBitmap(bitmap: Bitmap) {
        imageBitmap = bitmap
        base64EncodedImage = bitmapToBase64(bitmap)
    }

    fun resetDetectionResult() {
        detectionResult = ApiState.Empty()
    }

    fun detectObject(base64Image: String) {
        viewModelScope.launch {
            try {
                detectionResult = ApiState.Loading()
                val response = RetrofitInstance.api.detectObject(base64Image = base64Image)
                println("response: ${response.predictions}")
                detectionResult = ApiState.Success(response)
            } catch (e: HttpException) {
                e.printStackTrace()
                detectionResult = ApiState.Error(e.message())
            } catch (e: Exception) {
                e.printStackTrace()
                detectionResult = ApiState.Error(e.message)
            }
        }
    }
}

private fun bitmapToBase64(bitmap: Bitmap): String {
    val outputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream) // Use PNG or JPEG
    val byteArray = outputStream.toByteArray()
    return Base64.encodeToString(byteArray, Base64.NO_WRAP) // Avoid line breaks
}