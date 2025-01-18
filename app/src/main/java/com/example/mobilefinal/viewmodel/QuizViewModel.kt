package com.example.mobilefinal.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.mobilefinal.model.Question
import com.example.mobilefinal.model.QuestionData

class QuizViewModel : ViewModel() {
    var currentQuestionIndex by mutableStateOf(0)
        private set
    var questions by mutableStateOf(listOf<Question>())
        private set

    fun getRandomQuestions(size: Int) {
        questions = QuestionData.questions.shuffled().take(size)
    }

    fun nextQuestion() {
        currentQuestionIndex++
    }

    fun resetCurrentQuestionIndex() {
        currentQuestionIndex = 0
    }
}