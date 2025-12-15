package com.example.anonymoustimer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                AnonymousTimerApp()
            }
        }
    }
}

@Composable
fun AnonymousTimerApp() {
    var screen by remember { mutableStateOf("input") }
    var goalText by remember { mutableStateOf("") }
    var hourText by remember { mutableStateOf("") }
    var minuteText by remember { mutableStateOf("") }
    var secondText by remember { mutableStateOf("") }
    var timerSeconds by remember { mutableStateOf(0) }
    var records by remember { mutableStateOf(listOf<String>()) }

    if (screen == "input") {
        GoalInputScreen(
            goalText = goalText,
            hourText = hourText,
            minuteText = minuteText,
            secondText = secondText,
            onGoalChange = { goalText = it },
            onHourChange = { hourText = it.filter { c -> c.isDigit() } },
            onMinuteChange = { minuteText = it.filter { c -> c.isDigit() } },
            onSecondChange = { secondText = it.filter { c -> c.isDigit() } },
            onStart = {
                val h = hourText.toIntOrNull() ?: 0
                val m = minuteText.toIntOrNull() ?: 0
                val s = secondText.toIntOrNull() ?: 0
                timerSeconds = h * 3600 + m * 60 + s
                if (timerSeconds > 0) screen = "timer"
            }
        )
    } else {
        TimerScreen(
            goal = goalText,
            startTime = timerSeconds,
            records = records,
            onStop = {
                records = records + "익명 | ${hourText.padStart(2, '0')}:${minuteText.padStart(2, '0')}:${secondText.padStart(2, '0')} | $goalText"
                goalText = ""
                hourText = ""
                minuteText = ""
                secondText = ""
                screen = "input"
            }
        )
    }
}

@Composable
fun GoalInputScreen(
    goalText: String,
    hourText: String,
    minuteText: String,
    secondText: String,
    onGoalChange: (String) -> Unit,
    onHourChange: (String) -> Unit,
    onMinuteChange: (String) -> Unit,
    onSecondChange: (String) -> Unit,
    onStart: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F6FA))
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Anonymous Timer", fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text("집중 목표와 시간을 입력하세요", color = Color.Gray)
        Spacer(modifier = Modifier.height(32.dp))

        Card(shape = RoundedCornerShape(16.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                TextField(
                    value = goalText,
                    onValueChange = onGoalChange,
                    placeholder = { Text("집중 목표") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TextField(
                        value = hourText,
                        onValueChange = onHourChange,
                        placeholder = { Text("시") },
                        modifier = Modifier.weight(1f)
                    )
                    TextField(
                        value = minuteText,
                        onValueChange = onMinuteChange,
                        placeholder = { Text("분") },
                        modifier = Modifier.weight(1f)
                    )
                    TextField(
                        value = secondText,
                        onValueChange = onSecondChange,
                        placeholder = { Text("초") },
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = onStart,
                    enabled = goalText.isNotBlank() && (hourText.isNotBlank() || minuteText.isNotBlank() || secondText.isNotBlank()),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("집중 시작")
                }
            }
        }
    }
}

@Composable
fun TimerScreen(
    goal: String,
    startTime: Int,
    records: List<String>,
    onStop: () -> Unit
) {
    var time by remember { mutableStateOf(startTime) }
    var running by remember { mutableStateOf(true) }

    LaunchedEffect(running) {
        while (running && time > 0) {
            delay(1000)
            time--
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1E272E))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("집중 중", color = Color.White, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Text(goal, color = Color.LightGray)
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            String.format("%02d:%02d:%02d", time / 3600, (time % 3600) / 60, time % 60),
            fontSize = 46.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = {
                running = false
                onStop()
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEE5253)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("종료", color = Color.White)
        }
        Spacer(modifier = Modifier.height(32.dp))
        Card(shape = RoundedCornerShape(16.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("익명 기록", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                LazyColumn {
                    items(records) {
                        Text(it, modifier = Modifier.padding(vertical = 4.dp))
                    }
                }
            }
        }
    }
}