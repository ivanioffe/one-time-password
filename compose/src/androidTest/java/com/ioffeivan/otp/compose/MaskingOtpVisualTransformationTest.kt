package com.ioffeivan.otp.compose

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MaskingOtpVisualTransformationTest {
    @get:Rule
    val rule = createComposeRule()

    private val mask = '\u2022'
    private val transformer = MaskingOtpVisualTransformation(mask)

    @Test
    fun transform_shouldMasking() {
        val otp = "123"
        val expected = mask.toString().repeat(otp.length)

        var actual = ""
        rule.setContent {
            actual = transformer.transform(otp).value
        }

        assertThat(actual).isEqualTo(expected)
    }
}
