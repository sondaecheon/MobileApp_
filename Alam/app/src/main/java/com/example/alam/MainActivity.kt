package com.example.Alam

import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.alam.ui.theme.AlamTheme

class MainActivity : ComponentActivity() {
    private var mediaPlayer: MediaPlayer? = null
    private var timer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AlamTheme {
                AlarmApp(
                    onPlayAlarm = { playAlarm() },
                    onStopAlarm = { stopAlarm() }
                )
            }
        }
    }

    private fun playAlarm() {
        mediaPlayer = MediaPlayer.create(this, android.provider.Settings.System.DEFAULT_ALARM_ALERT_URI)
        mediaPlayer?.start()
    }

    private fun stopAlarm() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    override fun onDestroy() {
        super.onDestroy()
        stopAlarm()
        timer?.cancel()
    }
}

@Composable
fun AlarmApp(onPlayAlarm: () -> Unit, onStopAlarm: () -> Unit) {
    var inputTime by remember { mutableStateOf("") }
    var timeLeft by remember { mutableStateOf(0L) }
    var isRunning by remember { mutableStateOf(false) }
    var timer by remember { mutableStateOf<CountDownTimer?>(null) }
    var message by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            TextField(
                value = inputTime,
                onValueChange = { inputTime = it },
                placeholder = { Text("초 입력", color = Color.Gray) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier
                    .width(150.dp)
                    .padding(bottom = 16.dp),
                textStyle = androidx.compose.ui.text.TextStyle(
                    color = Color.White,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center
                )
            )

            Text(
                text = if (isRunning) "${timeLeft}s" else message.ifEmpty { " " },
                color = Color.White,
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            Button(
                onClick = {
                    if (!isRunning && inputTime.isNotEmpty()) {
                        val totalMillis = inputTime.toLong() * 1000
                        message = ""
                        isRunning = true

                        timer = object : CountDownTimer(totalMillis, 1000) {
                            override fun onTick(millisUntilFinished: Long) {
                                timeLeft = millisUntilFinished / 1000
                            }

                            override fun onFinish() {
                                isRunning = false
                                timeLeft = 0
                                message = "알람"
                                onPlayAlarm()
                            }
                        }.start()
                    } else {
                        timer?.cancel()
                        isRunning = false
                        message = "정지됨"
                        onStopAlarm()
                    }
                },
                modifier = Modifier
                    .width(120.dp)
                    .height(50.dp)
            ) {
                Text(
                    text = if (isRunning) "정지" else "시작",
                    color = Color.White,
                    fontSize = 20.sp
                )
            }
        }
    }
}
