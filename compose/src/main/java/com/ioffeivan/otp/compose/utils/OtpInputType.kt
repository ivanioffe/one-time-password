package com.ioffeivan.otp.compose.utils

import androidx.compose.ui.text.input.KeyboardType

/**
 * Defines the contract for configuring the behavior of an OTP (One-Time Password) input field.
 *
 * Classes implementing this interface specify:
 * 1. The virtual keyboard type to be displayed.
 * 2. The validation rules for the text entered.
 *
 * This allows consumers to easily define custom input types beyond the standard set.
 */
interface OtpInputType {
    /**
     * The Compose [KeyboardType] that should be suggested to the user
     * for entering the OTP.
     */
    val keyboardType: KeyboardType

    /**
     * Executes the validation check against the currently entered OTP string.
     *
     * @param otp The current string entered by the user.
     * @return `true` if the string meets the input rules, otherwise `false`.
     */
    fun isValid(otp: String): Boolean
}

/**
 * Provides a set of standard, out-of-the-box implementations for [OtpInputType].
 *
 * Consumers should use these values for common use cases like requiring only digits or letters.
 */
enum class StandardOtpInputType : OtpInputType {
    /**
     * Accepts only numeric digits (0-9).
     * Displays a decimal keyboard ([KeyboardType.Decimal]).
     */
    DIGITS {
        override val keyboardType: KeyboardType = KeyboardType.Decimal

        override fun isValid(otp: String): Boolean = otp.all { it.isDigit() }
    },

    /**
     * Accepts only letters (alphabetic characters).
     * Displays a standard text keyboard ([KeyboardType.Text]).
     */
    LETTERS {
        override val keyboardType: KeyboardType = KeyboardType.Text

        override fun isValid(otp: String): Boolean = otp.all { it.isLetter() }
    },
}
