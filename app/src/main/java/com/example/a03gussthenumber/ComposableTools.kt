package com.example.a03gussthenumber

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GridWithComponents(
    rows: Int,
    columns: Int,
    checkerboard: Boolean = false,
    modifier: Modifier = Modifier,
    content: @Composable (row: Int, column: Int) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        for (row in 0 until rows) {
            Row(
                Modifier
                    .weight(1f)
                    .fillMaxSize()) {
                for (column in 0 until columns) {
                    val cellModifier = if (checkerboard && (row + column) % 2 == 0) {
                        Modifier.background(Color.Gray)
                    } else { Modifier }

                    Box(modifier = cellModifier
                        .weight(1f)
                        .fillMaxSize()) {content(row, column) }
                }
            }
        }
    }
}




@Composable
fun ButtonRow(Left: String,
              mid:  String,
              Right:String,
              onButtonPressed: (String) -> Unit,
              ButtonColor01: Color = Color(0xffC2B0A1),
              ButtonColor02: Color = Color(0xf0c1afa0),
              ButtonColor03: Color = Color(0xf0c1afa0),
){
    Row (modifier = Modifier.fillMaxWidth().height(100.dp),horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Bottom) {
        Button(onClick = { onButtonPressed(Left) }, Modifier.weight(0.5f).fillMaxHeight()
            .padding(6.dp, 0.dp, 6.dp, 6.dp), shape = RoundedCornerShape(3.dp), colors = ButtonDefaults.buttonColors(ButtonColor01)) { Text(Left, fontSize = 24.sp)  }
        Button(onClick = { onButtonPressed(mid)  }, Modifier.weight(0.5f).fillMaxHeight()
            .padding(0.dp, 0.dp, 0.dp, 6.dp), shape = RoundedCornerShape(3.dp),colors = ButtonDefaults.buttonColors(ButtonColor02)) { Text(mid  , fontSize = 24.sp) }
        Button(onClick = { onButtonPressed(Right)}, Modifier.weight(0.5f).fillMaxHeight()
            .padding(6.dp, 0.dp, 6.dp, 6.dp), shape = RoundedCornerShape(3.dp),colors = ButtonDefaults.buttonColors(ButtonColor03)) { Text(Right, fontSize = 24.sp) }

    }
}

