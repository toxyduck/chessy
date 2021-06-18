package io.chessy.tool.view

import io.chessy.tool.Board
import io.chessy.tool.Move
import io.chessy.tool.animator.Animator
import io.chessy.tool.animator.MoveAnimator
import io.chessy.tool.interpolator.LinearInterpolator
import io.chessy.tool.opposite
import io.chessy.tool.primitive.Point
import java.awt.Color
import java.awt.Graphics
import kotlin.math.min

class GameView(
    override val x: Int,
    override val y: Int,
    override val width: Int,
    override val height: Int,
    board: Board
) : View {

    private val cellSize: Int = min(width, height) / 8

    private val boardView = OnceDrawView(BoardView(x, y, width, height, whiteColor, blackColor))
    private var staticPiecesView = OnceDrawView(StaticPiecesView(x, y, width, height, board))
    private var animatedViews: List<Animator<*>>? = null
    private var mateCellViews: OnceDrawView? = null

    fun isMoveFinished(): Boolean {
        return animatedViews?.fold(true) { acc, animator -> acc && animator.isFinish() } ?: true
    }

    fun doMove(currentBoard: Board, move: Move) {
        when {
            move.isCastleMove -> {
                castleMove(currentBoard, move)
            }
            else -> {
                move(currentBoard, move)
            }
        }
        mateCellViews = if (move.isKingAttacked) {
            val kingCell = currentBoard.kingCell(move.side.opposite())
            OnceDrawView(CellView(x + kingCell.x * cellSize, y + kingCell.y * cellSize, cellSize, cellSize, redColor))
        } else {
            null
        }
    }

    override fun draw(graphics: Graphics) {
        boardView.draw(graphics)
        mateCellViews?.draw(graphics)
        staticPiecesView.draw(graphics)
        animatedViews?.forEach { it.draw(graphics) }

    }

    private fun move(currentBoard: Board, move: Move) {
        val staticPieces = currentBoard.filter(listOf(move.from))
        staticPiecesView = OnceDrawView(StaticPiecesView(x, y, width, height, staticPieces))
        val pieceView = PieceView(
            x + move.from.x * cellSize,
            y + move.from.y * cellSize,
            cellSize,
            cellSize,
            move.piece
        )
        val animator = MoveAnimator(
            LinearInterpolator,
            pieceView,
            MOVE_DURATION,
            Point(x + move.to.x * cellSize, y + move.to.y * cellSize)
        )
        animatedViews = listOf(animator)
    }

    private fun castleMove(currentBoard: Board, move: Move) {
        move.rookCastleMove()?.let { rookMove ->
            val staticPieces = currentBoard.filter(listOf(move.from, rookMove.from))
            staticPiecesView = OnceDrawView(StaticPiecesView(x, y, width, height, staticPieces))
            val kingView = PieceView(
                x + move.from.x * cellSize,
                y + move.from.y * cellSize,
                cellSize,
                cellSize,
                move.piece
            )
            val rookView = PieceView(
                x + move.from.x * cellSize,
                y + move.from.y * cellSize,
                cellSize,
                cellSize,
                move.piece
            )
            val rookAnimator = MoveAnimator(
                LinearInterpolator,
                rookView,
                MOVE_DURATION,
                Point(x + move.to.x * cellSize, y + move.to.y * cellSize)
            )
            val kingAnimator = MoveAnimator(
                LinearInterpolator,
                kingView,
                MOVE_DURATION,
                Point(x + move.to.x * cellSize, y + move.to.y * cellSize)
            )
            animatedViews = listOf(rookAnimator, kingAnimator)
        }
    }

    companion object {
        private const val MOVE_DURATION = 500 / 16
        private val blackColor = Color.decode("#B27B41")
        private val whiteColor = Color.decode("#DEC496")
        private val redColor = Color.decode("#B24341")
    }
}