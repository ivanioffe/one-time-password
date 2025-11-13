package com.ioffeivan.otp.compose.ui.basic

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.ioffeivan.otp.compose.utils.RevealingMaskingOtpVisualTransformation
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RevealingMaskingOtpVisualTransformationTest {
    @get:Rule
    val rule = createComposeRule()

    private val mask = '\u2022'
    private val delay = 500L

    private val transformer =
        RevealingMaskingOtpVisualTransformation(
            mask = mask,
            revealDelayMs = delay,
        )

    @Test
    fun transform_whenAddingCharacter_shouldRevealThenMask() {
        val otp = mutableStateOf("")
        var actual = ""

        rule.setContent {
            actual = transformer.transform(otp.value).value
        }

        otp.value = "1"
        rule.waitForIdle()
        assertThat(actual).isEqualTo("1")

        rule.mainClock.advanceTimeBy(delay)
        assertThat(actual).isEqualTo(mask.toString().repeat(otp.value.length))
    }

    @Test
    fun transform_whenDeletingCharacter_shouldMaskImmediately() {
        val otp = mutableStateOf("123")
        val expected = { otp.value.length }

        var actual = ""
        rule.setContent {
            actual = transformer.transform(otp.value).value
        }

        rule.mainClock.advanceTimeBy(delay)
        assertThat(actual).isEqualTo(mask.toString().repeat(expected()))

        otp.value = "12"
        rule.waitForIdle()
        assertThat(actual).isEqualTo(mask.toString().repeat(expected()))
    }
}
