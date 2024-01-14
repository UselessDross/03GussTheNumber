package com.example.a03gussthenumber

import android.os.*
import androidx.compose.material3.Text
import androidx.activity.compose.*
import androidx.activity.*
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.*

import com.example.a03gussthenumber.ui.theme.*
import kotlinx.coroutines.*


enum class GameState {
    TooLow,
    TooHigh,
    Correct,
    NotGussed
}



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            _03GussTheNumberTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    uiLayout()
                }
            }
        }
    }
}

var gameState        by  mutableStateOf(GameState.NotGussed)
var triggerAnimation by   mutableStateOf(false)
var isShaking        by   mutableStateOf(false)

@Preview
@Composable
fun uiLayout() {
    var hiddenNumber by remember { mutableStateOf((1..100).random()) }
    var userInput    by remember { mutableStateOf("") }


    val onNewGameClick = {
                          userInput = ""
                          hiddenNumber = (1..100).random()
                          gameState = GameState.NotGussed
                          triggerAnimation = false
                         }

    val onCheckGuessClick = {
        checkGuess(userInput, hiddenNumber) { newGameState ->
                                                gameState = newGameState
                                                triggerAnimation = true
                                             }
    }

    val onButtonPressed: (String) -> Unit = { pressedText ->
        when (pressedText) {
            "clr" -> userInput = ""
            else  -> userInput = userInput.plus(pressedText)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().background(Color.DarkGray)
            .padding(0.dp).wrapContentSize()
    ) {
        GridWithComponents(3, 3, false, modifier = Modifier.weight(1f).padding(6.dp)) { row, column ->
            when {
                (row == 1 && column == 1) -> NumberGrid(userInput)
                (row == 1 && column == 2) -> NewGameButton {      onNewGameClick()   }
                (row == 2 && column == 2) -> CheckButton(               gameState) { onCheckGuessClick() }
                (row == 0 && column == 0) -> HandleGuessedNumberTooHigh(gameState)
                (row == 1 && column == 0) -> HandleEvaluateGuess(       gameState)
                (row == 2 && column == 0) -> HandleGuessedNumberTooLow( gameState)
            }
        }

        ButtonRow("1", "2", "3",   onButtonPressed)
        ButtonRow("4", "5", "6",   onButtonPressed)
        ButtonRow("7", "8", "9",   onButtonPressed)
        ButtonRow(".", "0", "clr", onButtonPressed)
    }
}
