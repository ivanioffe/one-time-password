package com.ioffeivan.otp

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ioffeivan.otp.compose.MaskingOtpVisualTransformation
import com.ioffeivan.otp.compose.OtpField
import com.ioffeivan.otp.compose.RevealingMaskingOtpVisualTransformation
import com.ioffeivan.otp.compose.cursor.CursorBlinkStyle
import com.ioffeivan.otp.compose.cursor.cursor
import com.ioffeivan.otp.core.model.OtpCell
import com.ioffeivan.otp.core.model.OtpLength
import com.ioffeivan.otp.ui.theme.OneTimePasswordTheme

enum class VisualTransformationType { None, Mask, RevealingMask }

@Composable
fun OtpFieldPlaygroundScreen(
    modifier: Modifier = Modifier,
) {
    var otp by remember { mutableStateOf("") }

    var isEnabled by remember { mutableStateOf(true) }
    var otpLength by remember { mutableStateOf(OtpLength(5)) }
    var contentSpacing by remember { mutableFloatStateOf(8f) }
    var fieldWidthPercent by remember { mutableFloatStateOf(100f) }
    var fieldHeight by remember { mutableFloatStateOf(100f) }

    var useAdaptiveWidth by remember { mutableStateOf(true) }
    var cellWidth by remember { mutableFloatStateOf(55f) }
    var cellHeight by remember { mutableFloatStateOf(55f) }

    var visualTransformationType by remember { mutableStateOf(VisualTransformationType.None) }
    var revealDelay by remember { mutableFloatStateOf(500f) }

    var cursorEnabled by remember { mutableStateOf(true) }
    var cursorWidth by remember { mutableFloatStateOf(2f) }
    var cursorHeight by remember { mutableFloatStateOf(24f) }
    var cursorBlinkStyle by remember { mutableStateOf(CursorBlinkStyle.BLINK) }
    var cursorBlinkDuration by remember { mutableFloatStateOf(500f) }

    val visualTransformation =
        when (visualTransformationType) {
            VisualTransformationType.None -> null
            VisualTransformationType.Mask -> MaskingOtpVisualTransformation()
            VisualTransformationType.RevealingMask ->
                RevealingMaskingOtpVisualTransformation(
                    revealDelayMs = revealDelay.toLong(),
                )
        }

    var expandedField by remember { mutableStateOf(true) }
    var expandedBasic by remember { mutableStateOf(true) }
    var expandedCell by remember { mutableStateOf(true) }
    var expandedCursor by remember { mutableStateOf(true) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier =
            modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(175.dp)
                    .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
                    .background(
                        brush =
                            Brush.verticalGradient(
                                colors =
                                    listOf(
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                                        MaterialTheme.colorScheme.surface,
                                    ),
                            ),
                    ),
        ) {
            OtpField(
                otp = otp,
                length = otpLength,
                onOtpChange = { otp = it },
                enabled = isEnabled,
                contentSpacing = contentSpacing.dp,
                visualTransformation = visualTransformation,
                modifier =
                    Modifier
                        .fillMaxWidth(fieldWidthPercent / 100f)
                        .height(fieldHeight.dp)
                        .padding(horizontal = 16.dp),
                content = { cell ->
                    val cursorModifier =
                        if (cell.isFocused && cursorEnabled) {
                            Modifier.cursor(
                                color = MaterialTheme.colorScheme.primary,
                                width = cursorWidth.dp,
                                height = cursorHeight.dp,
                                blinkStyle = cursorBlinkStyle,
                                blinkDurationMs = cursorBlinkDuration.toInt(),
                            )
                        } else {
                            Modifier
                        }

                    val useAdaptiveWidthModifier =
                        if (useAdaptiveWidth) {
                            Modifier.weight(1f)
                        } else {
                            Modifier.width(cellWidth.dp)
                        }

                    key(cursorBlinkDuration) {
                        OtpItem(
                            otpCell = cell,
                            modifier =
                                Modifier
                                    .height(cellHeight.dp)
                                    .then(useAdaptiveWidthModifier)
                                    .clip(RoundedCornerShape(12.dp))
                                    .border(
                                        border =
                                            BorderStroke(
                                                width = if (cell.isFocused) 2.dp else 1.dp,
                                                color =
                                                    if (cell.isFocused) {
                                                        MaterialTheme.colorScheme.primary
                                                    } else {
                                                        MaterialTheme.colorScheme.outline.copy(
                                                            alpha = 0.3f,
                                                        )
                                                    },
                                            ),
                                        shape = RoundedCornerShape(12.dp),
                                    )
                                    .background(MaterialTheme.colorScheme.surface)
                                    .then(cursorModifier),
                        )
                    }
                },
            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier =
                Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
                    .animateContentSize(),
        ) {
            ExpandableSettingsGroup(
                title = stringResource(R.string.section_dimensions),
                expanded = expandedField,
                onExpandedChange = { expandedField = it },
            ) {
                SliderSetting(
                    label = stringResource(R.string.label_field_width),
                    value = fieldWidthPercent,
                    valueRange = 50f..100f,
                    valueSuffix = "%",
                    onValueChange = { fieldWidthPercent = it },
                )

                SliderSetting(
                    label = stringResource(R.string.label_field_height),
                    value = fieldHeight,
                    valueRange = 60f..150f,
                    valueSuffix = " dp",
                    onValueChange = { fieldHeight = it },
                )
            }

            ExpandableSettingsGroup(
                title = stringResource(R.string.section_basic_params),
                expanded = expandedBasic,
                onExpandedChange = { expandedBasic = it },
            ) {
                ChoiceSettings(
                    label = stringResource(R.string.label_code_length),
                    options = listOf(4, 5, 6),
                    selectedOption = otpLength.value,
                    onOptionSelected = {
                        otpLength = OtpLength(it)
                        otp = ""
                    },
                    optionLabel = { "$it" },
                )

                ToggleSetting(
                    label = stringResource(R.string.label_enabled),
                    checked = isEnabled,
                    onCheckedChange = { isEnabled = it },
                )

                SliderSetting(
                    label = stringResource(R.string.label_content_spacing),
                    value = contentSpacing,
                    valueRange = 0f..48f,
                    valueSuffix = " dp",
                    onValueChange = { contentSpacing = it },
                )

                ChoiceSettings(
                    label = stringResource(R.string.section_visual_transformation),
                    options = VisualTransformationType.entries,
                    selectedOption = visualTransformationType,
                    onOptionSelected = { visualTransformationType = it },
                    optionLabel = { it.name },
                )

                AnimatedVisibility(
                    visible = visualTransformationType == VisualTransformationType.RevealingMask,
                ) {
                    SliderSetting(
                        label = stringResource(R.string.label_mask_delay),
                        value = revealDelay,
                        valueRange = 100f..2000f,
                        valueSuffix = " ms",
                        onValueChange = { revealDelay = it },
                    )
                }
            }

            ExpandableSettingsGroup(
                title = stringResource(R.string.section_cell_params),
                expanded = expandedCell,
                onExpandedChange = { expandedCell = it },
            ) {
                ToggleSetting(
                    label = stringResource(R.string.label_adaptive_width),
                    checked = useAdaptiveWidth,
                    onCheckedChange = { useAdaptiveWidth = it },
                )

                if (!useAdaptiveWidth) {
                    SliderSetting(
                        label = stringResource(R.string.label_cell_width),
                        value = cellWidth,
                        valueRange = 30f..80f,
                        valueSuffix = " dp",
                        onValueChange = { cellWidth = it },
                    )
                }

                SliderSetting(
                    label = stringResource(R.string.label_cell_height),
                    value = cellHeight,
                    valueRange = 40f..100f,
                    valueSuffix = " dp",
                    onValueChange = { cellHeight = it },
                )
            }

            ExpandableSettingsGroup(
                title = stringResource(R.string.section_cursor_settings),
                expanded = expandedCursor,
                onExpandedChange = { expandedCursor = it },
            ) {
                ToggleSetting(
                    label = stringResource(R.string.label_cursor_active),
                    checked = cursorEnabled,
                    onCheckedChange = { cursorEnabled = it },
                )

                if (cursorEnabled) {
                    SliderSetting(
                        label = stringResource(R.string.label_cursor_thickness),
                        value = cursorWidth,
                        valueRange = 1f..5f,
                        valueSuffix = " dp",
                        onValueChange = { cursorWidth = it },
                    )

                    SliderSetting(
                        label = stringResource(R.string.label_cursor_height),
                        value = cursorHeight,
                        valueRange = 10f..40f,
                        valueSuffix = " dp",
                        onValueChange = { cursorHeight = it },
                    )

                    SliderSetting(
                        label = stringResource(R.string.label_blink_duration),
                        value = cursorBlinkDuration,
                        valueRange = 200f..1500f,
                        valueSuffix = " ms",
                        onValueChange = { cursorBlinkDuration = it },
                    )

                    ChoiceSettings(
                        label = stringResource(R.string.label_blink_style),
                        options = CursorBlinkStyle.entries,
                        selectedOption = cursorBlinkStyle,
                        onOptionSelected = { cursorBlinkStyle = it },
                        optionLabel = { it.name },
                    )
                }
            }
        }
    }
}

@Composable
fun OtpItem(
    otpCell: OtpCell,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier,
    ) {
        Text(
            text = otpCell.value.toString(),
            style =
                MaterialTheme.typography.headlineMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 24.sp,
                ),
        )
    }
}

@Composable
fun ExpandableSettingsGroup(
    title: String,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    ElevatedCard(
        colors =
            CardDefaults.elevatedCardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f),
            ),
        elevation = CardDefaults.elevatedCardElevation(0.dp),
        modifier =
            modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .animateContentSize(),
    ) {
        Column(
            modifier =
                Modifier
                    .padding(16.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .clickable { onExpandedChange(!expanded) },
            ) {
                Text(
                    text = title,
                    style =
                        MaterialTheme.typography.titleMedium.copy(
                            color = MaterialTheme.colorScheme.primary,
                        ),
                    modifier =
                        Modifier
                            .weight(1f),
                )

                Icon(
                    painter = painterResource(R.drawable.arrow_drop_down),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier =
                        Modifier
                            .rotate(if (expanded) 180f else 0f),
                )
            }

            if (expanded) {
                HorizontalDivider(
                    modifier =
                        Modifier
                            .padding(vertical = 8.dp),
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    content()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> ChoiceSettings(
    label: String,
    options: List<T>,
    selectedOption: T,
    onOptionSelected: (T) -> Unit,
    optionLabel: (T) -> String,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier,
    ) {
        Text(
            text = label,
            style =
                MaterialTheme.typography.labelLarge.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                ),
        )

        CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides 36.dp) {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                options.forEach { option ->
                    FilterChip(
                        selected = option == selectedOption,
                        onClick = { onOptionSelected(option) },
                        label = { Text(optionLabel(option)) },
                        leadingIcon = {
                            if (option == selectedOption) {
                                Icon(
                                    painter = painterResource(R.drawable.check),
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                )
                            }
                        },
                    )
                }
            }
        }
    }
}

@Composable
fun ToggleSetting(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier =
            modifier
                .fillMaxWidth()
                .clickable { onCheckedChange(!checked) }
                .padding(vertical = 8.dp),
    ) {
        Text(
            text = label,
            style =
                MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                ),
            modifier =
                Modifier
                    .weight(1f),
        )

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
        )
    }
}

@Composable
fun SliderSetting(
    label: String,
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    valueSuffix: String = "",
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = label,
                style =
                    MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onSurface,
                    ),
                modifier =
                    Modifier
                        .weight(1f),
            )

            Text(
                text = "${value.toInt()}$valueSuffix",
                style =
                    MaterialTheme.typography.labelMedium.copy(
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.End,
                    ),
                modifier =
                    Modifier
                        .padding(start = 8.dp),
            )
        }

        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = valueRange,
        )
    }
}

@Preview
@Composable
private fun OtpFieldPlaygroundScreenPreview() {
    OneTimePasswordTheme {
        OtpFieldPlaygroundScreen()
    }
}
