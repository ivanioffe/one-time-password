package com.ioffeivan.otp.core.model

import androidx.compose.runtime.Immutable

/**
 * Represents a single cell in an OTP (One-Time Password) input field.
 *
 * This data class encapsulates the state of an individual OTP digit cell.
 *
 * @param value The current value (char or empty string) displayed in the cell.
 * @param position The 1-based index of the cell in the OTP input sequence.
 * @param isFocused Indicates whether this cell is currently focused.
 */
@Immutable
data class OtpCell(
    val value: Char,
    val position: Int,
    val isFocused: Boolean,
)
