package io.chessy.tool.view

import io.chessy.tool.animator.ColorAnimator
import io.chessy.tool.interpolator.EaseInSineInterpolator
import java.awt.Color
import java.awt.Font
import java.awt.Graphics
import kotlin.math.max

typealias ShowWinner = Unit

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
) : ViewGroup<ShowWinner>() {

    override var height: Int = 0
    private set

    init {
        initViews()
    }

    override fun copy(x: Int, y: Int, width: Int, height: Int): View {
        return PlayerDetailsView(x, y, width, playerName, playerRating, playerIconName, inverted, backgroundColor, graphicsContext)
    }

    override fun obtainAction(action: ShowWinner) {
        val backgroundView = removeViewSafe<FillView>(BACKGROUND_TAG)
        backgroundView?.let { animatedView ->
            val animator = ColorAnimator(
                EaseInSineInterpolator,
                animatedView,
                256 / 16,
                0x4D4D4D,
                0xBE8400
            ) { color, view -> view.recolor(color) }
            addChildOneAction(animator, z = 0)
        }
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
        val centerY = y + height / 2
        val avatarViewX = if (inverted) width - AVATAR_PADDING - avatarView.width else x + AVATAR_PADDING
        val secondViewX = if (inverted) avatarViewX - AVATAR_PADDING - ratingWithBackground.width else avatarViewX + avatarView.width + AVATAR_PADDING
        val thirdViewX = if (inverted) secondViewX - PADDING_HORIZONTAL - nameTextView.width else secondViewX + nameTextView.width + PADDING_HORIZONTAL
        val fillView = FillView(x, y, width, height, backgroundColor)
        addChild(fillView, BACKGROUND_TAG, z = 0)
        addChild(OnceDrawView(avatarView.move(avatarViewX, centerY - avatarView.height / 2)))
        addChild(nameTextView.move(if (inverted) thirdViewX else secondViewX, centerY - nameTextView.height / 2))
        addChild(ratingWithBackground.move(if (inverted) secondViewX else thirdViewX, centerY - ratingWithBackground.height / 2))
        val winnerTextView = TextView(
            graphicsContext = graphicsContext,
            text = "ВЫИГРАЛ",
            color = winnerColor,
            font = FONT_FOR_NAME
        )
        val winnerView = RoundBackgroundView(textColor, winnerTextView, 0, 24, 12)
        val winnerX = if (inverted) thirdViewX - PADDING_HORIZONTAL - winnerView.width else thirdViewX + ratingWithBackground.width + PADDING_HORIZONTAL
        addChild(winnerView.move(winnerX, centerY - winnerView.height / 2))
    }

    companion object {
        private val FONT_FOR_NAME = Font("Gilroy-Bold", Font.PLAIN, 32)
        private val FONT_FOR_RATING = Font("Gilroy-Regular", Font.PLAIN, 32)
        private const val AVATAR_PADDING = 40
        private const val PADDING_VERTICAL = 48
        private const val PADDING_HORIZONTAL = 32
        private val textColor = Color.decode("#FFFFFF")
        private val ratingBackgroundColor = Color.decode("#212121")
        private val winnerColor = Color.decode("#BE8400")
        private const val BACKGROUND_TAG = "background_tag"
    }
}