package io.chessy.tool.view

import io.chessy.tool.primitive.MovableView
import java.awt.Color
import java.awt.Graphics

class CellView(
    override val x: Int,
    override val y: Int,
    override val width: Int,
    override val height: Int,
    private val color: Color
) : MovableView {
    override fun draw(graphics: Graphics) {
        graphics.color = color
        graphics.fillRect(x, y, width, height)
    }

    override fun move(x: Int, y: Int): MovableView {
        return CellView(x, y, width, height, color)
    }
}