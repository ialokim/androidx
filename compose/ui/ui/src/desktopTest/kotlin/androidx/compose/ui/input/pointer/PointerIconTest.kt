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

package androidx.compose.ui.input.pointer

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalPointerIconService
import androidx.compose.ui.platform.TestComposeWindow
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@OptIn(ExperimentalComposeUiApi::class)
@RunWith(JUnit4::class)
class PointerIconTest {
    private val window = TestComposeWindow(width = 100, height = 100, density = Density(1f))

    private val iconService = object : PointerIconService {
        var currentIcon: PointerIcon? = null
        override fun set(cursor: PointerIcon) {
            currentIcon = cursor
        }

        override fun getCurrent(): PointerIcon = currentIcon ?: PointerIcon.Default
    }

    @Test
    fun basicTest() {
        window.setContent {
            CompositionLocalProvider(
                LocalPointerIconService provides iconService
            ) {
                Box(
                    modifier = Modifier
                        .size(30.dp, 30.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .pointerHoverIcon(PointerIcon.Text)
                            .size(10.dp, 10.dp)
                    )
                }
            }
        }

        window.onMouseMoved(
            x = 5,
            y = 5
        )
        assertThat(iconService.getCurrent()).isEqualTo(PointerIcon.Text)
    }

    @Test
    fun parentWins() {
        window.setContent {
            CompositionLocalProvider(
                LocalPointerIconService provides iconService
            ) {
                Box(
                    modifier = Modifier
                        .pointerHoverIcon(PointerIcon.Hand)
                        .size(30.dp, 30.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .pointerHoverIcon(PointerIcon.Text)
                            .size(10.dp, 10.dp)
                    )
                }
            }
        }

        window.onMouseMoved(
            x = 5,
            y = 5
        )
        assertThat(iconService.getCurrent()).isEqualTo(PointerIcon.Hand)

        window.onMouseMoved(
            x = 15,
            y = 15
        )
        assertThat(iconService.getCurrent()).isEqualTo(PointerIcon.Hand)
    }

    @Test
    fun childWins() {
        window.setContent {
            CompositionLocalProvider(
                LocalPointerIconService provides iconService
            ) {
                Box(
                    modifier = Modifier
                        .pointerHoverIcon(PointerIcon.Hand, false)
                        .size(30.dp, 30.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .pointerHoverIcon(PointerIcon.Text)
                            .size(10.dp, 10.dp)
                    )
                }
            }
        }

        window.onMouseMoved(
            x = 5,
            y = 5
        )
        assertThat(iconService.getCurrent()).isEqualTo(PointerIcon.Text)

        window.onMouseMoved(
            x = 15,
            y = 15
        )
        assertThat(iconService.getCurrent()).isEqualTo(PointerIcon.Hand)
    }
}