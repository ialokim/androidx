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

package androidx.compose.mpp.demo

import org.jetbrains.skia.Canvas
import org.jetbrains.skia.FontMgr
import org.jetbrains.skia.paragraph.FontCollection
import org.jetbrains.skia.paragraph.ParagraphBuilder
import org.jetbrains.skia.paragraph.ParagraphStyle
import org.jetbrains.skia.paragraph.TextStyle
import org.jetbrains.skiko.CursorManager
import org.jetbrains.skiko.GenericSkikoView
import org.jetbrains.skiko.SkiaLayer
import org.jetbrains.skiko.SkikoGestureEvent
import org.jetbrains.skiko.SkikoGestureEventKind
import org.jetbrains.skiko.SkikoInputEvent
import org.jetbrains.skiko.SkikoKey
import org.jetbrains.skiko.SkikoKeyboardEvent
import org.jetbrains.skiko.SkikoKeyboardEventKind
import org.jetbrains.skiko.SkikoPointerEvent
import org.jetbrains.skiko.SkikoTouchEvent
import org.jetbrains.skiko.SkikoUIView
import org.jetbrains.skiko.SkikoView
import org.jetbrains.skiko.SkikoViewController
import platform.UIKit.UIViewController

fun getSkikoViewContoller2(): UIViewController = SkikoViewController(
    SkikoUIView(
        SkiaLayer().apply {
            gesturesToListen = SkikoGestureEventKind.values()
            skikoView = GenericSkikoView(this, makeApp(this))
        }
    )
)

fun makeApp(skiaLayer: SkiaLayer) = Clocks(skiaLayer)

class Clocks(private val layer: SkiaLayer) : SkikoView {
    private val cursorManager = CursorManager()
    private var xOffset = 0.0
    private var yOffset = 0.0
    private var scale = 1.0
    private val fontCollection = FontCollection()
        .setDefaultFontManager(FontMgr.default)
    private val style = ParagraphStyle()
    private var inputText = ""

    override fun onRender(canvas: Canvas, width: Int, height: Int, nanoTime: Long) {
        canvas.translate(xOffset.toFloat(), yOffset.toFloat())
        canvas.scale(scale.toFloat(), scale.toFloat())
        val input = ParagraphBuilder(style, fontCollection)
            .pushStyle(TextStyle().setColor(0xFF000000.toInt()))
            .addText("TextInput: $inputText")
            .popStyle()
            .build()
        input.layout(Float.POSITIVE_INFINITY)
        input.paint(canvas, 5f, 100f)
        canvas.resetMatrix()
    }

    private fun inputTextDropLast() {
        if (inputText.isNotEmpty()) {
            inputText = inputText.dropLast(1)
        }
    }

    override fun onPointerEvent(event: SkikoPointerEvent) {

    }

    override fun onInputEvent(event: SkikoInputEvent) {
        println("event.input: ${event.input}")
        if (event.input != "\b") {
            inputText += event.input
        }
    }

    override fun onKeyboardEvent(event: SkikoKeyboardEvent) {
        if (event.kind == SkikoKeyboardEventKind.DOWN) {
            when (event.key) {
                SkikoKey.KEY_BACKSPACE -> {
                    inputTextDropLast()
                }
                else -> {}
            }
        }
    }

    override fun onTouchEvent(events: Array<SkikoTouchEvent>) {
        val event = events.first()
    }

    override fun onGestureEvent(event: SkikoGestureEvent) {

    }
}