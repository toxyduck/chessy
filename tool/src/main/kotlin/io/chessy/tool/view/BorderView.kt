package io.chessy.tool.view

import java.awt.Color
import java.awt.Graphics
import kotlin.math.min

class BorderView(
    override val x: Int,
    override val y: Int,
    override val width: Int,
    override val height: Int,
    private val borderSize: Int
) :
    ViewGroup<Nothing>() {
    override fun copy(x: Int, y: Int, width: Int, height: Int): View {
        return BorderView(x, y, width, height, borderSize)
    }

    override fun draw(graphics: Graphics) {
        graphics.color = grayColor
        graphics.fillRect(x, y, width, height)
        super.draw(graphics)
    }

    private val cellSize: Int = min(width - 2 * borderSize, height - 2 * borderSize) / 8

    init {
        views().forEach { addChild(it) }
    }

    private fun views(): List<SymbolView> {
        val numberBorder = ('1' until '9').mapIndexed { ix, symbol->
            val yCoord = y + height - borderSize - ix * cellSize - cellSize / 2
            val xCoord = x + borderSize / 2
            SymbolView(
                x = xCoord,
                y = yCoord,
                symbol = symbol,
                color = whiteColor,
                fontName = FONT_NAME,
                size = FONT_SIZE
            )
        }
        val symbolBorder = ('a' until 'i').mapIndexed { ix, symbol ->
            SymbolView(
                x = x + borderSize + ix * cellSize + cellSize / 2,
                y = width - borderSize / 2,
                symbol = symbol,
                color = whiteColor,
                fontName = FONT_NAME,
                size = FONT_SIZE
            )
        }
        return numberBorder + symbolBorder
    }

    companion object{
        private const val FONT_NAME = "SansSerif"
        private const val FONT_SIZE = 32
        private val whiteColor = Color.decode("#FFFFFF")
        private val grayColor = Color.decode("#AAAAAA")
    }
}