@file:Suppress("EXPERIMENTAL_UNSIGNED_LITERALS")

package org.example.image_show

import glib2.gpointer
import gtk3.GtkAlign
import gtk3.GtkOrientation
import gtk3.GtkToolButton
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.StableRef
import kotlinx.cinterop.asStableRef
import kotlinx.cinterop.staticCFunction
import org.guiVista.gui.GuiApplication
import org.guiVista.gui.layout.Container
import org.guiVista.gui.layout.boxLayout
import org.guiVista.gui.widget.display.imageWidget
import org.guiVista.gui.widget.tool.item.toolButtonWidget
import org.guiVista.gui.widget.tool.toolBarWidget
import org.guiVista.gui.window.AppWindow

private const val NO_IMAGE_POS = -1

class MainWindow(app: GuiApplication) : AppWindow(app) {
    private var imagePos = NO_IMAGE_POS
    private val nextBtn by lazy { createNextBtn() }
    private val prevBtn by lazy { createPrevBtn() }
    private val image by lazy { createImage() }
    val stableRef = StableRef.create(this)

    override fun createMainLayout(): Container? = boxLayout(orientation = GtkOrientation.GTK_ORIENTATION_VERTICAL) {
        this += toolBarWidget {
            this += prevBtn
            this += nextBtn
        }
        this += image
        borderWidth = 5u
    }

    private fun createNextBtn() = toolButtonWidget(iconWidget = null, label = "Next") {
        iconName = "go-next"
        tooltipText = "Next image"
        connectClickedSignal(staticCFunction(::nextBtnClicked), stableRef.asCPointer())
    }

    private fun createPrevBtn() = toolButtonWidget(iconWidget = null, label = "Previous") {
        iconName = "go-previous"
        tooltipText = "Previous image"
        connectClickedSignal(staticCFunction(::prevBtnClicked), stableRef.asCPointer())
    }

    override fun resetFocus() {
        nextBtn.grabFocus()
    }

    private fun createImage() = imageWidget {
        if (imageNames.isNotEmpty()) {
            imagePos = 0
            changeFile("$picturesDirPath/${imageNames.first()}")
            tooltipText = imageNames.first()
        }
        hAlign = GtkAlign.GTK_ALIGN_CENTER
        hExpand = true
        vExpand = true
        marginTop = 10
    }

    fun nextImage() {
        val range = 0 until imageNames.size - 1
        if (imageNames.isNotEmpty() && (imagePos + 1) in range) {
            imagePos += 1
            image.changeFile("$picturesDirPath/${imageNames[imagePos]}")
            image.tooltipText = imageNames[imagePos]
        }
    }

    fun previousImage() {
        val range = 0 until imageNames.size - 1
        if (imageNames.isNotEmpty() && (imagePos - 1) in range) {
            imagePos -= 1
            image.changeFile("$picturesDirPath/${imageNames[imagePos]}")
            image.tooltipText = imageNames[imagePos]
        }
    }

    override fun close() {
        super.close()
        stableRef.dispose()
    }
}

private fun nextBtnClicked(@Suppress("UNUSED_PARAMETER") button: CPointer<GtkToolButton>?, userData: gpointer) {
    val mainWin = userData.asStableRef<MainWindow>().get()
    mainWin.nextImage()
}

private fun prevBtnClicked(@Suppress("UNUSED_PARAMETER") button: CPointer<GtkToolButton>?, userData: gpointer) {
    val mainWin = userData.asStableRef<MainWindow>().get()
    mainWin.previousImage()
}
