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
) : ViewGroup<RootView.RootViewAction>() {

    val eventDetailsViewConfig = EventDetailsView.Config(
        fontEvent = Font("Gilroy-Bold", Font.PLAIN, 32),
        fontTournament = Font("Gilroy-Medium", Font.PLAIN, 32),
        fontDate = Font("Gilroy-Regular", Font.PLAIN, 32),
        textColor = Color.decode("#FFFFFF"),
        paddingVertical = 32,
        paddingHorizontal = 32
    )

    private val gameView: ViewGroup<GameView.GameViewAction>
    private val blackDetailView: PlayerDetailsView
    private val whiteDetailView: PlayerDetailsView = PlayerDetailsView(
        width = width,
        playerName = "Гарри Каспаров",
        playerRating = 2812,
        playerIconName = "kasparov.jpg",
        inverted = true,
        backgroundColor = detailsViewBackgroundColor,
        graphicsContext = graphicsContext
    ).moveWithSizeCast { _, viewHeight -> x to height - viewHeight - BOTTOM_PADDING }

    init {
        gameView = BorderedGameView(
            x,
            whiteDetailView.y - width,
            width,
            graphicsContext, board
        )
        blackDetailView = PlayerDetailsView(
            x = x,
            y = gameView.y - whiteDetailView.height,
            width = width,
            playerName =  "Веселин Топалов",
            playerRating = 2700,
            playerIconName = "topalov.jpg",
            inverted = false,
            backgroundColor = detailsViewBackgroundColor,
            graphicsContext = graphicsContext
        )
        val eventView = EventDetailsView(
            x = x,
            width = width,
            event = "Турнир Вейк-ан-Зее",
            tournament = "Группа A",
            date = "16 января 1999 года",
            config = eventDetailsViewConfig,
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
        return RootView(x, y, width, height, graphicsContext, board)
    }

    override fun draw(graphics: Graphics) {
        graphics.color = backgroundColor
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

    companion object {
        private val backgroundColor = Color.decode("#212121")
        private const val BOTTOM_PADDING = 148
        private val detailsViewBackgroundColor = Color.decode("#4D4D4D")
    }

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