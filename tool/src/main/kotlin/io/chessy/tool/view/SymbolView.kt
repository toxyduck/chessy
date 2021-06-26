package io.chessy.tool.view

import java.awt.Color
import java.awt.Font
import java.awt.Graphics

class SymbolView(
    override val x: Int,
    override val y: Int,
    private val symbol: Char,
    private val color: Color,
    private val font: Font
) : View {

    override val height: Int = 0
    override val width: Int = 0

    override fun draw(graphics: Graphics) {
        graphics.color = color
        graphics.font = font
        val size = measure(graphics, font, symbol)
        graphics.drawString(symbol.toString(), x, (y + size.height))
    }

    override fun copy(x: Int, y: Int, width: Int, height: Int): View {
        return SymbolView(x, y, symbol, color, font)
    }

    class Size(val width: Int, val height: Int)

    companion object {
        fun measure(graphics: Graphics, font: Font, symbol: Char): Size {
            val rect = graphics.getFontMetrics(font).getStringBounds(symbol.toString(), graphics)
            val height = (font.size / rect.height) * font.size
            return Size(rect.width.toInt(), height.toInt())
        }
    }
}