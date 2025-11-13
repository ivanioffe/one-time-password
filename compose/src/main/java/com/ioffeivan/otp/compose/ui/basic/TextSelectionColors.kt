package com.ioffeivan.otp.compose.ui.basic

import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.ui.graphics.Color

/**
 * Custom text selection colors for the OTP field, making the selection handle and background transparent.
 */
internal val textSelectionColors =
    TextSelectionColors(
        handleColor = Color.Transparent,
        backgroundColor = Color.Transparent,
    )
