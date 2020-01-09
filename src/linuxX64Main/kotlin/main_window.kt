@file:Suppress("EXPERIMENTAL_UNSIGNED_LITERALS")

package org.example.image_show

import gtk3.GtkAlign
import gtk3.GtkOrientation
import gtk3.GtkToolButton
import gtk3.gpointer
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.staticCFunction
import kotlinx.cinterop.toKString
import org.gui_vista.core.Application
import org.gui_vista.core.fetchEmptyDataPointer
import org.gui_vista.core.layout.Container
import org.gui_vista.core.layout.boxLayout
import org.gui_vista.core.widget.display.imageWidget
import org.gui_vista.core.widget.tool.item.toolButtonWidget
import org.gui_vista.core.widget.tool.toolBarWidget
import org.gui_vista.core.window.AppWindow
import platform.posix.getenv

class MainWindow(app: Application) : AppWindow(app) {
    private val nextBtn by lazy { createNextBtn() }
    private val prevBtn by lazy { createPrevBtn() }

    override fun createMainLayout(): Container? = boxLayout(orientation = GtkOrientation.GTK_ORIENTATION_VERTICAL) {
        this += toolBarWidget {
            this += prevBtn
            this += nextBtn
        }
        this += createImage()
        borderWidth = 5u
    }

    private fun createNextBtn() = toolButtonWidget(iconWidget = null, label = "Next") {
        connectClickedSignal(staticCFunction(::nextBtnClicked), fetchEmptyDataPointer())
    }

    private fun createPrevBtn() = toolButtonWidget(iconWidget = null, label = "Previous") {
        connectClickedSignal(staticCFunction(::prevBtnClicked), fetchEmptyDataPointer())
    }

    override fun resetFocus() {
        nextBtn.grabFocus()
    }

    private fun createImage() = imageWidget {
        val imageFile = "${getenv("HOME")?.toKString() ?: ""}/Pictures/ThinkPad Dog 1.jpg"
        tooltipText = "ThinkPad Dog"
        changeFile(imageFile)
        hAlign = GtkAlign.GTK_ALIGN_CENTER
        hExpand = true
        vExpand = true
        marginTop = 10
    }
}

@Suppress("UNUSED_PARAMETER")
private fun nextBtnClicked(button: CPointer<GtkToolButton>?, userData: gpointer) {
    println("Next Button clicked.")
}

@Suppress("UNUSED_PARAMETER")
private fun prevBtnClicked(button: CPointer<GtkToolButton>?, userData: gpointer) {
    println("Previous Button clicked.")
}
