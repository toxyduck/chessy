package io.chessy.tool.view

import io.chessy.tool.Board
import kotlin.math.min

class StaticPiecesView(
    override val x: Int,
    override val y: Int,
    override val width: Int,
    override val height: Int,
    private val piecePadding: Int,
    private val board: Board
) : ViewGroup<Nothing>() {

    private val cellSize: Int = min(width, height) / 8

    init {
        views().forEach { addChild(it) }
    }

    override fun copy(x: Int, y: Int, width: Int, height: Int): View {
        return StaticPiecesView(x, y, width, height, piecePadding ,board)
    }

    private fun views(): List<View> {
        return board.cells
            .filter { it.piece != null }
            .map {
                PaddingView(
                    PieceView(
                        it.cell.x * cellSize + x,
                        it.cell.y * cellSize + y,
                        cellSize,
                        cellSize,
                        it.piece!!
                    ),
                    piecePadding
                )
            }
    }
}