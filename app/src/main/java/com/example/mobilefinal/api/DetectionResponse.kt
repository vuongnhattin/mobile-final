package com.example.mobilefinal.api

data class DetectionResponse (
    val image: Image,
    val predictions: List<Prediction>
)

data class Image (
    val width: Int,
    val height: Int,
)

data class Prediction (
    val x: Float,
    val y: Float,
    val width: Float,
    val height: Float,
    val confidence: Float,
    val `class`: String,
    val classID: Int
)