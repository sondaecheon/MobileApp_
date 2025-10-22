package com.example.w06

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.w06.ui.theme.MoblieApp002Theme
import kotlinx.coroutines.delay
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MoblieApp002Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BubbleGameScreen()
                }
            }
        }
    }
}

// 버블의 속성을 담는 데이터 클래스
// --- 데이터 클래스 (단위: dp) ---
data class Bubble(
    val id: Int,
    val position: Offset, // 위치 (x, y 좌표). dp 단위를 의미.
    val radius: Float,    // 반지름. dp 단위를 의미.
    val color: Color,
    val creationTime: Long = System.currentTimeMillis(),
    val velocityX: Float = Random.nextFloat() * 8 - 4, // 초당 dp 이동 속도
    val velocityY: Float = Random.nextFloat() * 8 - 4  // 초당 dp 이동 속도
)

class GameState(initialBubbles: List<Bubble> = emptyList()) {
    var bubbles by mutableStateOf(initialBubbles)
    var score by mutableStateOf(0)
    var isGameOver by mutableStateOf(false)
    var timeLeft by mutableStateOf(60) // 남은 시간: 60초로 시작
}

// 게임의 전체 화면
@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun BubbleGameScreen() {
    // 1. 게임에 필요한 상태 변수들 선언, 버블 리스트가 빈 채로 시작하면 미리보기에선 안보일 수 있다.
    val gameState: GameState = remember { GameState() }

    // 2. 타이머 로직
    LaunchedEffect(gameState.isGameOver) {
        // 게임이 진행 중일 때만 타이머 작동
        if (!gameState.isGameOver && gameState.timeLeft > 0) {
            while (true) {
                delay(1000L) // 1초 대기
                gameState.timeLeft-- // 시간 1초 감소
                if (gameState.timeLeft == 0) {
                    gameState.isGameOver = true // 시간이 0이 되면 게임 오버
                    break
                }
                // 3초가 지난 버블 제거
                val currentTime = System.currentTimeMillis()
                gameState.bubbles = gameState.bubbles.filter { // filter()는 원본 리스트를 변경하지 않고 새 리스트 생성
                    currentTime - it.creationTime < 3000
                }
            }
        }
    }

    // 3. 버블의 상태를 관리
    Column(modifier = Modifier.fillMaxSize()) {
        // 3-1. 상단 상태 바 UI (점수, 남은 시간)
        GameStatusRow(score = gameState.score, timeLeft = gameState.timeLeft)

        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val density = LocalDensity.current
            val canvasWidthPx = with(density) { maxWidth.toPx() }
            val canvasHeightPx = with(density) { maxHeight.toPx() }

            // 3-2. 버블 물리 엔진
            LaunchedEffect(key1 = gameState.isGameOver) {
                if (!gameState.isGameOver) {
                    while (true) {
                        delay(16)

                        // 버블이 없으면 3개를 새로 생성합니다.
                        if (gameState.bubbles.isEmpty()) {
                            val newBubbles = List(3) { // 3개의 버블 생성
                                Bubble(
                                    id = Random.nextInt(),
                                    position = Offset(
                                        x = Random.nextFloat() * maxWidth.value, // 위치 단위는 dp
                                        y = Random.nextFloat() * maxHeight.value
                                    ),
                                    radius = Random.nextFloat() * 25 + 25, // 반지름 단위는 dp
                                    color = Color(Random.nextInt(256), Random.nextInt(256), Random.nextInt(256), 200)
                                )
                            }
                            gameState.bubbles = newBubbles // 생성된 버블 리스트로 교체
                        }

                        // 새 버블 생성 (랜덤)
                        if (Random.nextFloat() < 0.05f && gameState.bubbles.size < 15) {
                            val newBubble = Bubble(
                                id = Random.nextInt(),
                                position = Offset(
                                    x = Random.nextFloat() * maxWidth.value, // 위치 단위는 dp
                                    y = Random.nextFloat() * maxHeight.value
                                ),
                                radius = Random.nextFloat() * 50 + 50,
                                color = Color(
                                    red = Random.nextInt(256),
                                    green = Random.nextInt(256),
                                    blue = Random.nextInt(256),
                                    alpha = 200
                                )
                            )
                            gameState.bubbles = gameState.bubbles + newBubble
                        }

                        // 기존 물리 엔진 로직 (버블 이동)
                        gameState.bubbles = gameState.bubbles.map { bubble ->
                            with(density) {
                                // --- 1. 모든 dp 값을 px로 변환 (코드가 훨씬 짧아짐) ---
                                val radiusPx = bubble.radius.dp.toPx()
                                var xPx = bubble.position.x.dp.toPx()
                                var yPx = bubble.position.y.dp.toPx()
                                val vxPx = bubble.velocityX.dp.toPx()
                                val vyPx = bubble.velocityY.dp.toPx()

                                // --- 2. px 단위로 물리 계산 수행 (기존과 동일) ---
                                xPx += vxPx
                                yPx += vyPx

                                var newVx = bubble.velocityX
                                var newVy = bubble.velocityY

                                if (xPx < radiusPx || xPx > canvasWidthPx - radiusPx) newVx *= -1
                                if (yPx < radiusPx || yPx > canvasHeightPx - radiusPx) newVy *= -1

                                xPx = xPx.coerceIn(radiusPx, canvasWidthPx - radiusPx)
                                yPx = yPx.coerceIn(radiusPx, canvasHeightPx - radiusPx)

                                // --- 3. 계산 완료 후, 결과를 다시 dp로 변환하여 저장 ---
                                bubble.copy(
                                    position = Offset(
                                        x = xPx.toDp().value,
                                        y = yPx.toDp().value
                                    ),
                                    velocityX = newVx,
                                    velocityY = newVy
                                )
                            } // `with` 블록의 마지막 표현식(새로운 bubble 객체)이 map의 반환값
                        }
                    }
                }
            }

            // 3-4. 버블 그리기
            gameState.bubbles.forEach { bubble ->
                BubbleComposable(bubble = bubble) {
                    // 버블 클릭 시 점수 올리고, 해당 버블 제거
                    gameState.score++
                    gameState.bubbles = gameState.bubbles.filterNot { it.id == bubble.id }
                }
            }
        }
    }
}

// 버블 UI를 그리는 Composable
@Composable
fun BubbleComposable(bubble: Bubble, onClick: () -> Unit) {
    Canvas(
        modifier = Modifier
            .offset(x = bubble.position.x.dp, y = bubble.position.y.dp)
            .size((bubble.radius * 2 ).dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
    ) {
        // 3. 원은 Canvas의 정가운데에 그립니다. 클릭 영역은 실제로 사각형이다. ㅠㅜ
        drawCircle(
            color = bubble.color,
            radius = size.width / 2, // / size.width는 이 Canvas의 실제 가로 픽셀(px) 크기를 의미합니다.
            center = center
        )
    }
}
// 상단 UI를 별도의 Composable로 분리 (가독성 향상)
@Composable
fun GameStatusRow(score: Int , timeLeft: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Score: $score", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text(text = "Time: ${timeLeft}s", fontSize = 24.sp, fontWeight = FontWeight.Bold)
    }
}

@Preview(showBackground = true)
@Composable
fun BubbleGamePreview() {
    w06Theme {
        BubbleGameScreen()
    }
}

fun w06Theme(function: @Composable () -> Unit) {
    TODO("Not yet implemented")
}

@Composable
fun GameOverDialog(score: Int, onRestart: () -> Unit, onExit: () -> Unit) {
    AlertDialog(
        onDismissRequest = {},
        title = { Text("게임 오버") },
        text = { Text("당신의 점수는 $score 점입니다.") },
        confirmButton = {
            TextButton(onClick = onRestart) {
                Text("다시 시작")
            }
        },
        dismissButton = {
            TextButton(onClick = onExit) {
                Text("종료")
            }
        }
    )
}

// 게임 종료 시 호출되는 함수
fun onGameOver(gameState: GameState, showDialog: () -> Unit) {
    gameState.isGameOver = true
    showDialog()
}

// 게임 재시작 함수
fun restartGame(gameState: GameState) {
    gameState.score = 0
    gameState.timeLeft = 60
    gameState.isGameOver = false
    gameState.bubbles = emptyList()
}




