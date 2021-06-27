package io.chessy.tool.view

import io.chessy.tool.chess.Board
import java.awt.Color
import java.awt.Font
import java.awt.Graphics

class RootView(
    override val x: Int,
    override val y: Int,
    override val width: Int,
    override val height: Int,
    private val graphicsContext: Graphics,
    private val board: Board,
) : ViewGroup<GameView.GameViewAction>() {

    private val maxSymbolWidth = CHESS_SYMBOLS.maxOf { TextView.measure(graphicsContext, FONT, it.toString()).width }
    private val maxSymbolHeight = CHESS_SYMBOLS.maxOf { TextView.measure(graphicsContext, FONT, it.toString()).height }
    private val borderSize = maxSymbolHeight.coerceAtLeast(maxSymbolWidth) + 2 * GAME_VIEW_PADDING

    private val boardSize = width.coerceAtMost(height)
    private val centerBoardX = width / 2
    private val centerBoardY = height / 2
    private val topLeftBoardX = centerBoardX - boardSize / 2
    private val topLeftBoardY = centerBoardY - boardSize / 2

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

    private val gameView: ViewGroup<GameView.GameViewAction> =
        GameView(
            topLeftBoardX + borderSize,
            topLeftBoardY + borderSize,
            boardSize - borderSize * 2,
            boardSize - borderSize * 2,
            board
        )

    init {
        val playerDetailView = PlayerDetailsView(x, y, width, "Ян Непомнящий", 2891, graphicsContext)
        addChild(OnceDrawView(playerDetailView))
        addChild(OnceDrawView(BorderView(topLeftBoardX, topLeftBoardY, boardSize, boardSize, graphicsContext, borderViewConfig)))
        addChild(gameView)
    }

    override fun obtainAction(action: GameView.GameViewAction) {
        gameView.produceAction(action)
    }

    override fun copy(x: Int, y: Int, width: Int, height: Int): View {
        return RootView(x, y, width, height, graphicsContext, board)
    }

    override fun draw(graphics: Graphics) {
        graphics.color = backgroundColor
        graphics.fillRect(x, y, width, height)
        super.draw(graphics)
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
        private val backgroundColor = Color.decode("#000000")
    }
}