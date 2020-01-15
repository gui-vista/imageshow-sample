@file:Suppress("EXPERIMENTAL_UNSIGNED_LITERALS")

package org.example.image_show

import gio2.GApplication
import gio2.G_FILE_ATTRIBUTE_STANDARD_NAME
import gio2.G_FILE_QUERY_INFO_NONE
import gio2.G_FILE_TYPE_DIRECTORY
import glib2.gpointer
import gtk3.GtkWindowPosition
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.staticCFunction
import kotlinx.cinterop.toKString
import org.guiVista.core.fetchEmptyDataPointer
import org.guiVista.gui.GuiApplication
import org.guiVista.io.File
import platform.posix.getenv

private const val PNG_EXT = ".png"
private const val TIF_EXT = ".tif"
private const val JPEG_EXT = ".jpeg"
private val picturesDir = File.fromPath("${getenv("HOME")?.toKString()}/Pictures")
val picturesDirPath = picturesDir.path
val imageNames by lazy { fetchImageFiles(picturesDir) }
private lateinit var mainWin: MainWindow
private var activateHandlerId = 0uL

fun main() {
    printImageNames()
    GuiApplication(id = "org.example.image-show").use {
        mainWin = MainWindow(this)
        // Connect the "activate" signal, and get the Handler ID to disconnect the signal later.
        activateHandlerId = connectActivateSignal(staticCFunction(::activateApplication), fetchEmptyDataPointer())
        connectShutdownSignal(staticCFunction(::shutdownApplication), fetchEmptyDataPointer())
        println("Application Status: ${run()}")
    }
}

private fun printImageNames() {
    if (imageNames.isNotEmpty()) {
        println("-- Images (from $picturesDirPath) --")
        imageNames.forEach { println("* $it") }
    }
}

private fun fetchImageFiles(dir: File): Array<String> {
    val tmp = mutableListOf<String>()
    val fileType = dir.queryFileType(G_FILE_QUERY_INFO_NONE)
    if (fileType == G_FILE_TYPE_DIRECTORY) {
        val enumerator = dir.enumerateChildren(G_FILE_ATTRIBUTE_STANDARD_NAME, 0u)
        var fileInfo = enumerator?.nextFile()
        while (fileInfo != null) {
            tmp += fileInfo.name
            // It is important to close fileInfo otherwise a memory leak WILL occur.
            fileInfo.close()
            fileInfo = enumerator?.nextFile()
        }
        enumerator?.close()
    }
    return tmp.filter { it.endsWith(PNG_EXT) || it.endsWith(TIF_EXT) || it.endsWith(JPEG_EXT) }.toTypedArray()
}

@Suppress("UNUSED_PARAMETER")
private fun activateApplication(app: CPointer<GApplication>, userData: gpointer) {
    println("Activating application...")
    // Making use of the CPointer Event Reference technique to gain access to state.
    println("Application ID: ${GuiApplication(appPtr = app).appId}")
    mainWin.createUi {
        title = "Image Show"
        changeDefaultSize(width = 400, height = 400)
        changePosition(GtkWindowPosition.GTK_WIN_POS_CENTER)
        visible = true
    }
}

private fun shutdownApplication(app: CPointer<GApplication>, @Suppress("UNUSED_PARAMETER") userData: gpointer) {
    println("Shutting down application...")
    GuiApplication(appPtr = app).disconnectSignal(activateHandlerId)
}
