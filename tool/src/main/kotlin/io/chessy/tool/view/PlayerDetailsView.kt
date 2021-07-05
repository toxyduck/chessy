package io.chessy.tool.view

import java.awt.Color
import java.awt.Font
import java.awt.Graphics
import kotlin.math.max

class PlayerDetailsView(
    override val x: Int = 0,
    override val y: Int = 0,
    override val width: Int,
    private val playerName: String,
    private val playerRating: Int,
    private val playerIconName: String,
    private val inverted: Boolean,
    private val backgroundColor: Color,
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
        return PlayerDetailsView(x, y, width, playerName, playerRating, playerIconName, inverted, backgroundColor, graphicsContext)
    }

    fun recolor(color: Color): PlayerDetailsView {
        return PlayerDetailsView(x, y, width, playerName, playerRating, playerIconName, inverted, color, graphicsContext)
    }

    private fun initViews() {
        val avatarView = AvatarView(
            iconName = playerIconName,
            size = 96
        )
        val nameTextView = TextView(
            graphicsContext = graphicsContext,
            text = playerName,
            color = textColor,
            font = FONT_FOR_NAME
        )
        val ratingTextView = TextView(
            graphicsContext = graphicsContext,
            text = playerRating.toString(),
            color = textColor,
            font = FONT_FOR_RATING
        )
        val ratingWithBackground = RoundBackgroundView(ratingBackgroundColor, ratingTextView, 50, 24, 12)
        height = 2 * PADDING_VERTICAL + max(avatarView.height, max(nameTextView.height, ratingWithBackground.height))
        val centerY = height / 2
        val avatarViewX = if (inverted) width - AVATAR_PADDING - avatarView.width else x + AVATAR_PADDING
        val secondViewX = if (inverted) avatarViewX - AVATAR_PADDING - ratingWithBackground.width else avatarViewX + avatarView.width + AVATAR_PADDING
        val thirdViewX = if (inverted) secondViewX - PADDING_HORIZONTAL - nameTextView.width else secondViewX + nameTextView.width + PADDING_HORIZONTAL
        addChild(avatarView.move(avatarViewX, centerY - avatarView.height / 2))
        addChild(nameTextView.move(if (inverted) thirdViewX else secondViewX, centerY - nameTextView.height / 2))
        addChild(ratingWithBackground.move(if (inverted) secondViewX else thirdViewX, centerY - ratingWithBackground.height / 2))
    }

    companion object {
        private val FONT_FOR_NAME = Font("Gilroy-Bold", Font.PLAIN, 32)
        private val FONT_FOR_RATING = Font("Gilroy-Regular", Font.PLAIN, 32)
        private const val AVATAR_PADDING = 40
        private const val PADDING_VERTICAL = 48
        private const val PADDING_HORIZONTAL = 32
        private val textColor = Color.decode("#FFFFFF")
        private val ratingBackgroundColor = Color.decode("#212121")
    }
}