package com.example.memo

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.memo.ui.theme.MemoTheme
import androidx.core.content.edit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MemoTheme {
                MemoScreen()
            }
        }
    }
}

@Composable
fun MemoScreen() {
    val context = LocalContext.current
    val prefs = context.getSharedPreferences("memo_simple", Context.MODE_PRIVATE)

    var text by remember { mutableStateOf("") }          // 입력 중인 메모
    var memoList by remember { mutableStateOf(listOf<String>()) } // 저장된 메모 리스트

    // 앱 시작 시 불러오기
    LaunchedEffect(Unit) {
        val saved = prefs.getStringSet("memo_list", emptySet()) ?: emptySet()
        memoList = saved.toList()
    }

    Column(modifier = Modifier.padding(16.dp).fillMaxSize()) {
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("메모 입력") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                if (text.isNotBlank()) {
                    memoList = memoList + text
                    prefs.edit { putStringSet("memo_list", memoList.toSet()) }
                    text = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("저장")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(memoList) { memo ->
                Text(
                    text = memo,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable { text = memo } // 클릭 시 입력창에 불러오기
                )

            }
        }
    }
}
