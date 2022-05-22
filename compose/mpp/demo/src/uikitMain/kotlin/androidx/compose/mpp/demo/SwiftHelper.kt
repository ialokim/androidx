package androidx.compose.mpp.demo

import platform.UIKit.UIViewController

class SwiftHelper {
    fun getViewController(): UIViewController {
        println("getViewController ")
        return getSkikoViewContoller()
    }
}
