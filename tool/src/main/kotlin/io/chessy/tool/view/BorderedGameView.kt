package io.chessy.tool.view

import io.chessy.tool.chess.Board
import java.awt.Color
import java.awt.Font
import java.awt.Graphics

class BorderedGameView(
    override val x: Int,
    override val y: Int,
    override val width: Int,
    private val graphicsContext: Graphics,
    private val board: Board
) : ViewGroup<GameView.GameViewAction>() {

    private val maxSymbolWidth = CHESS_SYMBOLS.maxOf { TextView.measure(graphicsContext, FONT, it.toString()).width }
    private val maxSymbolHeight = CHESS_SYMBOLS.maxOf { TextView.measure(graphicsContext, FONT, it.toString()).height }
    private val borderSize = maxSymbolHeight.coerceAtLeast(maxSymbolWidth) + 2 * GAME_VIEW_PADDING

    override val height: Int
        get() = width

    private val boardSize = width

    private val borderViewConfig = BorderView.Config(
        borderSize = borderSize,
        symbolPadding = GAME_VIEW_PADDING,
        symbolHeight = maxSymbolHeight,
        symbolWidth = maxSymbolWidth,
        font = FONT,
        color = whiteColor,
        backgroundColor = grayColor,
        symbolsVertical = SYMBOLS_VERTICAL,
        symbolsHorizontal = SYMBOLS_HORIZONTAL
    )

    private val gameView: ViewGroup<GameView.GameViewAction>

    init {
        gameView =
            GameView(
                x + borderSize,
                y + borderSize,
                boardSize - borderSize * 2,
                boardSize - borderSize * 2,
                board
            )
        val border = BorderView(x, gameView.y - borderSize, boardSize, boardSize, graphicsContext, borderViewConfig)
        addChild(OnceDrawView(border))
        addChild(gameView)
    }

    override fun obtainAction(action: GameView.GameViewAction) {
        gameView.produceAction(action)
    }

    override fun copy(x: Int, y: Int, width: Int, height: Int): View {
        return BorderedGameView(x, y, width, graphicsContext, board)
    }

    companion object {
        private const val FONT_NAME = "SansSerif"
        private const val FONT_SIZE = 32
        private const val FONT_STYLE = Font.PLAIN
        private val FONT = Font(FONT_NAME, FONT_STYLE, FONT_SIZE)

        private val SYMBOLS_VERTICAL = ('1' until '9').toList()
        private val SYMBOLS_HORIZONTAL = ('A' until 'I').toList()

        private val CHESS_SYMBOLS = SYMBOLS_VERTICAL + SYMBOLS_HORIZONTAL
        private const val GAME_VIEW_PADDING = 8

        private val whiteColor = Color(0x88FFFFFF.toInt(), true)
        private val grayColor = Color.decode("#272522")
    }
}