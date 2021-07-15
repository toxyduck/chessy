package io.chessy.tool.view

import io.chessy.tool.animator.MoveAnimator
import io.chessy.tool.chess.Board
import io.chessy.tool.chess.Move
import io.chessy.tool.chess.opposite
import io.chessy.tool.interpolator.EaseInSineInterpolator
import io.chessy.tool.primitive.Point
import java.awt.Color
import kotlin.math.min

class GameView(
    override val x: Int,
    override val y: Int,
    override val width: Int,
    override val height: Int,
    private val board: Board,
    private val config: Config
) : ViewGroup<GameView.GameViewAction>() {

    class GameViewAction(val currentBoard: Board, val move: Move)

    private val cellSize: Int = min(width, height) / 8

    init {
        addChild(OnceDrawView(BoardView(x, y, width, height, config.whiteColor, config.blackColor)))
        addChildOneAction(OnceDrawView(StaticPiecesView(x, y, width, height, config.piecePadding, board)))
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
                        config.specialColor
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
        return GameView(x, y, width, height, board, config)
    }

    private fun move(currentBoard: Board, move: Move) {
        val staticPieces = currentBoard.filter(listOf(move.from))
        addChildOneAction(OnceDrawView(StaticPiecesView(x, y, width, height, config.piecePadding, staticPieces)))
        val pieceView = PieceView(
            x + move.from.x * cellSize,
            y + move.from.y * cellSize,
            cellSize,
            cellSize,
            move.piece
        )
        val animator = MoveAnimator(
            interpolator,
            PaddingView(pieceView, config.piecePadding),
            config.moveDuration,
            Point(x + move.to.x * cellSize, y + move.to.y * cellSize)
        )
        addChildOneAction(animator)
    }

    private fun castleMove(currentBoard: Board, move: Move) {
        move.rookCastleMove()?.let { rookMove ->
            val staticPieces = currentBoard.filter(listOf(move.from, rookMove.from))
            addChildOneAction(OnceDrawView(StaticPiecesView(x, y, width, height, config.piecePadding, staticPieces)))
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
                interpolator,
                PaddingView(rookView, config.piecePadding),
                config.moveDuration,
                Point(x + rookMove.to.x * cellSize, y + rookMove.to.y * cellSize)
            )
            val kingAnimator = MoveAnimator(
                interpolator,
                PaddingView(kingView, config.piecePadding),
                config.moveDuration,
                Point(x + move.to.x * cellSize, y + move.to.y * cellSize)
            )
            addChildesOneAction(kingAnimator, rookAnimator)
        }
    }

    class Config(
        val piecePadding: Int,
        val moveDuration: Int,
        val blackColor: Color,
        val whiteColor: Color,
        val specialColor: Color
    )

    companion object {
        private val interpolator = EaseInSineInterpolator
    }
}