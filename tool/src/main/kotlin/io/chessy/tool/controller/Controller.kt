package io.chessy.tool.controller

import io.chessy.tool.Board
import io.chessy.tool.Move
import io.chessy.tool.view.GameView
import java.awt.image.BufferedImage

class Controller(
    private val startBoard: Board,
    private val moves: List<Move>,
    private val width: Int,
    private val height: Int,
    private val fps: Int,
    private val frameListener: ((BufferedImage) -> Unit)
) {
    private val boardSize = width.coerceAtMost(height)
    private val centerBoardX = width / 2
    private val centerBoardY = height / 2
    private val topLeftBoardX = centerBoardX - boardSize / 2
    private val topLeftBoardY = centerBoardY - boardSize / 2

    fun startRender() {
        val gameView = GameView(topLeftBoardX, topLeftBoardY, boardSize, boardSize, startBoard)
        var currentBoard = startBoard
        moves.forEach { move ->
            println("Rendered move ${moves.joinToString()}}")
            gameView.doMove(currentBoard, move)
            while (!gameView.isMoveFinished()) {
                val frame = BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR)
                gameView.draw(frame.graphics)
                frameListener(frame)
            }
            currentBoard = currentBoard.mutate(move)
        }
    }
}