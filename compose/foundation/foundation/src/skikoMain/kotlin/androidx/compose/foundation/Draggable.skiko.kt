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
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.AwaitPointerEventScope
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.PointerKeyboardModifiers
import androidx.compose.ui.input.pointer.PointerType
import androidx.compose.ui.input.pointer.changedToUpIgnoreConsumed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.util.fastAll
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

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
    filter: PointerFilter.() -> Unit = PointerFilter.Default,
    onDragStart: (Offset, PointerKeyboardModifiers) -> Unit = { _, _ -> },
    onDragCancel: () -> Unit = {},
    onDragEnd: () -> Unit = {},
    onDrag: (DragChange) -> Unit
): Modifier = composed {
    if (!enabled) return@composed Modifier

    var dragInProgress by remember { mutableStateOf(false) }
    var dragPointer by remember { mutableStateOf<PointerType?>(null) }
    var previousKeyboardModifiers by remember { mutableStateOf(PointerKeyboardModifiers()) }

    val onDragState = rememberUpdatedState(onDrag)
    val onDragStartState = rememberUpdatedState(onDragStart)
    val onDragEndState = rememberUpdatedState(onDragEnd)
    val onDragCancelState = rememberUpdatedState(onDragCancel)

    val pointerFilterState = remember {
        mutableStateOf(PointerFilter())
    }.apply {
        value = remember(filter) { PointerFilter().also(filter) }
    }

    val combinedFilterState = remember {
        mutableStateOf<(PointerEvent) -> Boolean>({ false })
    }.apply {
        value = remember(filter) { pointerFilterState.value.combinedFilter() }
    }

    if (dragInProgress) {
        KeyboardModifiersObserver {
            if (previousKeyboardModifiers != it) {
                val passesFilter = pointerFilterState.value.keyboardModifiersFilter(dragPointer!!).invoke(it)
                if (passesFilter) {
                    onDragState.value(DragChange(Offset.Zero, previousKeyboardModifiers, it))
                    previousKeyboardModifiers = it
                } else {
                    dragInProgress = false
                    onDragCancel()
                }
            }
        }
    }

    Modifier.pointerInput(Unit) {
        while (currentCoroutineContext().isActive) {
            dragInProgress = false
            dragPointer = null
            val pressEvent = Channel<Unit>(Channel.CONFLATED)

            coroutineScope {
                val dragJob = launch {
                    awaitPointerEventScope {
                        val press = awaitPress(
                            requireUnconsumed = false,
                            filterPressEvent = combinedFilterState.value
                        )
                        pressEvent.trySend(Unit)

                        val overSlop = awaitDragStartOnSlop(press)
                        val pointerId = press.changes[0].id

                        if (overSlop != null) {
                            dragInProgress = true
                            dragPointer = press.changes[0].type
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
                                // with current implementation, the drag never ends here
                                onDragEndState.value()
                            }
                            dragInProgress = false
                        }
                    }
                }

                launch {
                    pressEvent.receive()
                    awaitPointerEventScope {
                        while (dragJob.isActive) {
                            val event = awaitPointerEvent()
                            if (event.isReleased() && combinedFilterState.value(event)) {
                                // it always ends the drag here
                                dragJob.cancel()
                                onDragEndState.value()
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun PointerEvent.isReleased() = type == PointerEventType.Release &&
    changes.fastAll { it.type == PointerType.Mouse } || changes.fastAll { it.changedToUpIgnoreConsumed() }

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
