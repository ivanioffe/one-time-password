package com.ioffeivan.otp.compose.utils

import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldBuffer
import androidx.compose.foundation.text.input.then

/**
 * Chains the current [InputTransformation] with a full-text filtering rule.
 *
 * This transformation applies the given [predicate] to the entire content of the text field
 * *after* any potential changes have been applied by preceding transformations. If the resulting
 * text does not satisfy the filtering rule (the [predicate] returns `false`), the entire input
 * change is rejected, and [TextFieldBuffer.revertAllChanges] is called, preserving the previous state of the field.
 *
 * @param predicate The filtering rule applied to the whole text content (as a [String]). Returns
 * `true` if the text is acceptable, `false` to reject the input change.
 * @return A new [InputTransformation] combining the current transformations with the full-text filter.
 */
fun InputTransformation.filter(predicate: (String) -> Boolean): InputTransformation =
    this.then(FullTextFilter(predicate))

/**
 * An [InputTransformation] that reverts any input change if the resulting text does not satisfy
 * the provided predicate (filtering rule).
 *
 * This implementation is used internally by the [filter] extension function.
 *
 * @property predicate The filtering rule to be checked against the full text content after input.
 */
private class FullTextFilter(
    private val predicate: (String) -> Boolean,
) : InputTransformation {
    override fun TextFieldBuffer.transformInput() {
        if (!predicate.invoke(asCharSequence().toString())) {
            revertAllChanges()
        }
    }
}
