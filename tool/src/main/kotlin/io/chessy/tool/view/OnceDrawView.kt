package io.chessy.tool.view

import io.chessy.tool.primitive.MovableView
import java.awt.Graphics
import java.awt.image.BufferedImage

class OnceDrawView(private val wrappedView: MovableView) : View by wrappedView {

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
}