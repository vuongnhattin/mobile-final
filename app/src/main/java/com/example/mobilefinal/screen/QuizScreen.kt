package com.example.mobilefinal.screen

import android.app.Activity
import android.content.Intent
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import com.example.mobilefinal.LocalAlarmViewModel
import com.example.mobilefinal.LocalNavController
import com.example.mobilefinal.LocalQuizViewModel
import com.example.mobilefinal.R
import com.example.mobilefinal.icon.Microphone
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
                        text = "Giải đố",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                },
//                navigationIcon = {
//                    IconButton(onClick = {
//                        navController.popBackStack()
//                    }) {
//                        Icon(Icons.Default.ArrowBack, contentDescription = null)
//                    }
//                }
            )
        }
    ) { innerPadding ->
        QuizContent(
            modifier = modifier.padding(innerPadding)
        )
    }
}

data class Language(
    val code: String,
    val name: String
)

@Composable
fun QuizContent(modifier: Modifier = Modifier) {
    val quizViewModel = LocalQuizViewModel.current
    val navController = LocalNavController.current
    var answer by remember { mutableStateOf("") }

    val languageList = listOf(
        Language("en", "Tiếng Anh"),
        Language("vi", "Tiếng Việt")
    )

    val currentQuestionIndex = quizViewModel.currentQuestionIndex
    val questions = quizViewModel.questions
    val numberOfQuestion = 3

    var alertText by remember { mutableStateOf("") }

    var enableTurnOffAlarm by remember { mutableStateOf(false) }

    var language by remember { mutableStateOf("en") }
    var showLanguageDialog by remember { mutableStateOf(false) }
    var showModeDialog by remember { mutableStateOf(false) }

    var mode by remember { mutableStateOf("text") }
    val modeList = listOf(
        "text",
        "voice"
    )

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

    if (showLanguageDialog) {
        AlertDialog(
            onDismissRequest = {
            },
            title = {
                Text("Chọn ngôn ngữ")
            },
            text = {
                Column {
                    for (language_ in languageList) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .selectable(selected = language_.code == language, onClick = {
                                    language = language_.code
                                })
                        ) {
                            RadioButton(
                                selected = language_.code == language,
                                onClick = {
                                    language = language_.code
                                }
                            )
                            Text(language_.name)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showLanguageDialog = false
                    }
                ) {
                    Text("Xác nhận")
                }
            }
        )
    }

    if (showModeDialog) {
        AlertDialog(
            onDismissRequest = {
            },
            title = {
                Text("Chọn cách điền đáp án")
            },
            text = {
                Column {
                    for (mode_ in modeList) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .selectable(selected = mode_ == mode, onClick = {
                                    mode = mode_
                                })
                        ) {
                            RadioButton(
                                selected = mode_ == mode,
                                onClick = {
                                    mode = mode_
                                }
                            )
                            Text(if (mode_ == "text") "Nhập văn bản" else "Nói")
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showModeDialog = false
                    }
                ) {
                    Text("Xác nhận")
                }
            }
        )
    }

    val menuItems = listOf("Chọn ngôn ngữ", "Chọn chế độ")

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp)
            .padding(top = 0.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(painterResource(R.drawable.quiz), contentDescription = null,
            modifier = Modifier.size(120.dp))
        Spacer(modifier = Modifier.height(8.dp))
        if (questions.isNotEmpty()) {
            val question = questions[currentQuestionIndex]
            OutlinedCard() {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            question.statement,
                            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.End
                        ) {
                            DropdownMenuIcon(
                                items = menuItems
                            ) { item ->
                                when (item) {
                                    "Chọn ngôn ngữ" -> {
                                        showLanguageDialog = true
                                    }

                                    "Chọn chế độ" -> {
                                        showModeDialog = true
                                    }
                                }
                            }
                        }
                    }
                    TextField(
                        value = answer,
                        onValueChange = {
                            answer = it
                        },
                        enabled = mode == "text",
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            alertText,
                            color = Color.Red
                        )
                        Text("${currentQuestionIndex + 1}/$numberOfQuestion")
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            onClick = {
                                if (SpeechRecognizer.isRecognitionAvailable(context)) {
                                    val intent =
                                        Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                                            putExtra(
                                                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                                                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                                            )
                                            if (language == "vi") {
                                                putExtra(
                                                    RecognizerIntent.EXTRA_LANGUAGE,
                                                    Locale.getDefault()
                                                )
                                            }
                                            if (language == "en") {
                                                putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en")
                                            }
                                        }
                                    launcher.launch(intent)
                                } else {
                                    answer =
                                        "Speech recognition is not available on this device."
                                }
                            },
                            enabled = mode == "voice"
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(Microphone, contentDescription = null)
                                Text("Thu âm")
                            }
                        }

                        Button(
                            onClick = {
                                if (answer != question.correctAnswer) {
                                    alertText = "Câu trả lời sai!"
                                } else {
                                    alertText = ""
                                    if (currentQuestionIndex != numberOfQuestion - 1) {
                                        quizViewModel.nextQuestion()
                                        answer = ""
                                    } else {
                                        context.stopService(
                                            Intent(
                                                context,
                                                AlarmService::class.java
                                            )
                                        )
                                        alarmViewModel.stopTimer()
                                        alarmViewModel.stopRinging()
                                        navController.navigate("alarm")
                                    }
                                }
                            }
                        ) {
                            if (currentQuestionIndex == numberOfQuestion - 1) {
                                Text("Tắt báo thức")
                            } else {
                                Text("Tiếp tục  ➜")
                            }
                        }
                    }
                }
            }

        }
    }
}