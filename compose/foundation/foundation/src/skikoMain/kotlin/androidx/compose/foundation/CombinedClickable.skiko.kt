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
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.isActive

@ExperimentalFoundationApi
fun Modifier.onCombinedClick(
    enabled: Boolean = true,
    filter: PointerFilter.() -> Unit = PointerFilter.Default,
    onDoubleClick: (() -> Unit)? = null,
    onLongClick: (() -> Unit)? = null,
    onClick: () -> Unit
) = composed {
    Modifier.onCombinedClick(
        enabled = enabled,
        filter = filter,
        interactionSource = remember { MutableInteractionSource() },
        onDoubleClick = onDoubleClick,
        onLongClick = onLongClick,
        onClick = onClick
    )
}

@ExperimentalFoundationApi
fun Modifier.onCombinedClick(
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource,
    filter: PointerFilter.() -> Unit,
    onDoubleClick: (() -> Unit)? = null,
    onLongClick: (() -> Unit)? = null,
    onClick: () -> Unit
) = composed(
    inspectorInfo = {
        name = "onCombinedClick"
        properties["enabled"] = enabled
        properties["filter"] = filter
        properties["onDoubleClick"] = onDoubleClick
        properties["onLongClick"] = onLongClick
        properties["onClick"] = onClick
        properties["interactionSource"] = interactionSource
    },
    factory = {
        val gestureModifier = if (enabled) {
            val pressedInteraction = remember { mutableStateOf<PressInteraction.Press?>(null) }
            val onClickState = rememberUpdatedState(onClick)
            val on2xClickState = rememberUpdatedState(onDoubleClick)
            val onLongClickState = rememberUpdatedState(onLongClick)
            val filterState = remember {
                mutableStateOf<(PointerEvent) -> Boolean>({ false })
            }.apply {
                value = remember(filter) { PointerFilter().also(filter).combinedFilter() }
            }

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
    }
)
