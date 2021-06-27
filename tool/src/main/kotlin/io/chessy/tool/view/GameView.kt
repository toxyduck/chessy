package io.chessy.tool.view

import io.chessy.tool.animator.MoveAnimator
import io.chessy.tool.chess.Board
import io.chessy.tool.chess.Move
import io.chessy.tool.chess.opposite
import io.chessy.tool.interpolator.LinearInterpolator
import io.chessy.tool.primitive.Point
import java.awt.Color
import kotlin.math.min

class GameView(
    override val x: Int,
    override val y: Int,
    override val width: Int,
    override val height: Int,
    private val board: Board
) : ViewGroup<GameView.GameViewAction>() {

    class GameViewAction(val currentBoard: Board, val move: Move)

    private val cellSize: Int = min(width, height) / 8

    init {
        addChild(OnceDrawView(BoardView(x, y, width, height, whiteColor, blackColor)))
    }

    override fun obtainAction(action: GameViewAction) {
        val move = action.move
        val currentBoard = action.currentBoard
        if (move.isKingAttacked) {
            val kingCell = currentBoard.kingCell(move.side.opposite())
            addChildOneAction(
                OnceDrawView(
                    CellView(
                        x + kingCell.x * cellSize,
                        y + kingCell.y * cellSize,
                        cellSize,
                        cellSize,
                        redColor
                    )
                )
            )
        }
        when {
            move.isCastleMove -> {
                castleMove(currentBoard, move)
            }
            else -> {
                move(currentBoard, move)
            }
        }
    }

    override fun copy(x: Int, y: Int, width: Int, height: Int): View {
        return GameView(x, y, width, height, board)
    }

    private fun move(currentBoard: Board, move: Move) {
        val staticPieces = currentBoard.filter(listOf(move.from))
        addChildOneAction(OnceDrawView(StaticPiecesView(x, y, width, height, PIECE_PADDING, staticPieces)))
        val pieceView = PieceView(
            x + move.from.x * cellSize,
            y + move.from.y * cellSize,
            cellSize,
            cellSize,
            move.piece
        )
        val animator = MoveAnimator(
            LinearInterpolator,
            PaddingView(pieceView, PIECE_PADDING),
            MOVE_DURATION,
            Point(x + move.to.x * cellSize, y + move.to.y * cellSize)
        )
        addChildOneAction(animator)
    }

    private fun castleMove(currentBoard: Board, move: Move) {
        move.rookCastleMove()?.let { rookMove ->
            val staticPieces = currentBoard.filter(listOf(move.from, rookMove.from))
            addChildOneAction(OnceDrawView(StaticPiecesView(x, y, width, height, PIECE_PADDING, staticPieces)))
            val kingView = PieceView(
                x + move.from.x * cellSize,
                y + move.from.y * cellSize,
                cellSize,
                cellSize,
                move.piece
            )
            val rookView = PieceView(
                x + rookMove.from.x * cellSize,
                y + rookMove.from.y * cellSize,
                cellSize,
                cellSize,
                rookMove.piece
            )
            val rookAnimator = MoveAnimator(
                LinearInterpolator,
                PaddingView(rookView, PIECE_PADDING),
                MOVE_DURATION,
                Point(x + rookMove.to.x * cellSize, y + rookMove.to.y * cellSize)
            )
            val kingAnimator = MoveAnimator(
                LinearInterpolator,
                PaddingView(kingView, PIECE_PADDING),
                MOVE_DURATION,
                Point(x + move.to.x * cellSize, y + move.to.y * cellSize)
            )
            addChildesOneAction(kingAnimator, rookAnimator)
        }
    }

    companion object {
        private const val PIECE_PADDING = 24
        private const val MOVE_DURATION = 500 / 16
        private val blackColor = Color.decode("#B27B41")
        private val whiteColor = Color.decode("#DEC496")
        private val redColor = Color.decode("#B24341")
    }
}