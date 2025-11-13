package com.ioffeivan.otp.compose.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import kotlinx.coroutines.delay

private const val DEFAULT_MASK = '\u2022'

/**
 * Interface for custom visual transformations applied to an OTP input field.
 */
interface OtpVisualTransformation {
    /**
     * Transforms the plain [otp] string into a visually modified representation.
     *
     * @param otp The current, untransformed OTP value.
     * @return A [State] containing the visually transformed OTP string. This state is observed
     * by the UI component.
     */
    @Composable
    fun transform(otp: String): State<String>
}

/**
 * A simple visual transformation that masks all characters of the OTP immediately.
 *
 * This implementation is stateless and returns the masked string synchronously,
 * wrapped in a [State].
 *
 * @property mask The character used to mask the input (default is [DEFAULT_MASK]).
 */
class MaskingOtpVisualTransformation(
    private val mask: Char = DEFAULT_MASK,
) : OtpVisualTransformation {
    @Composable
    override fun transform(otp: String): State<String> {
        val transformed = mask.toString().repeat(otp.length)

        return rememberUpdatedState(transformed)
    }
}

/**
 * A visual transformation that temporarily reveals the last entered character before masking it
 * after a specified delay.
 *
 * This transformation manages asynchronous behavior using a [LaunchedEffect] and tracks
 * the previous state to distinguish between adding a character (reveal/delay needed)
 * and deleting a character (immediate mask needed).
 *
 * @property mask The character used to mask the input (default is [DEFAULT_MASK]).
 * @property revealDelayMs The duration in milliseconds the last character remains visible before
 * being masked.
 */
class RevealingMaskingOtpVisualTransformation(
    private val mask: Char = DEFAULT_MASK,
    private val revealDelayMs: Long = 500L,
) : OtpVisualTransformation {
    @Composable
    override fun transform(otp: String): State<String> {
        val transformed = remember { mutableStateOf("") }
        val previousOtp = remember { mutableStateOf(otp) }

        LaunchedEffect(key1 = otp) {
            if (otp.isEmpty()) {
                transformed.value = ""
                return@LaunchedEffect
            }

            val isDeleting = otp.length < previousOtp.value.length

            previousOtp.value = otp

            if (!isDeleting) {
                val prefix = mask.toString().repeat(otp.length - 1)
                val lastChar = otp.last()
                transformed.value = prefix + lastChar

                delay(revealDelayMs)
            }

            transformed.value = mask.toString().repeat(otp.length)
        }

        return transformed
    }
}
