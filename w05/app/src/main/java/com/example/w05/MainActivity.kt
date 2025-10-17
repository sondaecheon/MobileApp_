package com.example.w05

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val darkTheme = isSystemInDarkTheme()
            MyAppTheme(darkTheme = darkTheme) {
                val count = remember { mutableStateOf(0) }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically)
                ) {
                    CounterApp(count)
                    StopWatchApp()
                }
            }
        }
    }
}

@Composable
fun CounterApp(count: MutableState<Int>) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Count: ${count.value}",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Button(
                onClick = { count.value++ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                )
            ) { Text("Increase") }
            Button(
                onClick = { count.value = 0 },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                )
            ) { Text("Reset") }
        }
    }
}

@Composable
fun StopWatchApp() {
    var timeInMillis by remember { mutableStateOf(0L) }
    var isRunning by remember { mutableStateOf(false) }
    var lapTimes by remember { mutableStateOf(listOf<Long>()) }
    var startTime by remember { mutableStateOf(0L) }

    LaunchedEffect(isRunning) {
        if (isRunning) {
            startTime = System.currentTimeMillis() - timeInMillis
            while (isRunning) {
                delay(10L)
                timeInMillis = System.currentTimeMillis() - startTime
            }
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        StopwatchScreen(
            timeInMillis = timeInMillis,
            onStartClick = {
                if (!isRunning) isRunning = true
            },
            onStopClick = {
                if (isRunning) {
                    isRunning = false
                    if (timeInMillis > 0L) lapTimes = listOf(timeInMillis) + lapTimes
                }
            },
            onResetClick = {
                isRunning = false
                timeInMillis = 0L
                lapTimes = emptyList()
            }
        )

        if (lapTimes.isNotEmpty()) {
            Text(
                "Records:",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.padding(top = 8.dp),
                color = MaterialTheme.colorScheme.onBackground
            )
            LazyColumn(
                modifier = Modifier
                    .heightIn(max = 200.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
            ) {
                items(lapTimes.take(5)) { lapTime ->
                    Text(
                        text = formatTime(lapTime),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Divider(color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f))
                }
            }
        }
    }
}

@Composable
fun StopwatchScreen(
    timeInMillis: Long,
    onStartClick: () -> Unit,
    onStopClick: () -> Unit,
    onResetClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = formatTime(timeInMillis),
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Button(
                onClick = onStartClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                )
            ) { Text("Start") }
            Button(
                onClick = onStopClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                )
            ) { Text("Stop") }
            Button(
                onClick = onResetClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                )
            ) { Text("Reset") }
        }
    }
}

@SuppressLint("DefaultLocale")
private fun formatTime(timeInMillis: Long): String {
    val minutes = (timeInMillis / 1000) / 60
    val seconds = (timeInMillis / 1000) % 60
    val millis = (timeInMillis % 1000) / 10
    return String.format("%02d:%02d:%02d", minutes, seconds, millis)
}

@Composable
fun MyAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        darkColorScheme(
            primary = MaterialTheme.colorScheme.primary,
            secondary = MaterialTheme.colorScheme.secondary,
            background = Color(0xFF121212),
            surface = Color(0xFF1E1E1E),
            onPrimary = Color.White,
            onSecondary = Color.White,
            onBackground = Color.White,
            onSurface = Color.White,
        )
    } else {
        lightColorScheme(
            primary = MaterialTheme.colorScheme.primary,
            secondary = MaterialTheme.colorScheme.secondary,
            background = Color.White,
            surface = Color.White,
            onPrimary = Color.Black,
            onSecondary = Color.Black,
            onBackground = Color.Black,
            onSurface = Color.Black,
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography(),
        content = content
    )
}

@Preview(showBackground = true)
@Composable
fun StopWatchPreview() {
    MyAppTheme {
        StopWatchApp()
    }
}
