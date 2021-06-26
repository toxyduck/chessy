package io.chessy.tool.view

import java.awt.Color
import java.awt.Font
import java.awt.Graphics
import kotlin.math.min

class BorderView(
    override val x: Int,
    override val y: Int,
    override val width: Int,
    override val height: Int,
    private val borderSize: Int,
    private val symbolPadding: Int,
    private val symbolHeight: Int,
    private val symbolWidth: Int,
) : ViewGroup<Nothing>() {

    override fun copy(x: Int, y: Int, width: Int, height: Int): View {
        return BorderView(x, y, width, height, borderSize, symbolPadding, symbolHeight, symbolWidth)
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
        val numberBorder = ('1' until '9').mapIndexed { ix, symbol ->
            val yCoord = y + height - borderSize - ix * cellSize - cellSize / 2 - symbolHeight / 2
            val xCoord = x + symbolPadding
            SymbolView(
                x = xCoord,
                y = yCoord,
                symbol = symbol,
                color = whiteColor,
                font = FONT
            )
        }
        val symbolBorder = ('A' until 'I').mapIndexed { ix, symbol ->
            SymbolView(
                x = x + borderSize + ix * cellSize + cellSize / 2 - symbolWidth / 2,
                y = width - borderSize + symbolPadding,
                symbol = symbol,
                color = whiteColor,
                font = FONT
            )
        }
        return numberBorder + symbolBorder
    }

    class BorderViewConfig(
        val borderSize: Int,
        val symbolPadding: Int,
        val symbolHeight: Int,
        val symbolWidth: Int,
        val font: Font,
        val color: Color
    )

    companion object{
        private const val FONT_NAME = "SansSerif"
        private const val FONT_SIZE = 32
        private const val FONT_STYLE = Font.PLAIN
        private val FONT = Font(FONT_NAME, FONT_STYLE, FONT_SIZE)
        private val whiteColor = Color.decode("#FFFFFF")
        private val grayColor = Color.decode("#AAAAAA")
    }
}