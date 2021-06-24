package io.chessy.tool.view

import io.chessy.tool.*
import io.chessy.tool.animator.MoveAnimator
import io.chessy.tool.interpolator.LinearInterpolator
import io.chessy.tool.primitive.Point
import java.awt.Color
import kotlin.math.min

class RootView(
    override val x: Int,
    override val y: Int,
    override val width: Int,
    override val height: Int,
    private val board: Board
) : ViewGroup<GameView.GameViewAction>() {

    private val boardSize = width.coerceAtMost(height)
    private val centerBoardX = width / 2
    private val centerBoardY = height / 2
    private val topLeftBoardX = centerBoardX - boardSize / 2
    private val topLeftBoardY = centerBoardY - boardSize / 2

    private val gameView: ViewGroup<GameView.GameViewAction> =
        GameView(
            topLeftBoardX + GAME_VIEW_PADDING,
            topLeftBoardY + GAME_VIEW_PADDING,
            boardSize - GAME_VIEW_PADDING * 2,
            boardSize - GAME_VIEW_PADDING * 2,
            board
        )

    init {
        addChild(OnceDrawView(BorderView(topLeftBoardX, topLeftBoardY, boardSize, boardSize, GAME_VIEW_PADDING)))
        addChild(gameView)
    }

    override fun obtainAction(action: GameView.GameViewAction) {
        gameView.produceAction(action)
    }

    override fun copy(x: Int, y: Int, width: Int, height: Int): View {
        return RootView(x, y, width, height, board)
    }

    companion object {
        private const val GAME_VIEW_PADDING = 48
    }
}