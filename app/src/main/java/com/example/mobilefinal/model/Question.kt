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
        Question("What is 16 + 16?", "32"),
        Question("What is 32 + 32?", "64"),
        Question("What is 64 + 64?", "128"),
        Question("What is 128 + 128?", "256"),
        Question("What is 256 + 256?", "512"),
        Question("What is 512 + 512?", "1024"),
        Question("What is 1024 + 1024?", "2048")
    )
}
