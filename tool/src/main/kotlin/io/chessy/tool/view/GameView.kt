package io.chessy.tool.view

import io.chessy.tool.Board
import io.chessy.tool.Move
import io.chessy.tool.animator.Animator
import io.chessy.tool.animator.MoveAnimator
import io.chessy.tool.interpolator.LinearInterpolator
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

    fun isMoveFinished(): Boolean {
        return animatedViews?.fold(true) { acc, animator -> acc && animator.isFinish() } ?: true
    }

    fun doMove(currentBoard: Board, move: Move) {
        when {
            move.isCastleMove -> {
            }
            move.isMateMove -> {
            }
            move.isKingAttacked -> {
            }
            else -> {
                val staticPieces = currentBoard.filter(listOf(move.from))
                staticPiecesView = OnceDrawView(StaticPiecesView(x, y, width, height, staticPieces))
                val piecesView = PieceView(
                    x + move.from.x * cellSize,
                    y + move.from.y * cellSize,
                    cellSize,
                    cellSize,
                    move.piece
                )
                val animator = MoveAnimator(
                    LinearInterpolator,
                    piecesView,
                    MOVE_DURATION,
                    Point(x + move.to.x * cellSize, y + move.to.y * cellSize)
                )
                animatedViews = listOf(animator)
            }
        }
    }

    override fun draw(graphics: Graphics) {
        boardView.draw(graphics)
        staticPiecesView.draw(graphics)
        animatedViews?.forEach { it.draw(graphics) }
    }

    companion object {
        private const val MOVE_DURATION = 500 / 16
        private val blackColor = Color.decode("#A57550")
        private val whiteColor = Color.decode("#EBD0A6")
        private val redColor = Color.decode("#FF0000")
    }
}