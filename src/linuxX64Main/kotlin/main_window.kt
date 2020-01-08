@file:Suppress("EXPERIMENTAL_UNSIGNED_LITERALS")

package org.example.image_show

import gtk3.GtkAlign
import gtk3.GtkOrientation
import kotlinx.cinterop.toKString
import org.gui_vista.core.Application
import org.gui_vista.core.layout.Container
import org.gui_vista.core.window.AppWindow
import org.gui_vista.core.layout.boxLayout
import org.gui_vista.core.widget.button.buttonWidget
import org.gui_vista.core.widget.display.imageWidget
import platform.posix.getenv

class MainWindow(app: Application) : AppWindow(app) {
    private val nextBtn by lazy { buttonWidget { label = "Next" } }
    private val prevBtn by lazy { buttonWidget { label = "Previous" } }

    override fun createMainLayout(): Container? = boxLayout(orientation = GtkOrientation.GTK_ORIENTATION_VERTICAL) {
        this += createImage()
        this += boxLayout {
            this += prevBtn
            this += nextBtn
            spacing = 5
        }
        borderWidth = 5u
    }

    override fun resetFocus() {
        nextBtn.grabFocus()
    }

    private fun createImage() = imageWidget {
        val imageFile = "${getenv("HOME")?.toKString() ?: ""}/Pictures/ThinkPad Dog 1.jpg"
        tooltipText = "ThinkPad Dog"
        changeFile(imageFile)
        hAlign = GtkAlign.GTK_ALIGN_CENTER
        marginBottom = 10
    }
}
