/*
 * Copyright 2020 The Android Open Source Project
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
import androidx.compose.runtime.CompositionContext
import org.jetbrains.skiko.skia.native.*
// import org.jetbrains.skiko.ClipComponent
// import org.jetbrains.skiko.GraphicsApi

import platform.AppKit.*
import platform.Cocoa.*
import platform.Foundation.*

/*
import java.awt.Component
import javax.swing.JFrame
import javax.swing.JLayeredPane
*/
/**
 * ComposeWindow is a window for building UI using Compose for Desktop.
 * ComposeWindow inherits javax.swing.JFrame.
 * @param parent The parent AppFrame that wraps the ComposeWindow.
 */
class ComposeWindow(val parent: AppFrame) /*: SkiaWindow()*/ {
    /*
    private var isDisposed = false
    */
    val layer = ComposeLayer()

    val title: String
        get() = "TODO: get a title from SkiaWindow"

    fun setTitle(title: String) {
        println("TODO: set title to SkiaWindow")
    }

    val windowStyle =
        NSWindowStyleMaskTitled or
        NSWindowStyleMaskMiniaturizable or
        NSWindowStyleMaskClosable or
        NSWindowStyleMaskResizable

    private val nsWindow = NSWindow(
        contentRect =  NSMakeRect(0.0, 0.0, 640.0, 480.0),
        styleMask = windowStyle,
        backing =  NSBackingStoreBuffered,
        defer =  true)


    // private val clipMap = mutableMapOf<Component, ClipComponent>()

    init {
        nsWindow.contentView?.addSubview(layer.wrapped.nsView)
        layer.wrapped.checkIsShowing() // TODO: The awt versions has hierarchy listener. What should we use here?
        nsWindow.orderFrontRegardless()
    }
/*
    override fun add(nsView: NSView): Component {
        println("ComposeWindow.add")
        // val clipComponent = ClipComponent(component)
        // clipMap.put(component, clipComponent)
        // layer.wrapped.clipComponents.add(clipComponent)
        // return pane.add(component, Integer.valueOf(0))
    }

    override fun remove(/*component: Component*/nsView: NSView) {
        println("ComposeWindow.remove")
        // layer.wrapped.clipComponents.remove(clipMap.get(component)!!)
        // clipMap.remove(component)
        // pane.remove(component)
    }
*/
    /**
     * Sets Compose content of the ComposeWindow.
     *
     * @param parentComposition The parent composition reference to coordinate
     *        scheduling of composition updates.
     *        If null then default root composition will be used.
     * @param content Composable content of the ComposeWindow.
     */
    fun setContent(
        parentComposition: CompositionContext? = null,
        content: @Composable () -> Unit
    ) {
        println("ComposeWindow.setContent")
        layer.setContent(
            parentComposition = parentComposition,
            content = content
        )
    }
/*
    override fun dispose() {
        if (!isDisposed) {
            layer.dispose()
            isDisposed = true
        }
        super.dispose()
    }

    override fun setVisible(value: Boolean) {
        if (value != isVisible) {
            super.setVisible(value)
            layer.component.requestFocus()
        }
    }
*/
/*
    /**
     * Retrieve underlying platform-specific operating system handle for the window where ComposeWindow is rendered.
     * Currently returns HWND on Windows, Drawable on X11 and 0 on macOS.
     */
    val windowHandle: Long
        get() = layer.component.windowHandle

    /**
     * Returns low level rendering API used for rendering in this ComposeWindow. API is automatically selected based on
     * operating system, graphical hardware and `SKIKO_RENDER_API` environment variable.
     */
    val renderApi: GraphicsApi
        get() = layer.component.renderApi

 */
}