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

        val gameView: ViewGroup<RootView.RootViewAction> = RootView(0, 0, width, height, graphicsContext, startBoard)
        var currentBoard = startBoard
        println("Render pause")
        gameView.produceAction(RootView.RootViewAction.Pause(PAUSE_DURATION))
        gameView.renderAction()
        moves
            .take(10)
            .forEach { move ->
                println("Rendered move $move}")
                gameView.produceAction(RootView.RootViewAction.GameViewMove(GameView.GameViewAction(currentBoard, move)))
                gameView.renderAction()
                currentBoard = currentBoard.mutate(move)
            }
        println("Render pause")
        gameView.produceAction(RootView.RootViewAction.Pause(PAUSE_DURATION))
        gameView.renderAction()
    }

    private fun ViewGroup<*>.renderAction() {
        while (!isFinish()) {
            // important to use this pixel format
            val frame = BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR)
            draw(frame.graphics)
            frameListener(frame)
        }
    }

    companion object {
        private const val PAUSE_DURATION = 1000 / 16
    }
}