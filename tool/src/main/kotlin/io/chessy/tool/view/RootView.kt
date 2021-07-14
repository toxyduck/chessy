package io.chessy.tool.view

import io.chessy.tool.chess.Board
import java.awt.Color
import java.awt.Graphics

class RootView(
    override val x: Int,
    override val y: Int,
    override val width: Int,
    override val height: Int,
    private val graphicsContext: Graphics,
    private val board: Board,
    private val config: Config
) : ViewGroup<RootView.RootViewAction>() {

    private val gameView: ViewGroup<GameView.GameViewAction>
    private val blackDetailView: PlayerDetailsView
    private val whiteDetailView: PlayerDetailsView = PlayerDetailsView(
        width = width,
        playerName = "Гарри Каспаров",
        playerRating = 2812,
        playerIconName = "kasparov.jpg",
        inverted = true,
        config = config.playerDetailsViewConfig,
        graphicsContext = graphicsContext
    ).moveWithSizeCast { _, viewHeight -> x to height - viewHeight - config.bottomPadding }

    init {
        gameView = BorderedGameView(
            x,
            whiteDetailView.y - width,
            width,
            graphicsContext, board, config.borderedGameViewConfig
        )
        blackDetailView = PlayerDetailsView(
            x = x,
            y = gameView.y - whiteDetailView.height,
            width = width,
            playerName =  "Веселин Топалов",
            playerRating = 2700,
            playerIconName = "topalov.jpg",
            inverted = false,
            config = config.playerDetailsViewConfig,
            graphicsContext = graphicsContext
        )
        val eventView = EventDetailsView(
            x = x,
            width = width,
            event = "Турнир Вейк-ан-Зее",
            tournament = "Группа A",
            date = "16 января 1999 года",
            config = config.eventDetailsViewConfig,
            graphicsContext = graphicsContext
        ).moveWithSize { _, height ->
            x to blackDetailView.y - height
        }
        addChild(whiteDetailView)
        addChild(blackDetailView)
        addChild(gameView)
        addChild(OnceDrawView(eventView))
    }

    override fun obtainAction(action: RootViewAction) {
        when(action) {
            is RootViewAction.GameViewMove -> gameView.produceAction(action.move)
            is RootViewAction.Pause -> finishActionAfterFrames(action.framesCount)
            is RootViewAction.ShowWinner -> showWinner(action.result)
        }
    }

    override fun copy(x: Int, y: Int, width: Int, height: Int): View {
        return RootView(x, y, width, height, graphicsContext, board, config)
    }

    override fun draw(graphics: Graphics) {
        graphics.color = config.backgroundColor
        graphics.fillRect(x, y, width, height)
        super.draw(graphics)
    }

    private fun showWinner(gameResult: GameResult) {
        when(gameResult) {
            GameResult.BLACK_WIN -> blackDetailView.produceAction(io.chessy.tool.view.GameResult.WIN)
            GameResult.WHITE_WIN -> whiteDetailView.produceAction(io.chessy.tool.view.GameResult.WIN)
            GameResult.DRAW -> {
                blackDetailView.produceAction(io.chessy.tool.view.GameResult.DRAW)
                whiteDetailView.produceAction(io.chessy.tool.view.GameResult.DRAW)
            }
        }
    }

    class Config(
        val backgroundColor: Color,
        val bottomPadding: Int,
        val eventDetailsViewConfig: EventDetailsView.Config,
        val playerDetailsViewConfig: PlayerDetailsView.Config,
        val borderedGameViewConfig: BorderedGameView.Config
    )

    sealed class RootViewAction {
        class GameViewMove(val move: GameView.GameViewAction) : RootViewAction()
        class Pause(val framesCount: Int) : RootViewAction()
        class ShowWinner(val result: GameResult) : RootViewAction()
    }

    enum class GameResult {
        BLACK_WIN,
        WHITE_WIN,
        DRAW
    }
}