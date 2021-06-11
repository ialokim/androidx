/*
 * Copyright 2021 The Android Open Source Project
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

package androidx.compose.ui.platform

import androidx.compose.runtime.BroadcastFrameClock
import androidx.compose.runtime.Recomposer
import androidx.compose.runtime.snapshots.Snapshot
import androidx.compose.runtime.staticCompositionLocalOf
/*
import androidx.compose.ui.geometry.Offset
// import androidx.compose.ui.input.mouse.MouseScrollEvent
import androidx.compose.ui.input.pointer.PointerId
import androidx.compose.ui.input.pointer.PointerInputEvent
import androidx.compose.ui.input.pointer.PointerInputEventData
import androidx.compose.ui.input.pointer.PointerType
*/
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.launch
import org.jetbrains.skiko.skia.native.Canvas
/*
import java.awt.event.InputMethodEvent
import java.awt.event.KeyEvent
import java.awt.event.MouseEvent
*/
import androidx.compose.ui.input.key.KeyEvent as ComposeKeyEvent

internal val LocalNativeOwners = staticCompositionLocalOf<NativeOwners> {
    error("CompositionLocal NativeOwnersAmbient not provided")
}

// TODO: this is essentially a copy of DesktopOwners.
/* internal */ class NativeOwners(
    private val coroutineScope: CoroutineScope,
    component: NativeComponent = DummyNativeComponent,
    private val invalidate: () -> Unit = {},
) {
    init {
        println("created NativeOwners")
    }
    private var isInvalidationDisabled = false

    // @Volatile
    private var hasPendingDraws = true
    private inline fun disableInvalidation(block: () -> Unit) {
        isInvalidationDisabled = true
        try {
            block()
        } finally {
            isInvalidationDisabled = false
        }
    }

    private fun invalidateIfNeeded() {
        hasPendingDraws = frameClock.hasAwaiters || list.any(NativeOwner::needsRender)
        if (hasPendingDraws && !isInvalidationDisabled) {
            invalidate()
        }
    }

    val list = LinkedHashSet<NativeOwner>()
    private val listCopy = mutableListOf<NativeOwner>()

    /*
    var keyboard: Keyboard? = null

    private var pointerId = 0L
    private var isMousePressed = false
*/
    private val dispatcher = FlushCoroutineDispatcher(coroutineScope)

    private val frameClock = BroadcastFrameClock(onNewAwaiters = ::invalidateIfNeeded)

    private val coroutineContext = dispatcher + frameClock

    internal val recomposer = Recomposer(coroutineContext)
    internal val platformInputService: NativePlatformInput = NativePlatformInput(component)

    init {
        coroutineScope.launch(coroutineContext, start = CoroutineStart.UNDISPATCHED) {
            recomposer.runRecomposeAndApplyChanges()
        }
    }

    private fun dispatchCommand(command: () -> Unit) {
        coroutineScope.launch(coroutineContext) {
            command()
        }
    }

    /**
     * Returns true if there are pending recompositions, draws or dispatched tasks.
     * Can be called from any thread.
     */
    fun hasInvalidations() = hasPendingDraws ||
        recomposer.hasPendingWork ||
        dispatcher.hasTasks()

    fun register(NativeOwner: NativeOwner) {
        println("NativeOwners.register() LIST: add")
        list.add(NativeOwner)
        NativeOwner.onNeedsRender = ::invalidateIfNeeded
        NativeOwner.onDispatchCommand = ::dispatchCommand
        invalidateIfNeeded()
    }

    fun unregister(NativeOwner: NativeOwner) {
        println("NativeOwners.unregister() LIST: remove")
        list.remove(NativeOwner)
        NativeOwner.onDispatchCommand = null
        NativeOwner.onNeedsRender = null
        invalidateIfNeeded()
    }

    fun onFrame(canvas: Canvas, width: Int, height: Int, nanoTime: Long) {
        println("NativeOwners.onFrame() ${list}")
        disableInvalidation {
            // We must see the actual state before we will render the frame
            Snapshot.sendApplyNotifications()
            dispatcher.flush()
            frameClock.sendFrame(nanoTime)

            listCopy.clear()
            listCopy.addAll(list)
            for (owner in listCopy) {
                println("LIST member render")
                owner.render(canvas, width, height)
            }
        }

        invalidateIfNeeded()
    }

    private val lastOwner: NativeOwner?
        get() = list.lastOrNull()
/*
    fun onMousePressed(x: Int, y: Int, nativeEvent: MouseEvent? = null) {
        isMousePressed = true
        lastOwner?.processPointerInput(pointerInputEvent(nativeEvent, x, y, isMousePressed))
    }

    fun onMouseReleased(x: Int, y: Int, nativeEvent: MouseEvent? = null) {
        isMousePressed = false
        lastOwner?.processPointerInput(pointerInputEvent(nativeEvent, x, y, isMousePressed))
        pointerId += 1
    }

    fun onMouseDragged(x: Int, y: Int, nativeEvent: MouseEvent? = null) {
        lastOwner?.processPointerInput(pointerInputEvent(nativeEvent, x, y, isMousePressed))
    }

    fun onMouseScroll(x: Int, y: Int, event: MouseScrollEvent) {
        val position = Offset(x.toFloat(), y.toFloat())
        lastOwner?.onMouseScroll(position, event)
    }

    fun onMouseMoved(x: Int, y: Int) {
        val position = Offset(x.toFloat(), y.toFloat())
        lastOwner?.onPointerMove(position)
    }

    fun onMouseEntered(x: Int, y: Int) {
        val position = Offset(x.toFloat(), y.toFloat())
        lastOwner?.onPointerEnter(position)
    }

    fun onMouseExited() {
        lastOwner?.onPointerExit()
    }

    private fun consumeKeyEvent(event: KeyEvent) {
        list.lastOrNull()?.sendKeyEvent(ComposeKeyEvent(event))
    }

    fun onKeyPressed(event: KeyEvent) = consumeKeyEvent(event)

    fun onKeyReleased(event: KeyEvent) = consumeKeyEvent(event)

    fun onKeyTyped(event: KeyEvent) = consumeKeyEvent(event)

    fun onInputMethodEvent(event: InputMethodEvent) {
        if (!event.isConsumed()) {
            when (event.id) {
                InputMethodEvent.INPUT_METHOD_TEXT_CHANGED -> {
                    platformInputService.replaceInputMethodText(event)
                    event.consume()
                }
                InputMethodEvent.CARET_POSITION_CHANGED -> {
                    platformInputService.inputMethodCaretPositionChanged(event)
                    event.consume()
                }
            }
        }
    }

    private fun pointerInputEvent(
        nativeEvent: MouseEvent?,
        x: Int,
        y: Int,
        down: Boolean
    ): PointerInputEvent {
        val time = System.nanoTime() / 1_000_000L
        val position = Offset(x.toFloat(), y.toFloat())
        return PointerInputEvent(
            time,
            listOf(
                PointerInputEventData(
                    PointerId(pointerId),
                    time,
                    position,
                    position,
                    down,
                    PointerType.Mouse
                )
            ),
            nativeEvent
        )
    }

*/
}