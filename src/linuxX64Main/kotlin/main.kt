@file:Suppress("EXPERIMENTAL_UNSIGNED_LITERALS")

package org.example.image_show

import gio2.G_FILE_ATTRIBUTE_STANDARD_NAME
import gio2.G_FILE_QUERY_INFO_NONE
import gio2.G_FILE_TYPE_DIRECTORY
import glib2.gpointer
import gtk3.GApplication
import gtk3.GtkWindowPosition
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.staticCFunction
import kotlinx.cinterop.toKString
import org.guiVista.gui.Application
import org.guiVista.core.fetchEmptyDataPointer
import org.guiVista.io.File
import platform.posix.getenv

private const val PNG_EXT = ".png"
private const val TIF_EXT = ".tif"
private const val JPEG_EXT = ".jpeg"
private val picturesDir = File.fromPath("${getenv("HOME")?.toKString()}/Pictures")
val picturesDirPath = picturesDir.path
val images by lazy { fetchImageFiles(picturesDir) }
private lateinit var mainWin: MainWindow
private var activateHandlerId = 0uL

fun main() {
    printImageNames()
    Application(id = "org.example.image-show").use {
        mainWin = MainWindow(this)
        activateHandlerId = connectActivateSignal(staticCFunction(::activateApplication), fetchEmptyDataPointer())
        connectShutdownSignal(staticCFunction(::shutdownApplication), fetchEmptyDataPointer())
        println("Application Status: ${run()}")
    }
}

private fun printImageNames() {
    if (images.isNotEmpty()) {
        println("-- Images (from $picturesDirPath) --")
        images.forEach { println("* $it") }
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
    println("Application ID: ${Application(appPtr = app).appId}")
    mainWin.createUi {
        resizable = false
        title = "Image Show"
        changeDefaultSize(width = 400, height = 400)
        changePosition(GtkWindowPosition.GTK_WIN_POS_CENTER)
        visible = true
    }
}

private fun shutdownApplication(app: CPointer<GApplication>, @Suppress("UNUSED_PARAMETER") userData: gpointer) {
    println("Shutting down application...")
    Application(appPtr = app).disconnectSignal(activateHandlerId)
}
