/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@file:OptIn(ExperimentalComposeUiApi::class, ExperimentalComposeUiApi::class)

package androidx.compose.foundation

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.input.pointer.isPrimary
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.onLongClick
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.isActive

@ExperimentalFoundationApi
data class CombinedClickableLabels(
    val onDoubleClickLabel: String? = null,
    val onLongPressLabel: String? = null,
    val onClickLabel: String? = null
)

// TODO add a separation function with required interactionSource: MutableInteractionSource
@ExperimentalFoundationApi
fun Modifier.combinedClickable( // onCombinedCLick: no indication, no semantics, with filter (combinedClickable should be implemented using it)
    indication: Indication? = null,
    enabled: Boolean = true,
    role: Role? = null,
    labels: CombinedClickableLabels? = null,
    filter: PointerFilterScope.() -> Boolean = { isMouse && button.isPrimary || !isMouse },
    onDoubleClick: (() -> Unit)? = null,
    onLongClick: (() -> Unit)? = null,
    onClick: () -> Unit
) = composed(
    inspectorInfo = {
        name = "combinedClickable"
        properties["enabled"] = enabled
        properties["filter"] = filter
        properties["role"] = role
        properties["labels"] = labels
        properties["onDoubleClick"] = onDoubleClick
        properties["onLongClick"] = onLongClick
        properties["onClick"] = onClick
        properties["indication"] = indication
    },
    factory = {
        val interactionSource = remember { MutableInteractionSource() }
        val pressedInteraction = remember { mutableStateOf<PressInteraction.Press?>(null) }
        val onClickState = rememberUpdatedState(onClick)
        val on2xClickState = rememberUpdatedState(onDoubleClick)
        val onLongClickState = rememberUpdatedState(onLongClick)
        val filterState = rememberUpdatedState(filter)

        val gestureModifier = if (enabled) {
            Modifier.pointerInput(Unit) {
                val clicksHandlerScope = ClicksHandlerScope(
                    pointerInputScope = this@pointerInput,
                    interactionSource = interactionSource,
                    pressedInteraction = pressedInteraction,
                    filterState = filterState,
                    onDoubleClick = on2xClickState,
                    onLongClick = onLongClickState,
                    onClick = onClickState
                )

                while (currentCoroutineContext().isActive) {
                    clicksHandlerScope.awaitEvents()
                }
            }
        } else {
            Modifier
        }

        gestureModifier
            .indication(interactionSource, indication)
            .semantics(mergeDescendants = true) {
                if (role != null) this.role = role
                this.onClick(labels?.onClickLabel) { onClick(); true }

                if (onLongClick != null) {
                    this.onLongClick(labels?.onLongPressLabel) { onLongClick(); true }
                }

                if (!enabled) disabled()
            }
    }
)
