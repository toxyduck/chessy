package io.chessy.tool.view

import java.awt.Graphics
import java.awt.image.BufferedImage

class OnceDrawView(private val wrappedView: View) : View by wrappedView {

    private val buffer by lazy { BufferedImage(1080, 1920, BufferedImage.TYPE_3BYTE_BGR) }
    private var isRendered = false


    override fun draw(graphics: Graphics) {
        if (!isRendered) {
            wrappedView.draw(buffer.graphics)
            isRendered = true
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