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

package androidx.compose.native

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Composition
import androidx.compose.runtime.CompositionContext
/*
import androidx.compose.ui.input.mouse.MouseScrollEvent
import androidx.compose.ui.input.mouse.MouseScrollOrientation
import androidx.compose.ui.input.mouse.MouseScrollUnit
 */
import androidx.compose.ui.platform.NativeComponent
import androidx.compose.ui.platform.NativeOwner
import androidx.compose.ui.platform.NativeOwners
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.unit.Density
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import org.jetbrains.skia.Canvas
import org.jetbrains.skiko.native.SkiaLayer
import org.jetbrains.skiko.native.SkiaRenderer

/* internal */ class ComposeLayer {
    init {
        println("created ComposeLayer")
    }
    private var isDisposed = false

    private val coroutineScope = CoroutineScope(Dispatchers.Main /*Dispatchers.Swing*/)
    // TODO(demin): maybe pass CoroutineScope into AWTDebounceEventQueue and get rid of [cancel]
    //  method?
    // private val events = AWTDebounceEventQueue()

    internal val wrapped = Wrapped()

    internal val owners: NativeOwners = NativeOwners(
        coroutineScope,
        wrapped,
        wrapped::needRedraw
    )

    private var owner: NativeOwner? = null
    private var composition: Composition? = null

    private var content: (@Composable () -> Unit)? = null
    private var parentComposition: CompositionContext? = null

    private /*lateinit*/ var density: Density = Density(1f, 1f)

    inner class Wrapped : SkiaLayer(), NativeComponent {
        init {
            println("created Wrapped")
        }
        // var currentInputMethodRequests: InputMethodRequests? = null

        var isInit = false
            private set

        override fun init() {
            println("Wrapped.init()")
            super.init()
            isInit = true
            resetDensity()
            initOwner()
        }

        override fun contentScaleChanged() {
            super.contentScaleChanged()
            resetDensity()
        }

        private fun resetDensity() {
            this@ComposeLayer.density = detectCurrentDensity()
            owner?.density = density
        }

        // override fun getInputMethodRequests() = currentInputMethodRequests


        override fun enableInput(/*inputMethodRequests: InputMethodRequests*/) {
            TODO("implement enableInput()")
            /*
            currentInputMethodRequests = inputMethodRequests
            enableInputMethods(true)
            val focusGainedEvent = FocusEvent(this, FocusEvent.FOCUS_GAINED)
            inputContext.dispatchEvent(focusGainedEvent)

             */
        }

        override fun disableInput() {
            TODO("implement disableInput()")
            // currentInputMethodRequests = null
        }

        // override val locationOnScreen: Point
        //     get() = super.getLocationOnScreen()

        override val density: Density
            get() = this@ComposeLayer.density
    }

    val component: SkiaLayer
        get() = wrapped

    init {
        component.renderer = object : SkiaRenderer {
            init {
                println("Created SkiaRenderer object")
            }
            override fun onRender(canvas: Canvas, width: Int, height: Int, nanoTime: Long) {
                println("ComposeLayer: renderer.onRender()")
                try {
                    owners.onFrame(canvas, width, height, nanoTime)
                } catch (e: Throwable) {
                    println("Exception in Wrapped.onRender()")
                    println(e.message)
                    e.printStackTrace()

                    throw e
                    // TODO: K/N doesn't have Properties.
                    // if (System.getProperty("compose.desktop.render.ignore.errors") == null) {
                    //     throw e
                    // }
                }
            }
        }
        initCanvas()
    }

    private fun initCanvas() {
        /*
        wrapped.addInputMethodListener(object : InputMethodListener {
            override fun caretPositionChanged(event: InputMethodEvent?) {
                if (event != null) {
                    owners.onInputMethodEvent(event)
                }
            }

            override fun inputMethodTextChanged(event: InputMethodEvent) = events.post {
                owners.onInputMethodEvent(event)
            }
        })

        wrapped.addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(event: MouseEvent) = Unit

            override fun mousePressed(event: MouseEvent) = events.post {
                owners.onMousePressed(
                    (event.x * density.density).toInt(),
                    (event.y * density.density).toInt(),
                    event
                )
            }

            override fun mouseReleased(event: MouseEvent) = events.post {
                owners.onMouseReleased(
                    (event.x * density.density).toInt(),
                    (event.y * density.density).toInt(),
                    event
                )
            }

            override fun mouseEntered(event: MouseEvent) = events.post {
                owners.onMouseEntered(
                    (event.x * density.density).toInt(),
                    (event.y * density.density).toInt()
                )
            }

            override fun mouseExited(event: MouseEvent) = events.post {
                owners.onMouseExited()
            }
        })
        wrapped.addMouseMotionListener(object : MouseMotionAdapter() {
            override fun mouseDragged(event: MouseEvent) = events.post {
                owners.onMouseDragged(
                    (event.x * density.density).toInt(),
                    (event.y * density.density).toInt(),
                    event
                )
            }

            override fun mouseMoved(event: MouseEvent) = events.post {
                owners.onMouseMoved(
                    (event.x * density.density).toInt(),
                    (event.y * density.density).toInt()
                )
            }
        })
        wrapped.addMouseWheelListener { event ->
            events.post {
                owners.onMouseScroll(
                    (event.x * density.density).toInt(),
                    (event.y * density.density).toInt(),
                    event.toComposeEvent()
                )
            }
        }
        wrapped.focusTraversalKeysEnabled = false
        wrapped.addKeyListener(object : KeyAdapter() {
            override fun keyPressed(event: KeyEvent) = events.post {
                owners.onKeyPressed(event)
            }

            override fun keyReleased(event: KeyEvent) = events.post {
                owners.onKeyReleased(event)
            }

            override fun keyTyped(event: KeyEvent) = events.post {
                owners.onKeyTyped(event)
            }
        })
        */
    }
/*
    private fun MouseWheelEvent.toComposeEvent() = MouseScrollEvent(
        delta = if (scrollType == MouseWheelEvent.WHEEL_BLOCK_SCROLL) {
            MouseScrollUnit.Page((scrollAmount * preciseWheelRotation).toFloat())
        } else {
            MouseScrollUnit.Line((scrollAmount * preciseWheelRotation).toFloat())
        },

        // There are no other way to detect horizontal scrolling in AWT
        orientation = if (isShiftDown) {
            MouseScrollOrientation.Horizontal
        } else {
            MouseScrollOrientation.Vertical
        }
    )
*/
    // TODO(demin): detect OS fontScale
    //  font size can be changed on Windows 10 in Settings - Ease of Access,
    //  on Ubuntu in Settings - Universal Access
    //  on macOS there is no such setting
    private fun detectCurrentDensity(): Density {
        return Density(wrapped.contentScale, 1f)
    }

    fun dispose() {
        check(!isDisposed)
        composition?.dispose()
        owner?.dispose()

        // events.cancel()
        coroutineScope.cancel()
        wrapped.dispose()


        isDisposed = true
    }

    internal fun setContent(
        parentComposition: CompositionContext? = null,
        content: @Composable () -> Unit
    ) {
        check(!isDisposed)
        check(this.content == null) { "Cannot set content twice" }
        this.content = content
        this.parentComposition = parentComposition
        // We can't create NativeOwner now, because we don't know density yet.
        // We will know density only after SkiaLayer will be visible.
        initOwner()
    }

    private fun initOwner() {
        check(!isDisposed)
        if (wrapped.isInit && owner == null && content != null) {
            owner = NativeOwner(owners, density)
            composition = owner!!.setContent(parent = parentComposition, content = content!!)
        }
    }
}