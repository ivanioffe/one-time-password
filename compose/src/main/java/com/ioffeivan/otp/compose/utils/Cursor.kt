package com.ioffeivan.otp.compose.utils

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Dp

/**
 * Draws a vertical cursor line centered horizontally and vertically within the draw scope.
 *
 * The line is positioned at the horizontal center and vertically centered
 * based on the provided height.
 *
 * @param color The color of the cursor line.
 * @param width The stroke width of the line in Dp.
 * @param height The height of the line in Dp.
 * @param alpha The opacity of the line, ranging from 0f (transparent) to 1f (opaque). Defaults to 1f.
 */
fun DrawScope.drawCursor(
    color: Color,
    width: Dp,
    height: Dp,
    alpha: Float = 1f,
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
