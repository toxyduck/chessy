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
    private val config: Config
) : ViewGroup<Nothing>() {

    override fun copy(x: Int, y: Int, width: Int, height: Int): View {
        return BorderView(x, y, width, height, config)
    }

    override fun draw(graphics: Graphics) {
        graphics.color = config.backgroundColor
        graphics.fillRect(x, y, width, height)
        super.draw(graphics)
    }

    private val cellSize: Int = min(width - 2 * config.borderSize, height - 2 * config.borderSize) / 8

    init {
        views().forEach { addChild(it) }
    }

    private fun views(): List<TextView> {
        val numberBorder = config.symbolsVertical.mapIndexed { ix, symbol ->
            val yCoord = y + height - config.borderSize - ix * cellSize - cellSize / 2 - config.symbolHeight / 2
            val xCoord = x + config.symbolPadding
            TextView(
                x = xCoord,
                y = yCoord,
                text = symbol.toString(),
                color = config.color,
                font = config.font
            )
        }
        val symbolBorder = config.symbolsHorizontal.mapIndexed { ix, symbol ->
            TextView(
                x = x + config.borderSize + ix * cellSize + cellSize / 2 - config.symbolWidth / 2,
                y = width - config.borderSize + config.symbolPadding,
                text = symbol.toString(),
                color = config.color,
                font = config.font
            )
        }
        return numberBorder + symbolBorder
    }

    class Config(
        val borderSize: Int,
        val symbolPadding: Int,
        val symbolHeight: Int,
        val symbolWidth: Int,
        val font: Font,
        val color: Color,
        val backgroundColor: Color,
        val symbolsVertical: List<Char>,
        val symbolsHorizontal: List<Char>
    )
}