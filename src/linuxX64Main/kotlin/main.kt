@file:Suppress("EXPERIMENTAL_UNSIGNED_LITERALS")

package org.example.image_show

import gtk3.GApplication
import gtk3.GtkWindowPosition
import gtk3.gpointer
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.staticCFunction
import org.gui_vista.core.Application
import org.gui_vista.core.fetchEmptyDataPointer

private lateinit var mainWin: MainWindow
private var activateHandlerId = 0uL

fun main() {
    Application(id = "org.example.image-show").use {
        mainWin = MainWindow(this)
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
        changePosition(GtkWindowPosition.GTK_WIN_POS_CENTER)
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
