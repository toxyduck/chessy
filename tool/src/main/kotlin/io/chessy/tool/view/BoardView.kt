package io.chessy.tool.view

import java.awt.Color
import kotlin.math.min

class BoardView(
    override val x: Int,
    override val y: Int,
    override val width: Int,
    override val height: Int,
    private val colorWhite: Color,
    private val colorBlack: Color
) : ViewGroup<Nothing>() {

    private val cellSize: Int = min(width, height) / 8

    init {
        views().forEach { addChild(it) }
    }

    override fun copy(x: Int, y: Int, width: Int, height: Int): View {
        return BoardView(x, y, width, height, colorWhite, colorBlack)
    }

    private fun views(): List<CellView> {
        return (0 until 64).map { ix ->
            val ixX = ix % 8
            val ixY = ix / 8
            val isWhite = isCellWhite(ixX, ixY)
            val cellX = x + cellSize * ixX
            val cellY = y + cellSize * ixY
            CellView(
                x = cellX,
                y = cellY,
                width = cellSize,
                height = cellSize,
                color = if (isWhite) colorWhite else colorBlack
            )
        }
    }

    private fun isCellWhite(x: Int, y: Int): Boolean {
        return ((x + y) % 2) == 0
    }

}