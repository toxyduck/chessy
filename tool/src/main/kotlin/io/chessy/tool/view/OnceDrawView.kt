package io.chessy.tool.view

import java.awt.Graphics
import java.awt.image.BufferedImage

class OnceDrawView(private val wrappedView: View) : View by wrappedView {

    private val buffer by lazy { BufferedImage(wrappedView.width, wrappedView.height, BufferedImage.TYPE_INT_ARGB) }
    private var isRendered = false

    override fun draw(graphics: Graphics) {
        if (!isRendered) {
            val movedView = wrappedView.move(0, 0)
            movedView.draw(buffer.graphics)
            isRendered = true
        }
        graphics.drawImage(
            buffer,
            wrappedView.x,
            wrappedView.y,
            wrappedView.width,
            wrappedView.height,
            null
        )
    }

    override fun copy(x: Int, y: Int, width: Int, height: Int): View {
        return OnceDrawView(wrappedView.copy(x, y, width, height))
    }
}