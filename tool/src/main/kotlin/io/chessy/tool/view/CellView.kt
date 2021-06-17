package io.chessy.tool.view

import java.awt.Color
import java.awt.Graphics

class CellView(
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
}