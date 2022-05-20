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

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.input.pointer.PointerButton
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.input.pointer.PointerKeyboardModifiers
import androidx.compose.ui.input.pointer.PointerType
import androidx.compose.ui.util.fastAll

@OptIn(ExperimentalComposeUiApi::class)
class PointerFilter internal constructor() {

    companion object {
        val Default: PointerFilter.() -> Unit = {
            mouse { button = PointerButton.Primary }
            touch {
                // no button or keyboardModifiers required
            }
            stylus {
                // no button or keyboardModifiers required
            }
            eraser {
                // no button or keyboardModifiers required
            }
        }

        private val DefaultFilterByKeyboardModifiers: PointerKeyboardModifiers.() -> Boolean = { true }
    }

    inner class FilterBuilder {
        var button: PointerButton? = null
        var keyboardModifiers: PointerKeyboardModifiers.() -> Boolean = DefaultFilterByKeyboardModifiers
    }

    private var mouse: FilterVariant? = null
    private var touch: FilterVariant? = null
    private var stylus: FilterVariant? = null
    private var eraser: FilterVariant? = null

    private fun buildFilterData(pointerType: PointerType, builder: FilterBuilder): FilterVariant {
        return FilterVariant(
            pointerType = pointerType,
            button = builder.button,
            keyboardModifiers = builder.keyboardModifiers
        )
    }

    fun mouse(builder: FilterBuilder.() -> Unit) {
        mouse = buildFilterData(PointerType.Mouse, FilterBuilder().also(builder))
    }

    fun touch(builder: FilterBuilder.() -> Unit) {
        touch = buildFilterData(PointerType.Touch, FilterBuilder().also(builder))
    }

    fun stylus(builder: FilterBuilder.() -> Unit) {
        stylus = buildFilterData(PointerType.Stylus, FilterBuilder().also(builder))
    }

    fun eraser(builder: FilterBuilder.() -> Unit) {
        eraser = buildFilterData(PointerType.Eraser, FilterBuilder().also(builder))
    }

    internal fun combinedFilter(): (PointerEvent) -> Boolean {
        return { event ->
            mouse?.filter(event) == true ||
                touch?.filter(event) == true ||
                stylus?.filter(event) == true ||
                eraser?.filter(event) == true
        }
    }

    internal fun keyboardModifiersFilter(pointer: PointerType): PointerKeyboardModifiers.() -> Boolean {
        return when(pointer) {
            PointerType.Mouse -> mouse?.keyboardModifiers
            PointerType.Touch -> touch?.keyboardModifiers
            PointerType.Stylus -> stylus?.keyboardModifiers
            PointerType.Eraser -> eraser?.keyboardModifiers
            else -> null
        } ?: DefaultFilterByKeyboardModifiers
    }
}


@OptIn(ExperimentalComposeUiApi::class)
private data class FilterVariant(
    val pointerType: PointerType,
    val button: PointerButton?,
    val keyboardModifiers: PointerKeyboardModifiers.() -> Boolean,
) {
    fun filter(e: PointerEvent): Boolean {
        return e.changes.fastAll { it.type == pointerType } &&
            keyboardModifiers(e.keyboardModifiers) &&
            if (button != null) e.button == button else true

    }
}
