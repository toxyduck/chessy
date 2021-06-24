package io.chessy.tool.view

import java.awt.Color
import java.awt.Font
import java.awt.Graphics

class SymbolView(
    override val x: Int,
    override val y: Int,
    private val symbol: Char,
    private val size: Int,
    private val color: Color,
    private val fontName: String
) : View {

    override val height: Int = 0
    override val width: Int = 0

    override fun draw(graphics: Graphics) {
        graphics.color = color
        val font = Font(fontName, Font.PLAIN, size)
        graphics.font = font
        val rect = graphics.getFontMetrics(font).getStringBounds(symbol.toString(), graphics)
        graphics.drawString(symbol.toString(), x, (y + (size / rect.height) * size).toInt())
    }

    override fun copy(x: Int, y: Int, width: Int, height: Int): View {
        return SymbolView(x, y, symbol, size, color, fontName)
    }
}