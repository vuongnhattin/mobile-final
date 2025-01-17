package com.example.mobilefinal.screen

import android.app.Activity
import android.content.Intent
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.mobilefinal.LocalAlarmViewModel
import com.example.mobilefinal.LocalNavController
import com.example.mobilefinal.LocalQuizViewModel
import com.example.mobilefinal.model.QuestionData.questions
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(modifier: Modifier = Modifier) {
    val navController = LocalNavController.current

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Tắt báo thức",
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) { innerPadding ->
        QuizContent(
            modifier = modifier.padding(innerPadding)
        )
    }
}

@Composable
fun QuizContent(modifier: Modifier = Modifier) {
    val quizViewModel = LocalQuizViewModel.current
    val navController = LocalNavController.current
    var answer by remember { mutableStateOf("") }

    val currentQuestionIndex = quizViewModel.currentQuestionIndex
    val questions = quizViewModel.questions
    val numberOfQuestion = 3

    var alertText by remember { mutableStateOf("") }

    var enableTurnOffAlarm by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        quizViewModel.getRandomQuestions(numberOfQuestion)
        quizViewModel.resetCurrentQuestionIndex()
    }

    val alarmViewModel = LocalAlarmViewModel.current
    val context = LocalContext.current

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val spokenText =
                    result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                spokenText?.let {
                    answer = it[0] // First result is usually the most accurate.
                }
            }
        }

    Column(
        modifier = modifier.fillMaxSize(),
//        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Câu hỏi: ${currentQuestionIndex + 1}/$numberOfQuestion",
        )

        Button(
            onClick = {
                context.stopService(Intent(context, AlarmService::class.java))
                alarmViewModel.stopTimer()
            },
            enabled = enableTurnOffAlarm
        ) {
            Text("Tắt báo thức")
        }

        if (questions.isNotEmpty()) {
            val question = questions[currentQuestionIndex]
            Text(question.statement)
            TextField(
                value = answer,
                onValueChange = {
                    answer = it
                },
                supportingText = { Text(alertText) }
            )

            Button(onClick = {
                if (SpeechRecognizer.isRecognitionAvailable(context)) {
                    val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                        putExtra(
                            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                        )
//                        putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
                        putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en")
//                        putExtra(RecognizerIntent.EXTRA_LANGUAGE, "vi")
                    }
                    launcher.launch(intent)
                } else {
                    answer = "Speech recognition is not available on this device."
                }
            }) {
                Text("Start Listening")
            }

            Button(
                onClick = {
                    if (answer != question.correctAnswer) {
                        alertText = "Wrong answer"
                    } else {
                        alertText = ""
                        if (currentQuestionIndex != numberOfQuestion - 1) {
                            quizViewModel.nextQuestion()
                            answer = ""
                        } else {
                            enableTurnOffAlarm = true
                        }
                    }
                }
            ) {
                if (currentQuestionIndex == numberOfQuestion - 1) {
                    Text("Finish")
                } else {
                    Text("Next")
                }
            }
        }
    }
}