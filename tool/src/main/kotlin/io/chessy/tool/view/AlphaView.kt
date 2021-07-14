package io.chessy.tool.view

import java.awt.AlphaComposite
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.image.BufferedImage

class AlphaView(private val wrappedView: View, alpha: Float) : View by wrappedView {

    private val buffer by lazy { BufferedImage(wrappedView.width, wrappedView.height, BufferedImage.TYPE_INT_ARGB) }

    private val alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha)

    private val fullAlpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f)

    override fun draw(graphics: Graphics) {
        if (graphics is Graphics2D) {
            val movedView = wrappedView.move(0, 0)
            movedView.draw(buffer.graphics)
            graphics.composite = alphaComposite
            graphics.drawImage(
                buffer,
                wrappedView.x,
                wrappedView.y,
                wrappedView.width,
                wrappedView.height,
                null
            )
            graphics.composite = fullAlpha
        }
    }

    fun transparent(alpha: Float): AlphaView {
        return AlphaView(wrappedView, alpha)
    }
}