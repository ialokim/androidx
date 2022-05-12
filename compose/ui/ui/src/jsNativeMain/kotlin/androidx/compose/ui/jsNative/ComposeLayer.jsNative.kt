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

package androidx.compose.ui.native

import androidx.compose.runtime.Composable
import androidx.compose.ui.ComposeScene
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.key.KeyEvent as ComposeKeyEvent
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.input.pointer.toCompose
import androidx.compose.ui.input.pointer.PointerType
import androidx.compose.ui.platform.PlatformComponent
import androidx.compose.ui.unit.Density
import androidx.compose.ui.currentMillis
import androidx.compose.ui.input.pointer.PointerEventType
import kotlinx.coroutines.CoroutineDispatcher
import org.jetbrains.skia.Canvas
import org.jetbrains.skiko.SkiaLayer
import org.jetbrains.skiko.SkikoView
import org.jetbrains.skiko.SkikoInputEvent
import org.jetbrains.skiko.SkikoKeyboardEvent
import org.jetbrains.skiko.SkikoPointerEvent
import org.jetbrains.skiko.SkikoPointerEventKind
import org.jetbrains.skiko.SkikoTouchEvent
import org.jetbrains.skiko.SkikoTouchEventKind
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.platform.WindowInfoImpl
import androidx.compose.ui.createSkiaLayer
import androidx.compose.ui.input.key.NativeKeyEvent
import androidx.compose.ui.window.WindowExceptionHandler

internal expect fun getMainDispatcher(): CoroutineDispatcher
