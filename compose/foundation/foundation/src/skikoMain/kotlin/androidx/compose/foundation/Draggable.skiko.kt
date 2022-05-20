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

package androidx.compose.foundation

import androidx.compose.foundation.gestures.awaitPointerSlopOrCancellation
import androidx.compose.foundation.gestures.drag
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.AwaitPointerEventScope
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.PointerKeyboardModifiers
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.platform.LocalWindowInfo
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.isActive

@ExperimentalFoundationApi
data class DragChange(
    val offset: Offset = Offset.Zero,
    val previousKeyboardModifiers: PointerKeyboardModifiers,
    val currentKeyboardModifiers: PointerKeyboardModifiers
) {
    fun keyboardModifierChanged(): Boolean =
        previousKeyboardModifiers != currentKeyboardModifiers
}

@ExperimentalFoundationApi
/**
 * @param onDrag - receives an instance of [DragChange]  for every pointer position change when dragging or
 * when [PointerKeyboardModifiers] state changes after drag started and before it ends.
 */
fun Modifier.onDrag(
    enabled: Boolean = true,
    filter: PointerFilterBuilder.() -> Unit = PointerFilterBuilder.Default,
    onDragStart: (Offset, PointerKeyboardModifiers) -> Unit = { _, _ -> },
    onDragCancel: () -> Unit = {},
    onDragEnd: () -> Unit = {},
    onDrag: (DragChange) -> Unit
): Modifier = composed {
    if (enabled) {
        var dragInProgress by remember { mutableStateOf(false) }
        var previousKeyboardModifiers by remember { mutableStateOf(PointerKeyboardModifiers()) }

        val onDragState = rememberUpdatedState(onDrag)
        val onDragStartState = rememberUpdatedState(onDragStart)
        val onDragEndState = rememberUpdatedState(onDragEnd)
        val onDragCancelState = rememberUpdatedState(onDragCancel)

        val filterState = remember {
            mutableStateOf<(PointerEvent) -> Boolean>({ false })
        }.apply {
            value = remember(filter) { PointerFilterBuilder().also(filter).build() }
        }

        if (dragInProgress) {
            KeyboardModifiersObserver {
                if (previousKeyboardModifiers != it) {
                    onDragState.value(DragChange(Offset.Zero, previousKeyboardModifiers, it))
                    previousKeyboardModifiers = it
                }
            }
        }

        Modifier.pointerInput(Unit) {
            while (currentCoroutineContext().isActive) {
                dragInProgress = false
                awaitPointerEventScope {
                    val press = awaitPress(
                        requireUnconsumed = false,
                        filterPressEvent = filterState.value
                    )

                    val overSlop = awaitDragStartOnSlop(press)
                    val pointerId = press.changes[0].id

                    if (overSlop != null) {
                        dragInProgress = true
                        onDragStartState.value(
                            press.changes[0].position,
                            currentEvent.keyboardModifiers
                        )
                        if (overSlop != Offset.Zero ||
                            currentEvent.keyboardModifiers != previousKeyboardModifiers
                        ) {
                            onDragState.value(
                                DragChange(overSlop, previousKeyboardModifiers, currentEvent.keyboardModifiers)
                            )
                            previousKeyboardModifiers = currentEvent.keyboardModifiers
                        }

                        val dragCompleted = drag(pointerId) {
                            val change = DragChange(
                                it.positionChange(),
                                previousKeyboardModifiers,
                                currentEvent.keyboardModifiers
                            )
                            previousKeyboardModifiers = currentEvent.keyboardModifiers
                            onDragState.value(change)
                            it.consume()
                        }

                        if (!dragCompleted) {
                            onDragCancelState.value()
                        } else {
                            onDragEndState.value()
                        }
                        dragInProgress = false
                    }
                }
            }
        }
    } else {
        Modifier
    }
}

@Composable
private fun KeyboardModifiersObserver(onKeyboardModifiersChanged: (PointerKeyboardModifiers) -> Unit) {
    val windowInfo = LocalWindowInfo.current
    val callback = rememberUpdatedState(onKeyboardModifiersChanged)

    LaunchedEffect(windowInfo) {
        snapshotFlow { windowInfo.keyboardModifiers }.collect {
            if (it != null) callback.value(it)
        }
    }
}

private suspend fun AwaitPointerEventScope.awaitDragStartOnSlop(initialDown: PointerEvent): Offset? {
    var overSlop = Offset.Zero
    var drag: PointerInputChange?
    do {
        drag = awaitPointerSlopOrCancellation(
            initialDown.changes[0].id,
            initialDown.changes[0].type
        ) { change, over ->
            change.consume()
            overSlop = over
        }
    } while (drag != null && !drag.isConsumed)

    return if (drag == null) {
        null
    } else {
        overSlop
    }
}
