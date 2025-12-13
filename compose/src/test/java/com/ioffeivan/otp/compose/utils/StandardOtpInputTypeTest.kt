package com.ioffeivan.otp.compose.utils

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

// This will be removed in future releases.
class StandardOtpInputTypeTest {
    @Nested
    inner class DigitsOtpInputTypeTest {
        private val type = StandardOtpInputType.DIGITS

        @Test
        fun isValid_whenOtpContainsOnlyDigits_returnsTrue() {
            val otp = "12345"
            val result = type.isValid(otp)

            assertThat(result).isTrue()
        }

        @Test
        fun isValid_whenOtpContainsNotOnlyDigits_returnsFalse() {
            val otp = "a2c45"
            val result = type.isValid(otp)

            assertThat(result).isFalse()
        }

        // Must be true to allow the user to clear the input field.
        @Test
        fun isValid_whenOtpIsEmpty_returnsTrue() {
            val otp = ""
            val result = type.isValid(otp)

            assertThat(result).isTrue()
        }
    }

    @Nested
    inner class LettersOtpInputTypeTest {
        private val type = StandardOtpInputType.LETTERS

        @Test
        fun isValid_whenOtpContainsOnlyLetters_returnsTrue() {
            val otp = "abcde"
            val result = type.isValid(otp)

            assertThat(result).isTrue()
        }

        @Test
        fun isValid_whenOtpContainsNotOnlyLetters_returnsFalse() {
            val otp = "ab3@d"
            val result = type.isValid(otp)

            assertThat(result).isFalse()
        }

        // Must be true to allow the user to clear the input field.
        @Test
        fun isValid_whenOtpIsEmpty_returnsTrue() {
            val otp = ""
            val result = type.isValid(otp)

            assertThat(result).isTrue()
        }
    }
}
