package com.ioffeivan.otp.core.model

import androidx.compose.runtime.Immutable

/**
 * Represents the length of an OTP (One-Time Password) input field.
 *
 * This value class encapsulates the length as an integer, ensuring it is positive through the initializer check.
 *
 * @param value The positive integer length of the OTP.
 */
@Immutable
@JvmInline
value class OtpLength(val value: Int) {
    init {
        require(value > 0) { "OtpLength must be positive" }
    }
}
