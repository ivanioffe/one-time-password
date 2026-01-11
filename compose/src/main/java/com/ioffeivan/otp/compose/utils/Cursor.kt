package com.ioffeivan.otp.compose.utils

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Dp
import kotlinx.coroutines.delay

private const val CURSOR_BLINKING_DURATION_MS = 500

/**
 * Defines the visual style of cursor blinking animation.
 *
 * - [BLINK]: Sharp on/off toggle without smooth transition.
 * - [FADE]: Smooth fade in/out using alpha animation.
 */
enum class CursorBlinkStyle {
    BLINK,
    FADE,
}

/**
 * Adds a blinking cursor modifier to the composable.
 * Draws a vertical line that blinks according to the specified style.
 *
 * @param color The color of the cursor line.
 * @param width The stroke width of the cursor line in Dp.
 * @param height The height of the cursor line in Dp.
 * @param blinkStyle The blinking style: [CursorBlinkStyle.BLINK] for sharp toggle or [CursorBlinkStyle.FADE] for smooth fade.
 * @param blinkDurationMs The duration of each blink cycle in milliseconds.
 */
fun Modifier.cursor(
    color: Color,
    width: Dp,
    height: Dp,
    blinkStyle: CursorBlinkStyle = CursorBlinkStyle.BLINK,
    blinkDurationMs: Int = CURSOR_BLINKING_DURATION_MS,
): Modifier =
    composed {
        val alpha by when (blinkStyle) {
            CursorBlinkStyle.BLINK -> {
                var visible by remember { mutableStateOf(true) }
                LaunchedEffect(Unit) {
                    while (true) {
                        delay(blinkDurationMs.toLong())
                        visible = !visible
                    }
                }

                remember { derivedStateOf { if (visible) 1f else 0f } }
            }

            CursorBlinkStyle.FADE -> {
                val transition = rememberInfiniteTransition("cursor_blink")

                transition.animateFloat(
                    initialValue = 1f,
                    targetValue = 0f,
                    animationSpec =
                        infiniteRepeatable(
                            animation =
                                tween(
                                    durationMillis = blinkDurationMs,
                                    easing = LinearEasing,
                                ),
                            RepeatMode.Reverse,
                        ),
                    label = "cursor_alpha",
                )
            }
        }

        drawBehind {
            drawCursor(color, width, height, alpha)
        }
    }

/**
 * Draws a vertical cursor line centered horizontally and vertically within the draw scope.
 *
 * The line is positioned at the horizontal center and vertically centered
 * based on the provided height.
 *
 * @param color The color of the cursor line.
 * @param width The stroke width of the cursor line in Dp.
 * @param height The height of the cursor line in Dp.
 * @param alpha The opacity of the cursor line, ranging from 0f (transparent) to 1f (opaque).
 */
private fun DrawScope.drawCursor(
    color: Color,
    width: Dp,
    height: Dp,
    alpha: Float,
) {
    val x = size.width / 2
    val startY = (size.height - height.toPx()) / 2
    val endY = startY + height.toPx()

    drawLine(
        color = color,
        start = Offset(x = x, y = startY),
        end = Offset(x = x, y = endY),
        strokeWidth = width.toPx(),
        alpha = alpha,
        cap = StrokeCap.Round,
    )
}
