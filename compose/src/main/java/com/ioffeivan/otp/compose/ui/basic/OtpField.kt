package com.ioffeivan.otp.compose.ui.basic

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.ioffeivan.otp.compose.utils.OtpInputType
import com.ioffeivan.otp.compose.utils.OtpVisualTransformation
import com.ioffeivan.otp.compose.utils.StandardOtpInputType
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
 * @param inputType The type of input allowed, defined by [OtpInputType]. Defaults to [StandardOtpInputType.DIGITS].
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
    inputType: OtpInputType = StandardOtpInputType.DIGITS,
    visualTransformation: OtpVisualTransformation? = null,
    content: @Composable RowScope.(otpCell: OtpCell) -> Unit,
) {
    val transformedOtp by visualTransformation?.transform(otp)
        ?: rememberUpdatedState(otp)

    CompositionLocalProvider(LocalTextSelectionColors provides textSelectionColors) {
        BasicTextField(
            value = TextFieldValue(otp, TextRange(otp.length)),
            onValueChange = {
                val newOtp = it.text.take(length.value)

                if (newOtp != otp && inputType.isValid(newOtp)) {
                    onOtpChange(newOtp)
                }
            },
            enabled = enabled,
            textStyle = LocalTextStyle.current.copy(color = Color.Transparent),
            keyboardOptions =
                KeyboardOptions(
                    keyboardType = inputType.keyboardType,
                    imeAction = ImeAction.Done,
                ),
            keyboardActions =
                KeyboardActions(
                    onDone = {},
                ),
            singleLine = true,
            cursorBrush = SolidColor(Color.Transparent),
            modifier =
                modifier
                    .testTag("OtpField"),
            decorationBox = { innerTextField ->
                Box(
                    modifier =
                        Modifier
                            .size(0.dp),
                ) {
                    innerTextField()
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                ) {
                    repeat(length.value) { index ->
                        val otpCellValue = transformedOtp.getOrNull(index) ?: ' '
                        val isFocused = index == otp.length
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
