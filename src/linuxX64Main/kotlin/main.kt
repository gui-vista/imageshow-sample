@file:Suppress("EXPERIMENTAL_UNSIGNED_LITERALS")

package org.example.image_show

import gtk3.GApplication
import gtk3.gpointer
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.staticCFunction
import org.guivista.core.Application
import org.guivista.core.fetchEmptyDataPointer
import org.guivista.core.window.AppWindow

private lateinit var mainWin: AppWindow
private var activateHandlerId = 0uL

fun main() {
    Application(id = "org.example.image-show").use {
        mainWin = AppWindow(this)
        activateHandlerId = connectActivateSignal(staticCFunction(::activateApplication), fetchEmptyDataPointer())
        connectShutdownSignal(staticCFunction(::shutdownApplication), fetchEmptyDataPointer())
        println("Application Status: ${run()}")
    }
}

@Suppress("UNUSED_PARAMETER")
private fun activateApplication(app: CPointer<GApplication>, userData: gpointer) {
    println("Activating application...")
    mainWin.createUi {
        title = "Image Show"
        changeDefaultSize(width = 400, height = 400)
        visible = true
    }
}

private fun shutdownApplication(app: CPointer<GApplication>, @Suppress("UNUSED_PARAMETER") userData: gpointer) {
    println("Shutting down application...")
    with(Application(appPtr = app)) {
        disconnectSignal(activateHandlerId)
        println("Application ID: $appId")
    }
}
