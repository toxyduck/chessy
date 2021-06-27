package io.chessy.tool.view

import java.awt.Color
import java.awt.Font
import java.awt.Graphics
import kotlin.math.max

class PlayerDetailsView(
    override val x: Int,
    override val y: Int,
    override val width: Int,
    private val playerName: String,
    private val playerRating: Int,
    private val graphicsContext: Graphics
) : ViewGroup<Nothing>() {

    override var height: Int = 0
    private set

    init {
        initViews()
    }

    override fun draw(graphics: Graphics) {
        graphics.color = backgroundColor
        graphics.fillRect(x, y, width, height)
        super.draw(graphics)
    }

    override fun copy(x: Int, y: Int, width: Int, height: Int): View {
        return PlayerDetailsView(x, y, width, playerName, playerRating, graphicsContext)
    }

    private fun initViews() {
        val avatarView = AvatarView(
            0,
            0,
            96
        )
        val nameTextView = TextView(
            x = 0,
            y = 0,
            graphicsContext = graphicsContext,
            text = playerName,
            color = textColor,
            font = FONT_FOR_NAME
        )
        val ratingTextView = TextView(
            x = 0,
            y = 0,
            graphicsContext = graphicsContext,
            text = playerRating.toString(),
            color = textColor,
            font = FONT_FOR_RATING
        )
        val ratingWithBackground = RoundBackgroundView(ratingBackgroundColor, ratingTextView, 50, 24, 16)
        height = 2 * PADDING_VERTICAL + max(avatarView.height, max(nameTextView.height, ratingWithBackground.height))
        val centerY = height / 2
        val avatarViewX = x + PADDING_HORIZONTAL
        val nameViewX = avatarViewX + avatarView.width + PADDING_HORIZONTAL
        val ratingViewX = nameViewX + nameTextView.width + PADDING_HORIZONTAL
        addChild(avatarView.move(avatarViewX, centerY - avatarView.height / 2))
        addChild(nameTextView.move(nameViewX, centerY - nameTextView.height / 2))
        addChild(ratingWithBackground.move(ratingViewX, centerY - ratingWithBackground.height / 2))
    }

    companion object {
        private const val FONT_NAME = "SansSerif"
        private val FONT_FOR_NAME = Font(FONT_NAME, Font.PLAIN, 32)
        private val FONT_FOR_RATING = Font(FONT_NAME, Font.PLAIN, 24)
        private const val PADDING_VERTICAL = 48
        private const val PADDING_HORIZONTAL = 32
        private val textColor = Color.decode("#FFFFFF")
        private val backgroundColor = Color.decode("#4D4D4D")
        private val ratingBackgroundColor = Color.decode("#000000")
    }
}