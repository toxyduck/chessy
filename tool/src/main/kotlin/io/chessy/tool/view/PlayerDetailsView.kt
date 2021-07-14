package io.chessy.tool.view

import io.chessy.tool.ColorFactory
import io.chessy.tool.animator.AlphaAnimator
import io.chessy.tool.animator.ColorAnimator
import io.chessy.tool.interpolator.EaseInSineInterpolator
import java.awt.Color
import java.awt.Font
import java.awt.Graphics
import kotlin.math.max

enum class GameResult {
    WIN, DRAW
}

class PlayerDetailsView(
    override val x: Int = 0,
    override val y: Int = 0,
    override val width: Int,
    private val playerName: String,
    private val playerRating: Int,
    private val playerIconName: String,
    private val inverted: Boolean,
    private val config: Config,
    private val graphicsContext: Graphics
) : ViewGroup<GameResult>() {

    override var height: Int = 0
    private set

    //refactor it
    private var thirdViewX = 0
    private var centerY = 0
    private var ratingWidth = 0

    init {
        initViews()
    }

    override fun copy(x: Int, y: Int, width: Int, height: Int): View {
        return PlayerDetailsView(x, y, width, playerName, playerRating, playerIconName, inverted, config, graphicsContext)
    }

    override fun obtainAction(action: GameResult) {
        val backgroundView = removeViewSafe<FillView>(BACKGROUND_TAG)
        val fromColor = config.backgroundColorHex
        val (toColor, text) = when(action) {
            GameResult.WIN -> config.winnerBackgroundColorHex to config.winnerText
            GameResult.DRAW -> config.drawBackgroundColorHex to config.drawText
        }
        backgroundView?.let { animatedView ->
            val animator = ColorAnimator(
                EaseInSineInterpolator,
                animatedView,
                config.resultAnimationDuration,
                fromColor,
                toColor
            ) { color, view -> view.recolor(color) }
            addChildOneAction(animator, z = 0)
        }
        val winnerTextView = TextView(
            graphicsContext = graphicsContext,
            text = text,
            color = if (action == GameResult.WIN) config.winnerTextColor else config.drawTextColor,
            font = config.fontName
        )
        val winnerView = RoundBackgroundView(config.resultViewBackgroundColor, winnerTextView, 0, config.resultViewPaddingHorizontal, config.resultViewPaddingVertical)
        val winnerX = if (inverted) thirdViewX - config.otherViewsPaddingHorizontal - winnerView.width else thirdViewX + ratingWidth + config.otherViewsPaddingHorizontal
        val movedWinnerView = winnerView.move(winnerX, centerY - winnerView.height / 2)
        addChildOneAction(AlphaAnimator(EaseInSineInterpolator, AlphaView(movedWinnerView, 0f), config.resultAnimationDuration))
    }

    private fun initViews() {
        val avatarView = AvatarView(
            iconName = playerIconName,
            size = config.avatarSize
        )
        val nameTextView = TextView(
            graphicsContext = graphicsContext,
            text = playerName,
            color = config.textColor,
            font = config.fontName
        )
        val ratingTextView = TextView(
            graphicsContext = graphicsContext,
            text = playerRating.toString(),
            color = config.textColor,
            font = config.fontRating
        )
        val ratingWithBackground = RoundBackgroundView(config.ratingBackgroundColor, ratingTextView, config.ratingRadius, config.ratingPaddingHorizontal, config.ratingPaddingVertical)
        height = 2 * config.paddingVertical + max(avatarView.height, max(nameTextView.height, ratingWithBackground.height))
        centerY = y + height / 2
        val avatarViewX = if (inverted) width - config.avatarPadding - avatarView.width else x + config.avatarPadding
        val secondViewX = if (inverted) avatarViewX - config.avatarPadding - ratingWithBackground.width else avatarViewX + avatarView.width + config.avatarPadding
        thirdViewX = if (inverted) secondViewX - config.otherViewsPaddingHorizontal - nameTextView.width else secondViewX + nameTextView.width + config.otherViewsPaddingHorizontal
        val backgroundColor = ColorFactory.fromInt(config.backgroundColorHex)
        val fillView = FillView(x, y, width, height, backgroundColor)
        ratingWidth = ratingWithBackground.width
        addChild(fillView, BACKGROUND_TAG, z = 0)
        addChild(OnceDrawView(avatarView.move(avatarViewX, centerY - avatarView.height / 2)))
        addChild(nameTextView.move(if (inverted) thirdViewX else secondViewX, centerY - nameTextView.height / 2))
        addChild(ratingWithBackground.move(if (inverted) secondViewX else thirdViewX, centerY - ratingWithBackground.height / 2))
    }

    class Config(
        val fontName: Font,
        val fontRating: Font,
        val textColor: Color,
        val avatarPadding: Int,
        val otherViewsPaddingHorizontal: Int,
        val paddingVertical: Int,
        val winnerTextColor: Color,
        val drawTextColor: Color,
        val resultAnimationDuration: Int,
        val ratingBackgroundColor: Color,
        val ratingRadius: Int,
        val ratingPaddingHorizontal: Int,
        val ratingPaddingVertical: Int,
        val resultViewBackgroundColor: Color,
        val backgroundColorHex: Int,
        val winnerBackgroundColorHex: Int,
        val drawBackgroundColorHex: Int,
        val resultViewPaddingHorizontal: Int,
        val resultViewPaddingVertical: Int,
        val winnerText: String,
        val drawText: String,
        val avatarSize: Int,
    )

    companion object {
//        private val FONT_FOR_NAME = Font("Gilroy-Bold", Font.PLAIN, 32)
//        private val FONT_FOR_RATING = Font("Gilroy-Regular", Font.PLAIN, 32)
//        private const val AVATAR_PADDING = 40
//        private const val PADDING_VERTICAL = 48
//        private const val PADDING_HORIZONTAL = 32
//        private val textColor = Color.decode("#FFFFFF")
//        private val ratingBackgroundColor = Color.decode("#212121")
//        private val winnerColor = Color.decode("#BE8400")
//        private val drawColor = Color.decode("#000000")
        private const val BACKGROUND_TAG = "background_tag"
//        private const val WINNER_ANIMATION_DURATION = 256 / 16
    }
}