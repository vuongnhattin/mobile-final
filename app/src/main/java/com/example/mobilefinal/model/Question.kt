package com.example.mobilefinal.model

data class Question(
    val statement: String,
    val correctAnswer: String
)

object QuestionData {
    val questions = listOf(
//        Question("What is 2 + 2?", "4"),
//        Question("What is 4 + 4?", "8"),
//        Question("What is 8 + 8?", "16"),
        Question("16 + 16 = ?", "32"),
        Question("32 + 32 = ?", "64"),
        Question("64 + 64 = ?", "128"),
        Question("128 + 128 = ?", "256"),
        Question("256 + 256 = ?", "512"),
        Question("512 + 512 = ?", "1024"),
        Question("1024 + 1024 = ?", "2048"),
    )
}
