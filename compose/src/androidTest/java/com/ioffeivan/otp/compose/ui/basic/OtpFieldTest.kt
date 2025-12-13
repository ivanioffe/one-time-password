package com.ioffeivan.otp.compose.ui.basic

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.text.input.KeyboardType
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.ioffeivan.otp.compose.utils.StandardOtpInputType
import com.ioffeivan.otp.compose.utils.filter
import com.ioffeivan.otp.core.model.OtpLength
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

// This will be removed in future releases.
@RunWith(AndroidJUnit4::class)
class DeprecatedOtpFieldTest {
    @get:Rule
    val rule = createComposeRule()

    @Test
    fun displaysCorrectNumberOfCells() {
        with(rule) {
            val length = OtpLength(5)

            setContent {
                OtpField(
                    otp = "",
                    length = length,
                    onOtpChange = {},
                ) { otpCell ->
                    Text(
                        text = otpCell.value.toString(),
                        modifier =
                            Modifier.Companion
                                .testTag("OtpCell"),
                    )
                }
            }

            onAllNodesWithTag("OtpCell", useUnmergedTree = true)
                .assertCountEquals(length.value)
        }
    }

    @Test
    fun displaysCorrectValueInCells() {
        with(rule) {
            setContent {
                OtpField(
                    otp = "123",
                    onOtpChange = {},
                    length = OtpLength(3),
                ) { otpCell ->
                    Text(
                        text = otpCell.value.toString(),
                        modifier =
                            Modifier.Companion
                                .testTag("OtpCell_${otpCell.position}"),
                    )
                }
            }

            onNodeWithTag("OtpCell_1", useUnmergedTree = true)
                .assertTextEquals("1")

            onNodeWithTag("OtpCell_2", useUnmergedTree = true)
                .assertTextEquals("2")

            onNodeWithTag("OtpCell_3", useUnmergedTree = true)
                .assertTextEquals("3")
        }
    }

    @Test
    fun displaysOnlyDigitsWhenInputTypeIsDIGITS() {
        with(rule) {
            val otpState = mutableStateOf("")

            setContent {
                OtpField(
                    otp = otpState.value,
                    onOtpChange = { otpState.value = it },
                    length = OtpLength(4),
                    inputType = StandardOtpInputType.DIGITS,
                ) { otpCell ->
                    Text(
                        text = otpCell.value.toString(),
                        modifier = Modifier.Companion.testTag("OtpCell_${otpCell.position}"),
                    )
                }
            }

            onNodeWithTag("OtpField")
                .performTextInput("123")

            onNodeWithTag("OtpCell_1", useUnmergedTree = true)
                .assertTextEquals("1")

            onNodeWithTag("OtpCell_2", useUnmergedTree = true)
                .assertTextEquals("2")

            onNodeWithTag("OtpCell_3", useUnmergedTree = true)
                .assertTextEquals("3")
        }
    }

    @Test
    fun ignoresInvalidInputWhenInputTypeIsDIGITS() {
        with(rule) {
            val otpState = mutableStateOf("")

            setContent {
                OtpField(
                    otp = otpState.value,
                    onOtpChange = { otpState.value = it },
                    length = OtpLength(4),
                    inputType = StandardOtpInputType.DIGITS,
                ) { otpCell ->
                    Text(
                        text = otpCell.value.toString(),
                        modifier = Modifier.Companion.testTag("OtpCell_${otpCell.position}"),
                    )
                }
            }

            onNodeWithTag("OtpField")
                .performTextInput("12a")

            onNodeWithTag("OtpCell_1", useUnmergedTree = true)
                .assertTextEquals(" ")

            onNodeWithTag("OtpCell_2", useUnmergedTree = true)
                .assertTextEquals(" ")

            onNodeWithTag("OtpCell_3", useUnmergedTree = true)
                .assertTextEquals(" ")
        }
    }

    @Test
    fun displaysOnlyLettersWhenInputTypeIsLETTERS() {
        with(rule) {
            val otpState = mutableStateOf("")

            setContent {
                OtpField(
                    otp = otpState.value,
                    onOtpChange = { otpState.value = it },
                    length = OtpLength(4),
                    inputType = StandardOtpInputType.LETTERS,
                ) { otpCell ->
                    Text(
                        text = otpCell.value.toString(),
                        modifier = Modifier.Companion.testTag("OtpCell_${otpCell.position}"),
                    )
                }
            }

            onNodeWithTag("OtpField")
                .performTextInput("abc")

            onNodeWithTag("OtpCell_1", useUnmergedTree = true)
                .assertTextEquals("a")

            onNodeWithTag("OtpCell_2", useUnmergedTree = true)
                .assertTextEquals("b")

            onNodeWithTag("OtpCell_3", useUnmergedTree = true)
                .assertTextEquals("c")
        }
    }

    @Test
    fun ignoresInvalidInputWhenInputTypeIsLETTERS() {
        with(rule) {
            val otpState = mutableStateOf("")

            setContent {
                OtpField(
                    otp = otpState.value,
                    onOtpChange = { otpState.value = it },
                    length = OtpLength(4),
                    inputType = StandardOtpInputType.LETTERS,
                ) { otpCell ->
                    Text(
                        text = otpCell.value.toString(),
                        modifier = Modifier.Companion.testTag("OtpCell_${otpCell.position}"),
                    )
                }
            }

            onNodeWithTag("OtpField")
                .performTextInput("a;c")

            onNodeWithTag("OtpCell_1", useUnmergedTree = true)
                .assertTextEquals(" ")

            onNodeWithTag("OtpCell_2", useUnmergedTree = true)
                .assertTextEquals(" ")

            onNodeWithTag("OtpCell_3", useUnmergedTree = true)
                .assertTextEquals(" ")
        }
    }

    @Test
    fun ignoreInputWhenOtpLengthIsMax() {
        with(rule) {
            val otp = "1234"
            val otpState = mutableStateOf(otp)

            setContent {
                OtpField(
                    otp = otpState.value,
                    onOtpChange = { otpState.value = it },
                    length = OtpLength(4),
                    inputType = StandardOtpInputType.LETTERS,
                ) { otpCell ->
                    Text(
                        text = otpCell.value.toString(),
                        modifier = Modifier.Companion.testTag("OtpCell_${otpCell.position}"),
                    )
                }
            }

            onNodeWithTag("OtpField")
                .performTextInput("123")

            assertThat(otpState.value).isEqualTo(otp)
        }
    }

    @Test
    fun truncatesInputWhenLengthIsExceeded(): Unit =
        with(rule) {
            val otpState = mutableStateOf("")

            setContent {
                OtpField(
                    otp = otpState.value,
                    onOtpChange = { otpState.value = it },
                    length = OtpLength(4),
                ) { otpCell ->
                    Text(
                        text = otpCell.value.toString(),
                        modifier = Modifier.Companion.testTag("OtpCell_${otpCell.position}"),
                    )
                }
            }

            onNodeWithTag("OtpField")
                .performTextInput("12345")

            onNodeWithTag("OtpCell_1", useUnmergedTree = true)
                .assertTextEquals("1")

            onNodeWithTag("OtpCell_2", useUnmergedTree = true)
                .assertTextEquals("2")

            onNodeWithTag("OtpCell_3", useUnmergedTree = true)
                .assertTextEquals("3")

            onNodeWithTag("OtpCell_4", useUnmergedTree = true)
                .assertTextEquals("4")
        }

    @Test
    fun disabledWhenEnabledIsFalse(): Unit =
        with(rule) {
            setContent {
                OtpField(
                    otp = "",
                    length = OtpLength(4),
                    onOtpChange = {},
                    enabled = false,
                ) {}
            }

            onNodeWithTag("OtpField")
                .assertIsNotEnabled()
        }
}

@RunWith(AndroidJUnit4::class)
class OtpFieldTest {
    @get:Rule
    val rule = createComposeRule()

    @Test
    fun displaysCorrectNumberOfCells(): Unit =
        with(rule) {
            val length = OtpLength(5)

            setContent {
                OtpField(
                    otp = "",
                    length = length,
                    onOtpChange = {},
                    keyboardOptions =
                        KeyboardOptions(
                            keyboardType = KeyboardType.NumberPassword,
                        ),
                ) { otpCell ->
                    Text(
                        text = otpCell.value.toString(),
                        modifier =
                            Modifier.Companion
                                .testTag("OtpCell"),
                    )
                }
            }

            onAllNodesWithTag("OtpCell", useUnmergedTree = true)
                .assertCountEquals(length.value)
        }

    @Test
    fun displaysCorrectValueInCells(): Unit =
        with(rule) {
            setContent {
                OtpField(
                    otp = "123",
                    length = OtpLength(3),
                    onOtpChange = {},
                    keyboardOptions =
                        KeyboardOptions(
                            keyboardType = KeyboardType.NumberPassword,
                        ),
                ) { otpCell ->
                    Text(
                        text = otpCell.value.toString(),
                        modifier =
                            Modifier.Companion
                                .testTag("OtpCell_${otpCell.position}"),
                    )
                }
            }

            onNodeWithTag("OtpCell_1", useUnmergedTree = true)
                .assertTextEquals("1")

            onNodeWithTag("OtpCell_2", useUnmergedTree = true)
                .assertTextEquals("2")

            onNodeWithTag("OtpCell_3", useUnmergedTree = true)
                .assertTextEquals("3")
        }

    @Test
    fun whenOtpLengthIsMax_ignoreInput(): Unit =
        with(rule) {
            val otp = "1234"
            val otpState = mutableStateOf(otp)

            setContent {
                OtpField(
                    otp = otpState.value,
                    length = OtpLength(4),
                    onOtpChange = { otpState.value = it },
                    keyboardOptions =
                        KeyboardOptions(
                            keyboardType = KeyboardType.NumberPassword,
                        ),
                ) { otpCell ->
                    Text(
                        text = otpCell.value.toString(),
                        modifier = Modifier.Companion.testTag("OtpCell_${otpCell.position}"),
                    )
                }
            }

            onNodeWithTag("OtpField")
                .performTextInput("123")

            assertThat(otpState.value).isEqualTo(otp)
        }

    @Test
    fun whenFilterIsAppliedAndValidInput_acceptsInput(): Unit =
        with(rule) {
            val otp = ""
            val otpState = mutableStateOf(otp)

            setContent {
                OtpField(
                    otp = otp,
                    length = OtpLength(4),
                    onOtpChange = {
                        otpState.value = it
                    },
                    inputTransformation =
                        InputTransformation
                            .filter { otp -> otp.all { it.isDigit() } },
                    keyboardOptions =
                        KeyboardOptions(
                            keyboardType = KeyboardType.NumberPassword,
                        ),
                ) { otpCell ->
                    Text(
                        text = otpCell.value.toString(),
                        modifier = Modifier.Companion.testTag("OtpCell_${otpCell.position}"),
                    )
                }
            }

            onNodeWithTag("OtpField")
                .performTextInput("123")

            assertThat(otpState.value).isEqualTo(otp)
        }

    @Test
    fun whenFilterIsAppliedAndInValidInput_rejectsInput(): Unit =
        with(rule) {
            val otp = ""
            val otpState = mutableStateOf(otp)

            setContent {
                OtpField(
                    otp = otp,
                    length = OtpLength(4),
                    onOtpChange = {
                        otpState.value = it
                    },
                    inputTransformation =
                        InputTransformation
                            .filter { otp -> otp.all { it.isDigit() } },
                    keyboardOptions =
                        KeyboardOptions(
                            keyboardType = KeyboardType.NumberPassword,
                        ),
                ) { otpCell ->
                    Text(
                        text = otpCell.value.toString(),
                        modifier = Modifier.Companion.testTag("OtpCell_${otpCell.position}"),
                    )
                }
            }

            onNodeWithTag("OtpField")
                .performTextInput("12c")

            assertThat(otpState.value).isEqualTo("")
        }

    @Test
    fun whenEnabledIsFalse_disabled(): Unit =
        with(rule) {
            setContent {
                OtpField(
                    otp = "",
                    length = OtpLength(4),
                    onOtpChange = {},
                    enabled = false,
                    keyboardOptions =
                        KeyboardOptions(
                            keyboardType = KeyboardType.NumberPassword,
                        ),
                ) {}
            }

            onNodeWithTag("OtpField")
                .assertIsNotEnabled()
        }
}
