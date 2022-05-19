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

@file:OptIn(ExperimentalComposeUiApi::class)

package androidx.compose.desktop.examples.mouseclicks

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.ui.input.pointer.isPrimary
import androidx.compose.ui.input.pointer.isSecondary
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.drag
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.isAltPressed
import androidx.compose.ui.input.pointer.isCtrlPressed
import androidx.compose.ui.input.pointer.isShiftPressed
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.singleWindowApplication

@OptIn(ExperimentalFoundationApi::class)
fun main() {
    singleWindowApplication(
        title = "Desktop Mouse Clicks",
        state = WindowState(width = 512.dp, height = 425.dp)
    ) {
        var enabled by remember { mutableStateOf(true) }

        Column {
            Box(modifier = Modifier.fillMaxWidth().border(1.dp, Color.Black)) {
                Row(modifier = Modifier.align(Alignment.Center)) {
                    Checkbox(enabled, onCheckedChange = {
                        enabled = it
                    })
                    Text("enabled all", modifier = Modifier.padding(top = 16.dp, start = 8.dp))
                }
            }


            Row {
                Column(modifier = Modifier.padding(25.dp)) {
                    Text("combinedMouseClickable")

                    Box(modifier = Modifier.size(100.dp).background(Color.LightGray)
                        .combinedClickable(
                            enabled = enabled,
                            onDoubleClick = {
                                println("Simple LClick DoubleClick")
                            },
                            onLongClick = {
                                println("Simple LClick LongPress")
                            })
                        {
                            println("Simple LClick click")
                        }
                        .combinedClickable(
                            enabled = enabled,
                            filter = {
                                button.isPrimary && keyboardModifiers.isShiftPressed
                            },
                        ) {
                            println("LClick + Shit")
                        }
                        .combinedClickable(
                            enabled = enabled,
                            filter = {
                                button.isSecondary && keyboardModifiers.isAltPressed
                            },
                        ) {
                            println("RClick + Alt")
                        }
                    ) {
                        Text("LClick + Shit | RClick + Alt | Simple LClick & DoubleClick & LongPress")
                    }
                }

                Column(modifier = Modifier.padding(25.dp)) {
                    Text("mouseDraggable")
                    var offset1 by remember { mutableStateOf(Offset.Zero) }
                    Box(modifier = Modifier.offset { IntOffset(offset1.x.toInt(), offset1.y.toInt()) }
                        .size(100.dp).background(Color.Blue)
                        .drag(
                            enabled = enabled,
                            filter = { button?.isSecondary == true },
                            onDragStart = { o, km -> println("Blue: Start, offset=$o, km=$km") },
                            onDragEnd = { println("Blue: End") }
                        ) {
                            offset1 += it.offset * if (it.currentKeyboardModifiers.isCtrlPressed) 2.0f else 1.0f
                        }) {
                        Text("Use Right Mouse")
                    }

                    var offset2 by remember { mutableStateOf(Offset.Zero) }
                    Box(
                        modifier = Modifier.offset { IntOffset(offset2.x.toInt(), offset2.y.toInt()) }
                            .size(100.dp).background(Color.Gray)
                            .drag(
                                enabled = enabled,
                                onDragStart = { o, km -> println("Gray: Start, offset=$o, km=$km") },
                                onDragEnd = { println("Gray: End") }
                            ) {
                                offset2 += it.offset * if (it.currentKeyboardModifiers.isCtrlPressed) 2.0f else 1.0f
                            }) {
                        Text("Use Left Mouse")
                    }
                }
            }
        }
    }
}
