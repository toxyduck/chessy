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
) : ViewGroup<RootView.RootViewAction>() {

    private val gameView: ViewGroup<GameView.GameViewAction>

    init {
        val playerDetailView2 = PlayerDetailsView(
            width = width,
            playerName = "Гарри Каспаров",
            playerRating = 2812,
            playerIconName = "kasparov.jpg",
            inverted = true,
            graphicsContext = graphicsContext
        ).moveWithSize { _, viewHeight -> x to height - viewHeight - BOTTOM_PADDING }
        gameView = BorderedGameView(
            x,
            playerDetailView2.y - width,
            width,
            graphicsContext, board
        )
        val playerDetailView = PlayerDetailsView(
            x = x,
            y = gameView.y - playerDetailView2.height,
            width = width,
            playerName =  "Веселин Топалов",
            playerRating = 2700,
            playerIconName = "topalov.jpg",
            inverted = false,
            graphicsContext = graphicsContext
        )
        val eventView = EventDetailsView(
            x = x,
            width = width,
            event = "Турнир Вейк-ан-Зее",
            tournament = "Группа A",
            date = "16 января 1999 года",
            graphicsContext = graphicsContext
        ).moveWithSize { _, height ->
            x to playerDetailView.y - height
        }
        addChild(OnceDrawView(playerDetailView2))
        addChild(OnceDrawView(playerDetailView))
        addChild(gameView)
        addChild(OnceDrawView(eventView))
    }

    override fun obtainAction(action: RootViewAction) {
        when(action) {
            is RootViewAction.GameViewMove -> gameView.produceAction(action.move)
            is RootViewAction.Pause -> finishActionAfterFrames(action.framesCount)
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

    companion object {
        private val backgroundColor = Color.decode("#212121")
        private const val BOTTOM_PADDING = 148
    }

    sealed class RootViewAction {
        class GameViewMove(val move: GameView.GameViewAction) : RootViewAction()
        class Pause(val framesCount: Int) : RootViewAction()
    }
}