package io.chessy.tool.view

import io.chessy.tool.*
import io.chessy.tool.animator.MoveAnimator
import io.chessy.tool.interpolator.LinearInterpolator
import io.chessy.tool.primitive.Point
import java.awt.Color
import java.awt.Font
import java.awt.Graphics
import java.lang.Math.max
import kotlin.math.min

class RootView(
    override val x: Int,
    override val y: Int,
    override val width: Int,
    override val height: Int,
    private val graphicsContext: Graphics,
    private val board: Board,
) : ViewGroup<GameView.GameViewAction>() {

    private val maxSymbolWidth = CHESS_SYMBOLS.maxOf { SymbolView.measure(graphicsContext, FONT, it).width }
    private val maxSymbolHeight = CHESS_SYMBOLS.maxOf { SymbolView.measure(graphicsContext, FONT, it).height }
    private val padding = maxSymbolHeight.coerceAtLeast(maxSymbolWidth) + 2 * GAME_VIEW_PADDING

    private val boardSize = width.coerceAtMost(height)
    private val centerBoardX = width / 2
    private val centerBoardY = height / 2
    private val topLeftBoardX = centerBoardX - boardSize / 2
    private val topLeftBoardY = centerBoardY - boardSize / 2

    private val gameView: ViewGroup<GameView.GameViewAction> =
        GameView(
            topLeftBoardX + padding,
            topLeftBoardY + padding,
            boardSize - padding * 2,
            boardSize - padding * 2,
            board
        )

    init {
        addChild(OnceDrawView(BorderView(topLeftBoardX, topLeftBoardY, boardSize, boardSize, padding, GAME_VIEW_PADDING, maxSymbolHeight, maxSymbolWidth)))
        addChild(gameView)
    }

    override fun obtainAction(action: GameView.GameViewAction) {
        gameView.produceAction(action)
    }

    override fun copy(x: Int, y: Int, width: Int, height: Int): View {
        return RootView(x, y, width, height, graphicsContext, board)
    }

    companion object {

        private const val FONT_NAME = "SansSerif"
        private const val FONT_SIZE = 32
        private const val FONT_STYLE = Font.PLAIN
        private val FONT = Font(FONT_NAME, FONT_STYLE, FONT_SIZE)

        private val CHESS_SYMBOLS = ('1' until '9').toList() + ('A' until 'I').toList()
        private const val GAME_VIEW_PADDING = 8
    }
}