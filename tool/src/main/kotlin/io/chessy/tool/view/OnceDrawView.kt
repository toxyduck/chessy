package io.chessy.tool.view

import java.awt.Graphics
import java.awt.image.BufferedImage

class OnceDrawView(private val wrappedView: View) : View by wrappedView {

    private val buffer by lazy { BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR) }
    private var isRendered = false

    override fun draw(graphics: Graphics) {
        if (!isRendered) {
            wrappedView.draw(buffer.graphics)
        }
        graphics.drawImage(
            buffer,
            x,
            y,
            width,
            height,
            null
        )
    }
}