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

@file:OptIn(ExperimentalComposeUiApi::class)

package androidx.compose.ui.platform

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.ExperimentalComposeUiApi

@ExperimentalComposeUiApi
interface PlatformLocalization {
    val copy: String
    val cut: String
    val paste: String
    val selectAll: String
}

internal val defaultPlatformLocalization = object : PlatformLocalization {
    override val copy = "Copy"
    override val cut = "Cut"
    override val paste = "Paste"
    override val selectAll = "Select All"
}

@ExperimentalComposeUiApi
val LocalLocalization = staticCompositionLocalOf {
    defaultPlatformLocalization
}