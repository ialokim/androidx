/*
 * Copyright 2019 The Android Open Source Project
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

@file:OptIn(InternalComposeApi::class)
package androidx.compose.runtime

import androidx.compose.runtime.mock.EmptyApplier
import androidx.compose.runtime.snapshots.Snapshot
import androidx.compose.runtime.snapshots.SnapshotStateObserver
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.runBlocking
import kotlin.concurrent.thread
import kotlin.test.Test

@Stable
@OptIn(InternalComposeApi::class)
@Suppress("unused")
class JvmCompositionTests {
    // Regression test for b/202967533
    // Test taken from the bug report; reformatted to conform to lint rules.
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun avoidsDeadlockInRecomposerComposerDispose() {
        val thread = thread {
            while (!Thread.interrupted()) {
                // -> synchronized(stateLock) -> recordComposerModificationsLocked
                // -> composition.recordModificationsOf -> synchronized(lock)
                Snapshot.sendApplyNotifications()
            }
        }

        for (i in 1..5000) {
            runBlocking(TestCoroutineDispatcher()) {
                localRecomposerTest {
                    @Suppress("ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE")
                    var value by mutableStateOf(0)
                    val snapshotObserver = SnapshotStateObserver {}
                    snapshotObserver.start()
                    @Suppress("UNUSED_VALUE")
                    value = 4
                    val composition = Composition(EmptyApplier(), it)
                    composition.setContent {}

                    // -> synchronized(lock) -> parent.unregisterComposition(this)
                    // -> synchronized(stateLock)
                    composition.dispose()
                    snapshotObserver.stop()
                }
            }
        }

        thread.interrupt()
    }
}