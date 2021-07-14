package io.chessy.tool.view

import java.awt.Color
import java.awt.Graphics

class FillView(
    override val x: Int,
    override val y: Int,
    override val width: Int,
    override val height: Int,
    private val color: Color
) : View {

    override fun draw(graphics: Graphics) {
        graphics.color = color
        graphics.fillRect(x, y, width, height)
    }

    override fun copy(x: Int, y: Int, width: Int, height: Int): View {
        return FillView(x, y, width, height, color)
    }

    fun recolor(color: Color): FillView {
        return FillView(x, y, width, height, color)
    }
}