package io.chessy.tool.view

import io.chessy.tool.Board
import java.awt.Graphics
import kotlin.math.min

class StaticPiecesView(
    override val x: Int,
    override val y: Int,
    override val width: Int,
    override val height: Int,
    private val board: Board
) : View {

    private val cellSize: Int = min(width, height) / 8

    private val pieceViews by lazy { views() }

    override fun draw(graphics: Graphics) {
        pieceViews.forEach { it.draw(graphics) }
    }

    override fun copy(x: Int, y: Int, width: Int, height: Int): View {
        return StaticPiecesView(x, y, width, height, board)
    }

    private fun views(): List<PieceView> {
        return board.cells
            .filter { it.piece != null }
            .map {
                PieceView(
                    it.cell.x * cellSize + x,
                    it.cell.y * cellSize + y,
                    cellSize,
                    cellSize,
                    it.piece!!
                )
            }
    }
}