package io.chessy.tool.controller

import io.chessy.tool.FontsLoader
import io.chessy.tool.chess.Board
import io.chessy.tool.chess.Move
import io.chessy.tool.view.GameView
import io.chessy.tool.view.RootView
import io.chessy.tool.view.ViewGroup
import java.awt.image.BufferedImage

class Controller(
    private val startBoard: Board,
    private val moves: List<Move>,
    private val width: Int,
    private val height: Int,
    private val fps: Int,
    private val frameListener: ((BufferedImage) -> Unit)
) {
    fun startRender() {
        FontsLoader.load()
        val graphicsContext = BufferedImage(1, 1, BufferedImage.TYPE_3BYTE_BGR).graphics

        val gameView: ViewGroup<GameView.GameViewAction> = RootView(0, 0, width, height, graphicsContext, startBoard)
        var currentBoard = startBoard
        moves
            .forEach { move ->
                println("Rendered move $move}")
                gameView.produceAction(GameView.GameViewAction(currentBoard, move))
                while (!gameView.isFinish()) {
                    // important to use this pixel format
                    val frame = BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR)
                    gameView.draw(frame.graphics)
                    frameListener(frame)
                }
                currentBoard = currentBoard.mutate(move)
            }
    }
}