package com.example.a03gussthenumber

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay


@Composable
fun HandleEvaluateGuess(gameState: GameState) {
    val shake = remember { Animatable(0f) }
    val angleOffset = 45f

    val x1 =  0.47f
    val y1 = -0.63f
    val x2 = 1f-x1
    val y2 = 1f+y1


    val rotationAnim by animateFloatAsState(
        targetValue   = if (isShaking) angleOffset else 0f,
        animationSpec = keyframes {
            durationMillis = 500
            0f                   at 0                                  with CubicBezierEasing(x1, y1, x2, y2)
            -angleOffset * 0.25f at ((durationMillis*0.25).toInt()  )  with CubicBezierEasing(x1, y1, x2, y2)
            angleOffset * 0.25f at ((durationMillis*0.25).toInt()*2 ) with CubicBezierEasing(x1, y1, x2, y2)
            0f                   at   durationMillis                   with CubicBezierEasing(x1, y1, x2, y2)
        }
    )

    Box(
        Modifier.fillMaxSize().wrapContentSize(Alignment.Center).rotate(rotationAnim),
        contentAlignment = Alignment.Center
    ) {

        Text(
            text = if (gameState == GameState.Correct) "⨀" else if (gameState == GameState.NotGussed) "" else "⨉",
            fontSize = 60.sp,
            color = if (gameState == GameState.Correct) Color.Green else Color.Red
        )

    }
}

@Composable
fun CheckButton(gameState: GameState, onCheckClick: () -> Unit) {

    LaunchedEffect(isShaking) {
        if (isShaking) {
            delay(500) // Adjust the duration based on your animation specs
            isShaking = false
        }
    }
    Button(
        shape = RoundedCornerShape(6.dp),
        modifier = Modifier.fillMaxWidth().padding(0.dp).width(IntrinsicSize.Max).pointerInput(Unit) {
            detectTapGestures { offset ->
                // Handle tap event to start the shake animation
                isShaking = true
            }
        },
        colors = ButtonDefaults.buttonColors(if (gameState == GameState.Correct) Color.Transparent else Color(0xf0c1afa0)),
        content = { Text(if (gameState == GameState.Correct) "" else "Check") },
        enabled = gameState != GameState.Correct,
        onClick = {
            onCheckClick()

            // Activate shake animation on button click
            isShaking = true
        }
    )
}




@Composable
fun HandleGuessedNumberTooLow(gameState: GameState){
    Box(Modifier .fillMaxSize().wrapContentSize(Alignment.Center)){
        Text(if (gameState == GameState.TooHigh) "▼"
        else if (gameState == GameState.Correct) " "
        else " ",
            fontSize = 84.sp,
            color = Color(0xf0c1afa0)
        )
    }
}

@Composable
fun HandleGuessedNumberTooHigh(gameState: GameState){
    Box(Modifier .fillMaxSize().wrapContentSize(Alignment.Center)){
        Text(if (gameState == GameState.TooLow) "▲"
        else if (gameState == GameState.Correct) " "
        else " ",
            fontSize = 84.sp,
            color = Color(0xf0c1afa0)
        )
    }
}




fun checkGuess(userInput: String, hiddenNumber: Int,onGameStateUpdate: (GameState) -> Unit ) {
    val guess: Int = userInput.toIntOrNull() ?: 0

    when { guess < hiddenNumber -> { onGameStateUpdate(GameState.TooLow) }
        guess > hiddenNumber -> { onGameStateUpdate(GameState.TooHigh)}
        else                 -> { onGameStateUpdate(GameState.Correct) }
    }
}



@Composable
fun NumberGrid(userInput: String, ) {
    // Sample code (replace with your existing logic):
    Card(
        Modifier.fillMaxSize().wrapContentSize(Alignment.Center),
        colors = CardDefaults.cardColors( Color(0x005C4634) )
    ) {
        Text(
            text = if (userInput.isEmpty()) "?" else userInput,
            fontSize = if (userInput.isEmpty()) 84.sp else 60.sp,
            color = Color(0xFFC2B0A1)
        )
    }
}





@Composable
fun NewGameButton(onNewGameClick: () -> Unit) {
    val ButtonShape    = RoundedCornerShape(6.dp)
    val ButtonModifier = Modifier.fillMaxWidth().padding(0.dp).width(IntrinsicSize.Max)
    val ButtonColor    = ButtonDefaults.buttonColors(Color(0xf0c1afa0))

    Button(
        modifier = ButtonModifier,
        colors   = ButtonColor,
        shape    = ButtonShape,
        content  = { Text("New Game") },
        onClick  = { onNewGameClick() }
    )
}



