import androidx.compose.animation.core.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.clip
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.*
import kotlinx.coroutines.delay


var alpha by  mutableStateOf(false)
var isUp  by   mutableStateOf(false)






@Composable
fun ButtonToTriggerAnimation(isFading: MutableState<Boolean>) {
    Button(
            onClick = { alpha = !alpha },
            modifier = Modifier.padding(6.dp)
            ) {
              Text("Click me to trigger animation")
              }
}


@Composable
fun AnimationExample() {
    var isFading by remember { mutableStateOf<Boolean>(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        FadingText(isFading = rememberUpdatedState(isFading))
        Spacer(modifier = Modifier.height(6.dp))
        ButtonToTriggerAnimation(isFading = rememberUpdatedState(isFading) as MutableState<Boolean>)
        Button(onClick = {isUp = !isUp}, content = {"is up boolean"})
    }
}

@Preview
@Composable
fun AnimationExamplePreview() {
    AnimationExample()
}










enum class BounceState { Pressed, Released }

@Preview(showBackground = true)
@Composable
fun PressEffect() {
    Button(onClick = {
        //Clicked
    }, shape = RoundedCornerShape(12.dp), contentPadding = PaddingValues(16.dp),
        modifier = Modifier.pressClickEffect()) {
        Text(text = "Click me")
    }
}
enum class ButtonState { Pressed, Idle }
fun Modifier.pressClickEffect() = composed {
    var buttonState by remember { mutableStateOf(ButtonState.Idle) }
    val ty by animateFloatAsState(if (buttonState == ButtonState.Pressed) 0f else -20f)

    this
        .graphicsLayer {
            translationY = ty
        }
        .clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = {  }
        )
        .pointerInput(buttonState) {
            awaitPointerEventScope {
                buttonState = if (buttonState == ButtonState.Pressed) {
                    waitForUpOrCancellation()
                    ButtonState.Idle
                } else {
                    awaitFirstDown(false)
                    ButtonState.Pressed
                }
            }
        }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun FadingText(isFading: State<Boolean>) {
    val ty by animateFloatAsState(if (isUp) 0f else -20f)

    val bounceOffset by animateIntOffsetAsState(
        targetValue = if (isUp) IntOffset(0, 20) else IntOffset.Zero,
        animationSpec = tween(
            durationMillis = 500,
            easing = CubicBezierEasing(0.36f, 0.0f, 0.66f, 1.0f)
        )
    )

    DisposableEffect(Unit) {
        onDispose {
            if (isUp) {
                // Reset isUp after the animation finishes
                isUp = false
            }
        }
    }

    LaunchedEffect(isUp) {
        if (isUp) {
            // Trigger the animation when isUp changes
            delay(500) // Adjust the delay based on your animation specs
            isUp = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .graphicsLayer {
                translationY = ty + bounceOffset.y.toFloat()
            }
            .background(if (alpha) Color.Blue else Color.Green)
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    // Handle tap event
                    isUp = true
                }
            }
    ) {
        Text(
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
            text = "^",
            modifier = Modifier.fillMaxSize().align(Alignment.TopCenter)
        )
    }
}

@Preview
@Composable
fun FiniteShake() {
    var isShaking by remember { mutableStateOf(false) }

    // Angle to rotate for the shake animation
    val angleOffset = 45f

    val x1 =  0.47f
    val y1 = -0.63f
    val x2 = 1f-x1
    val y2 = 1f+y1

    // Animation for the rotation
    val rotationAnim by animateFloatAsState(
        targetValue = if (isShaking) angleOffset else 0f,
        animationSpec = keyframes {
            durationMillis = 500
            // Keyframes for the rotation animation
            0f at 0 with CubicBezierEasing(x1, y1, x2, y2)
            0f at 100 // Initial position, no overshoot
            -angleOffset * 0.25f at ((durationMillis*0.25).toInt()  ) with CubicBezierEasing(x1, y1, x2, y2) // Quick move to -45 degrees
             angleOffset * 0.25f at ((durationMillis*0.25).toInt()*2) with CubicBezierEasing(x1, y1, x2, y2) // Return to 45 degrees
            0f at durationMillis with CubicBezierEasing(x1, y1, x2, y2) // Return to 0 degrees

        }
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        // Effect to reset isShaking after the animation finishes
        LaunchedEffect(isShaking) {
            if (isShaking) {
                delay(500) // Adjust the duration based on your animation specs
                isShaking = false
            }
        }

        // Box containing the shaking element
        Box(
            modifier = Modifier
                .size(125.dp)
                .rotate(rotationAnim)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.Magenta)
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        // Handle tap event to start the shake animation
                        isShaking = true
                    }
                }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Button to manually trigger the shake animation
        Button(onClick = {
            // Activate shake animation on button click
            isShaking = true
        }) {
            Text("Activate Shake Animation")
        }
    }
}



@Preview
@Composable
fun springShake() {
    var isShaking by remember { mutableStateOf(false) }

    // Angle to rotate for the shake animation
    val angleOffset = 45f

    val x1 =  0.47f
    val y1 = -0.63f
    val x2 = 1f-x1
    val y2 = 1f+y1

    // Animation for the rotation
    val rotationAnim by animateFloatAsState(
        targetValue = if (isShaking) angleOffset else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessHigh
        )    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        // Effect to reset isShaking after the animation finishes
        LaunchedEffect(isShaking) {
            if (isShaking) {
                delay(500) // Adjust the duration based on your animation specs
                isShaking = false
            }
        }

        // Box containing the shaking element
        Box(
            modifier = Modifier
                .size(125.dp)
                .rotate(rotationAnim)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.Magenta)
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        // Handle tap event to start the shake animation
                        isShaking = true
                    }
                }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Button to manually trigger the shake animation
        Button(onClick = {
            // Activate shake animation on button click
            isShaking = true
        }) {
            Text("Activate Shake Animation")
        }
    }
}

