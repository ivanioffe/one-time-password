package com.ioffeivan.otp.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.KeyboardActionHandler
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.maxLength
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import com.ioffeivan.otp.compose.internal.textSelectionColors
import com.ioffeivan.otp.core.model.OtpCell
import com.ioffeivan.otp.core.model.OtpLength

/**
 * A composable function that creates an OTP (One-Time Password) input field with customizable cells.
 *
 * @param otp The current OTP string value.
 * @param length The expected length of the OTP, defined by [OtpLength].
 * @param onOtpChange Callback invoked when the OTP value changes, providing the new OTP string.
 * @param modifier Optional [Modifier] to apply to the text field.
 * @param enabled Whether the OTP field is enabled for user input. Defaults to true.
 * @param inputTransformation The input transformation to apply, such as filtering or limiting input. Defaults to [InputTransformation].
 * @param keyboardOptions The keyboard options for the input field. Defaults to [KeyboardOptions.Default].
 * @param keyboardActionHandler Optional handler for keyboard actions.
 * @param visualTransformation An optional custom transformation to visually modify the OTP value
 * displayed in the cells (e.g., masking or delayed revealing). If null, the raw OTP is shown.
 * @param content A composable lambda that defines the appearance of each OTP cell, receiving an [OtpCell] parameter.
 */
@Composable
fun OtpField(
    otp: String,
    length: OtpLength,
    onOtpChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    inputTransformation: InputTransformation = InputTransformation,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActionHandler: KeyboardActionHandler? = null,
    visualTransformation: OtpVisualTransformation? = null,
    content: @Composable RowScope.(otpCell: OtpCell) -> Unit,
) {
    val state = rememberTextFieldState(otp)

    val transformedOtp by visualTransformation?.transform(otp)
        ?: rememberUpdatedState(otp)

    LaunchedEffect(state) {
        snapshotFlow { state.text.toString() }
            .collect {
                onOtpChange(it)
            }
    }

    LaunchedEffect(otp) {
        val otpStateValue = state.text.toString()

        if (otpStateValue != otp) {
            state.edit {
                replace(0, otpStateValue.length, otp)
            }
        }
    }

    CompositionLocalProvider(LocalTextSelectionColors provides textSelectionColors) {
        BasicTextField(
            state = state,
            enabled = enabled,
            inputTransformation = inputTransformation.maxLength(length.value),
            textStyle = LocalTextStyle.current.copy(color = Color.Transparent),
            keyboardOptions = keyboardOptions,
            onKeyboardAction = keyboardActionHandler,
            lineLimits = TextFieldLineLimits.SingleLine,
            cursorBrush = SolidColor(Color.Transparent),
            modifier =
                modifier
                    .testTag("OtpField"),
            decorator = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                ) {
                    repeat(length.value) { index ->
                        val otpCellValue = transformedOtp.getOrElse(index) { ' ' }
                        val isFocused = index == transformedOtp.length
                        val position = index + 1

                        content(
                            OtpCell(
                                value = otpCellValue,
                                position = position,
                                isFocused = isFocused,
                            ),
                        )
                    }
                }
            },
        )
    }
}
