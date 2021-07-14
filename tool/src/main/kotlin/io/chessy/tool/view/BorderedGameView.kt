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
    private val board: Board,
    private val config: Config
) : ViewGroup<GameView.GameViewAction>() {

    private val chessSymbols = config.symbolsVertical + config.symbolsHorizontal

    private val maxSymbolWidth = chessSymbols.maxOf { TextView.measure(graphicsContext, config.borderFont, it.toString()).width }
    private val maxSymbolHeight = chessSymbols.maxOf { TextView.measure(graphicsContext, config.borderFont, it.toString()).height }
    private val borderSize = maxSymbolHeight.coerceAtLeast(maxSymbolWidth) + 2 * config.gameViewPadding

    override val height: Int
        get() = width

    private val boardSize = width

    private val borderViewConfig = BorderView.Config(
        borderSize = borderSize,
        symbolPadding = config.gameViewPadding,
        symbolHeight = maxSymbolHeight,
        symbolWidth = maxSymbolWidth,
        font = config.borderFont,
        color = config.symbolsColor,
        backgroundColor = config.borderColor,
        symbolsVertical = config.symbolsVertical,
        symbolsHorizontal = config.symbolsHorizontal
    )

    private val gameView: ViewGroup<GameView.GameViewAction>

    init {
        gameView =
            GameView(
                x + borderSize,
                y + borderSize,
                boardSize - borderSize * 2,
                boardSize - borderSize * 2,
                board,
                config.gameViewConfig
            )
        val border = BorderView(x, gameView.y - borderSize, boardSize, boardSize, graphicsContext, borderViewConfig)
        addChild(OnceDrawView(border))
        addChild(gameView)
    }

    override fun obtainAction(action: GameView.GameViewAction) {
        gameView.produceAction(action)
    }

    override fun copy(x: Int, y: Int, width: Int, height: Int): View {
        return BorderedGameView(x, y, width, graphicsContext, board, config)
    }

    class Config(
        val borderFont: Font,
        val symbolsVertical: List<Char>,
        val symbolsHorizontal: List<Char>,
        val gameViewPadding: Int,
        val symbolsColor: Color,
        val borderColor: Color,
        val gameViewConfig: GameView.Config
    )
}