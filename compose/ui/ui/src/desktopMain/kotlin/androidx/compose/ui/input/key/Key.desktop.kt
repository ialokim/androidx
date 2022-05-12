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

package androidx.compose.ui.input.key

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.input.key.Key.Companion.Number
import java.awt.event.KeyEvent
import java.awt.event.KeyEvent.KEY_LOCATION_STANDARD
import androidx.compose.ui.util.unpackInt1

// TODO(demin): implement most of key codes

/**
 * Actual implementation of [Key] for Desktop.
 *
 * @param keyCode an integer code representing the key pressed. Note: This keycode can be used to
 * uniquely identify a hardware key. It is different from the native keycode.
 */
@JvmInline
actual value class Key(val keyCode: Long) {
    actual companion object {
        /** Unknown key. */
        @ExperimentalComposeUiApi
        actual val Unknown = Key(0)

        /**
         * Home key.
         *
         * This key is handled by the framework and is never delivered to applications.
         */
        @ExperimentalComposeUiApi
        actual val Home = Key(0x24)

        /** Help key. */
        @ExperimentalComposeUiApi
        actual val Help = Key(0x9C)

        /**
         * Up Arrow Key / Directional Pad Up key.
         *
         * May also be synthesized from trackball motions.
         */
        @ExperimentalComposeUiApi
        actual val DirectionUp = Key(0x26)

        /**
         * Down Arrow Key / Directional Pad Down key.
         *
         * May also be synthesized from trackball motions.
         */
        @ExperimentalComposeUiApi
        actual val DirectionDown = Key(0x28)

        /**
         * Left Arrow Key / Directional Pad Left key.
         *
         * May also be synthesized from trackball motions.
         */
        @ExperimentalComposeUiApi
        actual val DirectionLeft = Key(0x25)

        /**
         * Right Arrow Key / Directional Pad Right key.
         *
         * May also be synthesized from trackball motions.
         */
        @ExperimentalComposeUiApi
        actual val DirectionRight = Key(0x27)

        /** '0' key. */
        @ExperimentalComposeUiApi
        actual val Zero = Key(0x30)

        /** '1' key. */
        @ExperimentalComposeUiApi
        actual val One = Key(0x31)

        /** '2' key. */
        @ExperimentalComposeUiApi
        actual val Two = Key(0x32)

        /** '3' key. */
        @ExperimentalComposeUiApi
        actual val Three = Key(0x33)

        /** '4' key. */
        @ExperimentalComposeUiApi
        actual val Four = Key(0x34)

        /** '5' key. */
        @ExperimentalComposeUiApi
        actual val Five = Key(0x35)

        /** '6' key. */
        @ExperimentalComposeUiApi
        actual val Six = Key(0x36)

        /** '7' key. */
        @ExperimentalComposeUiApi
        actual val Seven = Key(0x37)

        /** '8' key. */
        @ExperimentalComposeUiApi
        actual val Eight = Key(0x38)

        /** '9' key. */
        @ExperimentalComposeUiApi
        actual val Nine = Key(0x39)

        /** '+' key. */
        @ExperimentalComposeUiApi
        actual val Plus = Key(0x0209)

        /** '-' key. */
        @ExperimentalComposeUiApi
        actual val Minus = Key(0x2D)

        /** '*' key. */
        @ExperimentalComposeUiApi
        actual val Multiply = Key(0x6A)

        /** '=' key. */
        @ExperimentalComposeUiApi
        actual val Equals = Key(0x3D)

        /** '#' key. */
        @ExperimentalComposeUiApi
        actual val Pound = Key(0x0208)

        /** 'A' key. */
        @ExperimentalComposeUiApi
        actual val A = Key(0x41)

        /** 'B' key. */
        @ExperimentalComposeUiApi
        actual val B = Key(0x42)

        /** 'C' key. */
        @ExperimentalComposeUiApi
        actual val C = Key(0x43)

        /** 'D' key. */
        @ExperimentalComposeUiApi
        actual val D = Key(0x44)

        /** 'E' key. */
        @ExperimentalComposeUiApi
        actual val E = Key(0x45)

        /** 'F' key. */
        @ExperimentalComposeUiApi
        actual val F = Key(0x46)

        /** 'G' key. */
        @ExperimentalComposeUiApi
        actual val G = Key(0x47)

        /** 'H' key. */
        @ExperimentalComposeUiApi
        actual val H = Key(0x48)

        /** 'I' key. */
        @ExperimentalComposeUiApi
        actual val I = Key(0x49)

        /** 'J' key. */
        @ExperimentalComposeUiApi
        actual val J = Key(0x4A)

        /** 'K' key. */
        @ExperimentalComposeUiApi
        actual val K = Key(0x4B)

        /** 'L' key. */
        @ExperimentalComposeUiApi
        actual val L = Key(0x4C)

        /** 'M' key. */
        @ExperimentalComposeUiApi
        actual val M = Key(0x4D)

        /** 'N' key. */
        @ExperimentalComposeUiApi
        actual val N = Key(0x4E)

        /** 'O' key. */
        @ExperimentalComposeUiApi
        actual val O = Key(0x4F)

        /** 'P' key. */
        @ExperimentalComposeUiApi
        actual val P = Key(0x50)

        /** 'Q' key. */
        @ExperimentalComposeUiApi
        actual val Q = Key(0x51)

        /** 'R' key. */
        @ExperimentalComposeUiApi
        actual val R = Key(0x52)

        /** 'S' key. */
        @ExperimentalComposeUiApi
        actual val S = Key(0x53)

        /** 'T' key. */
        @ExperimentalComposeUiApi
        actual val T = Key(0x54)

        /** 'U' key. */
        @ExperimentalComposeUiApi
        actual val U = Key(0x55)

        /** 'V' key. */
        @ExperimentalComposeUiApi
        actual val V = Key(0x56)

        /** 'W' key. */
        @ExperimentalComposeUiApi
        actual val W = Key(0x57)

        /** 'X' key. */
        @ExperimentalComposeUiApi
        actual val X = Key(0x58)

        /** 'Y' key. */
        @ExperimentalComposeUiApi
        actual val Y = Key(0x59)

        /** 'Z' key. */
        @ExperimentalComposeUiApi
        actual val Z = Key(0x5A)

        /** ',' key. */
        @ExperimentalComposeUiApi
        actual val Comma = Key(0x2C)

        /** '.' key. */
        @ExperimentalComposeUiApi
        actual val Period = Key(0x2E)

        /** Left Alt modifier key. */
        @ExperimentalComposeUiApi
        actual val AltLeft = Key(0x12, 2)

        /** Right Alt modifier key. */
        @ExperimentalComposeUiApi
        actual val AltRight = Key(0x12, 3)

        /** Left Shift modifier key. */
        @ExperimentalComposeUiApi
        actual val ShiftLeft = Key(0x10, 2)

        /** Right Shift modifier key. */
        @ExperimentalComposeUiApi
        actual val ShiftRight = Key(0x10, 3)

        /** Tab key. */
        @ExperimentalComposeUiApi
        actual val Tab = Key('\t'.code)

        /** Space key. */
        @ExperimentalComposeUiApi
        actual val Spacebar = Key(0x20)

        /** Enter key. */
        @ExperimentalComposeUiApi
        actual val Enter = Key('\n'.code)

        /**
         * Backspace key.
         *
         * Deletes characters before the insertion point, unlike [Delete].
         */
        @ExperimentalComposeUiApi
        actual val Backspace = Key('\b'.code)

        /**
         * Delete key.
         *
         * Deletes characters ahead of the insertion point, unlike [Backspace].
         */
        @ExperimentalComposeUiApi
        actual val Delete = Key(0x7F)

        /** Escape key. */
        @ExperimentalComposeUiApi
        actual val Escape = Key(0x1B)

        /** Left Control modifier key. */
        @ExperimentalComposeUiApi
        actual val CtrlLeft = Key(0x11, 2)

        /** Right Control modifier key. */
        @ExperimentalComposeUiApi
        actual val CtrlRight = Key(0x11, 3)

        /** Caps Lock key. */
        @ExperimentalComposeUiApi
        actual val CapsLock = Key(0x14)

        /** Scroll Lock key. */
        @ExperimentalComposeUiApi
        actual val ScrollLock = Key(0x91)

        /** Left Meta modifier key. */
        @ExperimentalComposeUiApi
        actual val MetaLeft = Key(0x9D, 2)

        /** Right Meta modifier key. */
        @ExperimentalComposeUiApi
        actual val MetaRight = Key(0x9D, 3)

        /** System Request / Print Screen key. */
        @ExperimentalComposeUiApi
        actual val PrintScreen = Key(0x9A)

        /**
         * Insert key.
         *
         * Toggles insert / overwrite edit mode.
         */
        @ExperimentalComposeUiApi
        actual val Insert = Key(0x9B)

        /** Cut key. */
        @ExperimentalComposeUiApi
        actual val Cut = Key(0xFFD1)

        /** Copy key. */
        @ExperimentalComposeUiApi
        actual val Copy = Key(0xFFCD)

        /** Paste key. */
        @ExperimentalComposeUiApi
        actual val Paste = Key(0xFFCF)

        /** '`' (backtick) key. */
        @ExperimentalComposeUiApi
        actual val Grave = Key(0xC0)

        /** '[' key. */
        @ExperimentalComposeUiApi
        actual val LeftBracket = Key(0x5B)

        /** ']' key. */
        @ExperimentalComposeUiApi
        actual val RightBracket = Key(0x5D)

        /** '/' key. */
        @ExperimentalComposeUiApi
        actual val Slash = Key(0x2F)

        /** '\' key. */
        @ExperimentalComposeUiApi
        actual val Backslash = Key(0x5C)

        /** ';' key. */
        @ExperimentalComposeUiApi
        actual val Semicolon = Key(0x3B)

        /** ''' (apostrophe) key. */
        @ExperimentalComposeUiApi
        actual val Apostrophe = Key(0xDE)

        /** '@' key. */
        @ExperimentalComposeUiApi
        actual val At = Key(0x0200)

        /** Page Up key. */
        @ExperimentalComposeUiApi
        actual val PageUp = Key(0x21)

        /** Page Down key. */
        @ExperimentalComposeUiApi
        actual val PageDown = Key(0x22)

        /** F1 key. */
        @ExperimentalComposeUiApi
        actual val F1 = Key(0x70)

        /** F2 key. */
        @ExperimentalComposeUiApi
        actual val F2 = Key(0x71)

        /** F3 key. */
        @ExperimentalComposeUiApi
        actual val F3 = Key(0x72)

        /** F4 key. */
        @ExperimentalComposeUiApi
        actual val F4 = Key(0x73)

        /** F5 key. */
        @ExperimentalComposeUiApi
        actual val F5 = Key(0x74)

        /** F6 key. */
        @ExperimentalComposeUiApi
        actual val F6 = Key(0x75)

        /** F7 key. */
        @ExperimentalComposeUiApi
        actual val F7 = Key(0x76)

        /** F8 key. */
        @ExperimentalComposeUiApi
        actual val F8 = Key(0x77)

        /** F9 key. */
        @ExperimentalComposeUiApi
        actual val F9 = Key(0x78)

        /** F10 key. */
        @ExperimentalComposeUiApi
        actual val F10 = Key(0x79)

        /** F11 key. */
        @ExperimentalComposeUiApi
        actual val F11 = Key(0x7A)

        /** F12 key. */
        @ExperimentalComposeUiApi
        actual val F12 = Key(0x7B)

        /**
         * Num Lock key.
         *
         * This is the Num Lock key; it is different from [Number].
         * This key alters the behavior of other keys on the numeric keypad.
         */
        @ExperimentalComposeUiApi
        actual val NumLock = Key(0x90, 4)

        /** Numeric keypad '0' key. */
        @ExperimentalComposeUiApi
        actual val NumPad0 = Key(0x60, 4)

        /** Numeric keypad '1' key. */
        @ExperimentalComposeUiApi
        actual val NumPad1 = Key(0x61, 4)

        /** Numeric keypad '2' key. */
        @ExperimentalComposeUiApi
        actual val NumPad2 = Key(0x62, 4)

        /** Numeric keypad '3' key. */
        @ExperimentalComposeUiApi
        actual val NumPad3 = Key(0x63, 4)

        /** Numeric keypad '4' key. */
        @ExperimentalComposeUiApi
        actual val NumPad4 = Key(0x64, 4)

        /** Numeric keypad '5' key. */
        @ExperimentalComposeUiApi
        actual val NumPad5 = Key(0x65, 4)

        /** Numeric keypad '6' key. */
        @ExperimentalComposeUiApi
        actual val NumPad6 = Key(0x66, 4)

        /** Numeric keypad '7' key. */
        @ExperimentalComposeUiApi
        actual val NumPad7 = Key(0x67, 4)

        /** Numeric keypad '8' key. */
        @ExperimentalComposeUiApi
        actual val NumPad8 = Key(0x68, 4)

        /** Numeric keypad '9' key. */
        @ExperimentalComposeUiApi
        actual val NumPad9 = Key(0x69, 4)

        /** Numeric keypad '/' key (for division). */
        @ExperimentalComposeUiApi
        actual val NumPadDivide = Key(0x6F, 4)

        /** Numeric keypad '*' key (for multiplication). */
        @ExperimentalComposeUiApi
        actual val NumPadMultiply = Key(0x6A, 4)

        /** Numeric keypad '-' key (for subtraction). */
        @ExperimentalComposeUiApi
        actual val NumPadSubtract = Key(0x6D, 4)

        /** Numeric keypad '+' key (for addition). */
        @ExperimentalComposeUiApi
        actual val NumPadAdd = Key(0x6B, 4)

        /** Numeric keypad '.' key (for decimals or digit grouping). */
        @ExperimentalComposeUiApi
        actual val NumPadDot = Key(0x2E, 4)

        /** Numeric keypad ',' key (for decimals or digit grouping). */
        @ExperimentalComposeUiApi
        actual val NumPadComma = Key(0x2C, 4)

        /** Numeric keypad Enter key. */
        @ExperimentalComposeUiApi
        actual val NumPadEnter = Key('\n'.code, 4)

        /** Numeric keypad '=' key. */
        @ExperimentalComposeUiApi
        actual val NumPadEquals = Key(0x3D, 4)

        /** Numeric keypad '(' key. */
        @ExperimentalComposeUiApi
        actual val NumPadLeftParenthesis = Key(0x0207, 4)

        /** Numeric keypad ')' key. */
        @ExperimentalComposeUiApi
        actual val NumPadRightParenthesis = Key(0x020A, 4)

        @ExperimentalComposeUiApi
        actual val MoveHome = Key(0x24)
        @ExperimentalComposeUiApi
        actual val MoveEnd = Key(0x23)

        // Unsupported Keys. These keys will never be sent by the desktop. However we need unique
        // keycodes so that these constants can be used in a when statement without a warning.
        @ExperimentalComposeUiApi
        actual val SoftLeft = Key(-1000000001)
        @ExperimentalComposeUiApi
        actual val SoftRight = Key(-1000000002)
        @ExperimentalComposeUiApi
        actual val Back = Key(-1000000003)
        @ExperimentalComposeUiApi
        actual val NavigatePrevious = Key(-1000000004)
        @ExperimentalComposeUiApi
        actual val NavigateNext = Key(-1000000005)
        @ExperimentalComposeUiApi
        actual val NavigateIn = Key(-1000000006)
        @ExperimentalComposeUiApi
        actual val NavigateOut = Key(-1000000007)
        @ExperimentalComposeUiApi
        actual val SystemNavigationUp = Key(-1000000008)
        @ExperimentalComposeUiApi
        actual val SystemNavigationDown = Key(-1000000009)
        @ExperimentalComposeUiApi
        actual val SystemNavigationLeft = Key(-1000000010)
        @ExperimentalComposeUiApi
        actual val SystemNavigationRight = Key(-1000000011)
        @ExperimentalComposeUiApi
        actual val Call = Key(-1000000012)
        @ExperimentalComposeUiApi
        actual val EndCall = Key(-1000000013)
        @ExperimentalComposeUiApi
        actual val DirectionCenter = Key(-1000000014)
        @ExperimentalComposeUiApi
        actual val DirectionUpLeft = Key(-1000000015)
        @ExperimentalComposeUiApi
        actual val DirectionDownLeft = Key(-1000000016)
        @ExperimentalComposeUiApi
        actual val DirectionUpRight = Key(-1000000017)
        @ExperimentalComposeUiApi
        actual val DirectionDownRight = Key(-1000000018)
        @ExperimentalComposeUiApi
        actual val VolumeUp = Key(-1000000019)
        @ExperimentalComposeUiApi
        actual val VolumeDown = Key(-1000000020)
        @ExperimentalComposeUiApi
        actual val Power = Key(-1000000021)
        @ExperimentalComposeUiApi
        actual val Camera = Key(-1000000022)
        @ExperimentalComposeUiApi
        actual val Clear = Key(-1000000023)
        @ExperimentalComposeUiApi
        actual val Symbol = Key(-1000000024)
        @ExperimentalComposeUiApi
        actual val Browser = Key(-1000000025)
        @ExperimentalComposeUiApi
        actual val Envelope = Key(-1000000026)
        @ExperimentalComposeUiApi
        actual val Function = Key(-1000000027)
        @ExperimentalComposeUiApi
        actual val Break = Key(-1000000028)
        @ExperimentalComposeUiApi
        actual val Number = Key(-1000000031)
        @ExperimentalComposeUiApi
        actual val HeadsetHook = Key(-1000000032)
        @ExperimentalComposeUiApi
        actual val Focus = Key(-1000000033)
        @ExperimentalComposeUiApi
        actual val Menu = Key(-1000000034)
        @ExperimentalComposeUiApi
        actual val Notification = Key(-1000000035)
        @ExperimentalComposeUiApi
        actual val Search = Key(-1000000036)
        @ExperimentalComposeUiApi
        actual val PictureSymbols = Key(-1000000037)
        @ExperimentalComposeUiApi
        actual val SwitchCharset = Key(-1000000038)
        @ExperimentalComposeUiApi
        actual val ButtonA = Key(-1000000039)
        @ExperimentalComposeUiApi
        actual val ButtonB = Key(-1000000040)
        @ExperimentalComposeUiApi
        actual val ButtonC = Key(-1000000041)
        @ExperimentalComposeUiApi
        actual val ButtonX = Key(-1000000042)
        @ExperimentalComposeUiApi
        actual val ButtonY = Key(-1000000043)
        @ExperimentalComposeUiApi
        actual val ButtonZ = Key(-1000000044)
        @ExperimentalComposeUiApi
        actual val ButtonL1 = Key(-1000000045)
        @ExperimentalComposeUiApi
        actual val ButtonR1 = Key(-1000000046)
        @ExperimentalComposeUiApi
        actual val ButtonL2 = Key(-1000000047)
        @ExperimentalComposeUiApi
        actual val ButtonR2 = Key(-1000000048)
        @ExperimentalComposeUiApi
        actual val ButtonThumbLeft = Key(-1000000049)
        @ExperimentalComposeUiApi
        actual val ButtonThumbRight = Key(-1000000050)
        @ExperimentalComposeUiApi
        actual val ButtonStart = Key(-1000000051)
        @ExperimentalComposeUiApi
        actual val ButtonSelect = Key(-1000000052)
        @ExperimentalComposeUiApi
        actual val ButtonMode = Key(-1000000053)
        @ExperimentalComposeUiApi
        actual val Button1 = Key(-1000000054)
        @ExperimentalComposeUiApi
        actual val Button2 = Key(-1000000055)
        @ExperimentalComposeUiApi
        actual val Button3 = Key(-1000000056)
        @ExperimentalComposeUiApi
        actual val Button4 = Key(-1000000057)
        @ExperimentalComposeUiApi
        actual val Button5 = Key(-1000000058)
        @ExperimentalComposeUiApi
        actual val Button6 = Key(-1000000059)
        @ExperimentalComposeUiApi
        actual val Button7 = Key(-1000000060)
        @ExperimentalComposeUiApi
        actual val Button8 = Key(-1000000061)
        @ExperimentalComposeUiApi
        actual val Button9 = Key(-1000000062)
        @ExperimentalComposeUiApi
        actual val Button10 = Key(-1000000063)
        @ExperimentalComposeUiApi
        actual val Button11 = Key(-1000000064)
        @ExperimentalComposeUiApi
        actual val Button12 = Key(-1000000065)
        @ExperimentalComposeUiApi
        actual val Button13 = Key(-1000000066)
        @ExperimentalComposeUiApi
        actual val Button14 = Key(-1000000067)
        @ExperimentalComposeUiApi
        actual val Button15 = Key(-1000000068)
        @ExperimentalComposeUiApi
        actual val Button16 = Key(-1000000069)
        @ExperimentalComposeUiApi
        actual val Forward = Key(-1000000070)
        @ExperimentalComposeUiApi
        actual val MediaPlay = Key(-1000000071)
        @ExperimentalComposeUiApi
        actual val MediaPause = Key(-1000000072)
        @ExperimentalComposeUiApi
        actual val MediaPlayPause = Key(-1000000073)
        @ExperimentalComposeUiApi
        actual val MediaStop = Key(-1000000074)
        @ExperimentalComposeUiApi
        actual val MediaRecord = Key(-1000000075)
        @ExperimentalComposeUiApi
        actual val MediaNext = Key(-1000000076)
        @ExperimentalComposeUiApi
        actual val MediaPrevious = Key(-1000000077)
        @ExperimentalComposeUiApi
        actual val MediaRewind = Key(-1000000078)
        @ExperimentalComposeUiApi
        actual val MediaFastForward = Key(-1000000079)
        @ExperimentalComposeUiApi
        actual val MediaClose = Key(-1000000080)
        @ExperimentalComposeUiApi
        actual val MediaAudioTrack = Key(-1000000081)
        @ExperimentalComposeUiApi
        actual val MediaEject = Key(-1000000082)
        @ExperimentalComposeUiApi
        actual val MediaTopMenu = Key(-1000000083)
        @ExperimentalComposeUiApi
        actual val MediaSkipForward = Key(-1000000084)
        @ExperimentalComposeUiApi
        actual val MediaSkipBackward = Key(-1000000085)
        @ExperimentalComposeUiApi
        actual val MediaStepForward = Key(-1000000086)
        @ExperimentalComposeUiApi
        actual val MediaStepBackward = Key(-1000000087)
        @ExperimentalComposeUiApi
        actual val MicrophoneMute = Key(-1000000088)
        @ExperimentalComposeUiApi
        actual val VolumeMute = Key(-1000000089)
        @ExperimentalComposeUiApi
        actual val Info = Key(-1000000090)
        @ExperimentalComposeUiApi
        actual val ChannelUp = Key(-1000000091)
        @ExperimentalComposeUiApi
        actual val ChannelDown = Key(-1000000092)
        @ExperimentalComposeUiApi
        actual val ZoomIn = Key(-1000000093)
        @ExperimentalComposeUiApi
        actual val ZoomOut = Key(-1000000094)
        @ExperimentalComposeUiApi
        actual val Tv = Key(-1000000095)
        @ExperimentalComposeUiApi
        actual val Window = Key(-1000000096)
        @ExperimentalComposeUiApi
        actual val Guide = Key(-1000000097)
        @ExperimentalComposeUiApi
        actual val Dvr = Key(-1000000098)
        @ExperimentalComposeUiApi
        actual val Bookmark = Key(-1000000099)
        @ExperimentalComposeUiApi
        actual val Captions = Key(-1000000100)
        @ExperimentalComposeUiApi
        actual val Settings = Key(-1000000101)
        @ExperimentalComposeUiApi
        actual val TvPower = Key(-1000000102)
        @ExperimentalComposeUiApi
        actual val TvInput = Key(-1000000103)
        @ExperimentalComposeUiApi
        actual val SetTopBoxPower = Key(-1000000104)
        @ExperimentalComposeUiApi
        actual val SetTopBoxInput = Key(-1000000105)
        @ExperimentalComposeUiApi
        actual val AvReceiverPower = Key(-1000000106)
        @ExperimentalComposeUiApi
        actual val AvReceiverInput = Key(-1000000107)
        @ExperimentalComposeUiApi
        actual val ProgramRed = Key(-1000000108)
        @ExperimentalComposeUiApi
        actual val ProgramGreen = Key(-1000000109)
        @ExperimentalComposeUiApi
        actual val ProgramYellow = Key(-1000000110)
        @ExperimentalComposeUiApi
        actual val ProgramBlue = Key(-1000000111)
        @ExperimentalComposeUiApi
        actual val AppSwitch = Key(-1000000112)
        @ExperimentalComposeUiApi
        actual val LanguageSwitch = Key(-1000000113)
        @ExperimentalComposeUiApi
        actual val MannerMode = Key(-1000000114)
        @ExperimentalComposeUiApi
        actual val Toggle2D3D = Key(-1000000125)
        @ExperimentalComposeUiApi
        actual val Contacts = Key(-1000000126)
        @ExperimentalComposeUiApi
        actual val Calendar = Key(-1000000127)
        @ExperimentalComposeUiApi
        actual val Music = Key(-1000000128)
        @ExperimentalComposeUiApi
        actual val Calculator = Key(-1000000129)
        @ExperimentalComposeUiApi
        actual val ZenkakuHankaru = Key(-1000000130)
        @ExperimentalComposeUiApi
        actual val Eisu = Key(-1000000131)
        @ExperimentalComposeUiApi
        actual val Muhenkan = Key(-1000000132)
        @ExperimentalComposeUiApi
        actual val Henkan = Key(-1000000133)
        @ExperimentalComposeUiApi
        actual val KatakanaHiragana = Key(-1000000134)
        @ExperimentalComposeUiApi
        actual val Yen = Key(-1000000135)
        @ExperimentalComposeUiApi
        actual val Ro = Key(-1000000136)
        @ExperimentalComposeUiApi
        actual val Kana = Key(-1000000137)
        @ExperimentalComposeUiApi
        actual val Assist = Key(-1000000138)
        @ExperimentalComposeUiApi
        actual val BrightnessDown = Key(-1000000139)
        @ExperimentalComposeUiApi
        actual val BrightnessUp = Key(-1000000140)
        @ExperimentalComposeUiApi
        actual val Sleep = Key(-1000000141)
        @ExperimentalComposeUiApi
        actual val WakeUp = Key(-1000000142)
        @ExperimentalComposeUiApi
        actual val SoftSleep = Key(-1000000143)
        @ExperimentalComposeUiApi
        actual val Pairing = Key(-1000000144)
        @ExperimentalComposeUiApi
        actual val LastChannel = Key(-1000000145)
        @ExperimentalComposeUiApi
        actual val TvDataService = Key(-1000000146)
        @ExperimentalComposeUiApi
        actual val VoiceAssist = Key(-1000000147)
        @ExperimentalComposeUiApi
        actual val TvRadioService = Key(-1000000148)
        @ExperimentalComposeUiApi
        actual val TvTeletext = Key(-1000000149)
        @ExperimentalComposeUiApi
        actual val TvNumberEntry = Key(-1000000150)
        @ExperimentalComposeUiApi
        actual val TvTerrestrialAnalog = Key(-1000000151)
        @ExperimentalComposeUiApi
        actual val TvTerrestrialDigital = Key(-1000000152)
        @ExperimentalComposeUiApi
        actual val TvSatellite = Key(-1000000153)
        @ExperimentalComposeUiApi
        actual val TvSatelliteBs = Key(-1000000154)
        @ExperimentalComposeUiApi
        actual val TvSatelliteCs = Key(-1000000155)
        @ExperimentalComposeUiApi
        actual val TvSatelliteService = Key(-1000000156)
        @ExperimentalComposeUiApi
        actual val TvNetwork = Key(-1000000157)
        @ExperimentalComposeUiApi
        actual val TvAntennaCable = Key(-1000000158)
        @ExperimentalComposeUiApi
        actual val TvInputHdmi1 = Key(-1000000159)
        @ExperimentalComposeUiApi
        actual val TvInputHdmi2 = Key(-1000000160)
        @ExperimentalComposeUiApi
        actual val TvInputHdmi3 = Key(-1000000161)
        @ExperimentalComposeUiApi
        actual val TvInputHdmi4 = Key(-1000000162)
        @ExperimentalComposeUiApi
        actual val TvInputComposite1 = Key(-1000000163)
        @ExperimentalComposeUiApi
        actual val TvInputComposite2 = Key(-1000000164)
        @ExperimentalComposeUiApi
        actual val TvInputComponent1 = Key(-1000000165)
        @ExperimentalComposeUiApi
        actual val TvInputComponent2 = Key(-1000000166)
        @ExperimentalComposeUiApi
        actual val TvInputVga1 = Key(-1000000167)
        @ExperimentalComposeUiApi
        actual val TvAudioDescription = Key(-1000000168)
        @ExperimentalComposeUiApi
        actual val TvAudioDescriptionMixingVolumeUp = Key(-1000000169)
        @ExperimentalComposeUiApi
        actual val TvAudioDescriptionMixingVolumeDown = Key(-1000000170)
        @ExperimentalComposeUiApi
        actual val TvZoomMode = Key(-1000000171)
        @ExperimentalComposeUiApi
        actual val TvContentsMenu = Key(-1000000172)
        @ExperimentalComposeUiApi
        actual val TvMediaContextMenu = Key(-1000000173)
        @ExperimentalComposeUiApi
        actual val TvTimerProgramming = Key(-1000000174)
        @ExperimentalComposeUiApi
        actual val StemPrimary = Key(-1000000175)
        @ExperimentalComposeUiApi
        actual val Stem1 = Key(-1000000176)
        @ExperimentalComposeUiApi
        actual val Stem2 = Key(-1000000177)
        @ExperimentalComposeUiApi
        actual val Stem3 = Key(-1000000178)
        @ExperimentalComposeUiApi
        actual val AllApps = Key(-1000000179)
        @ExperimentalComposeUiApi
        actual val Refresh = Key(-1000000180)
        @ExperimentalComposeUiApi
        actual val ThumbsUp = Key(-1000000181)
        @ExperimentalComposeUiApi
        actual val ThumbsDown = Key(-1000000182)
        @ExperimentalComposeUiApi
        actual val ProfileSwitch = Key(-1000000183)
    }

    actual override fun toString(): String {
        return "Key: ${KeyEvent.getKeyText(nativeKeyCode)}"
    }
}

/**
 * Creates instance of [Key].
 *
 * @param nativeKeyCode represents this key as defined in [java.awt.event.KeyEvent]
 * @param nativeKeyLocation represents the location of key as defined in [java.awt.event.KeyEvent]
 */
fun Key(nativeKeyCode: Int, nativeKeyLocation: Int = KEY_LOCATION_STANDARD): Key {
    // First 32 bits are for keycode.
    val keyCode = nativeKeyCode.toLong().shl(32)

    // Next 3 bits are for location.
    val location = (nativeKeyLocation.toLong() and 0x7).shl(29)

    return Key(keyCode or location)
}

/**
 * The native keycode corresponding to this [Key].
 */
val Key.nativeKeyCode: Int
    get() = unpackInt1(keyCode)

/**
 * The native location corresponding to this [Key].
 */
val Key.nativeKeyLocation: Int
    get() = (keyCode and 0xFFFFFFFF).shr(29).toInt()
