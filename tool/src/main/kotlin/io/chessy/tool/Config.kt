package io.chessy.tool

import io.chessy.tool.view.*
import java.awt.Color
import java.awt.Font

val eventDetailsViewConfig = EventDetailsView.Config(
    fontEvent = Font("Gilroy-Bold", Font.PLAIN, 32),
    fontTournament = Font("Gilroy-Medium", Font.PLAIN, 32),
    fontDate = Font("Gilroy-Regular", Font.PLAIN, 32),
    textColor = Color.decode("#FFFFFF"),
    paddingVertical = 32,
    paddingHorizontal = 32
)

val playerDetailsViewConfig = PlayerDetailsView.Config(
    fontName = Font("Gilroy-Bold", Font.PLAIN, 32),
    fontRating = Font("Gilroy-Regular", Font.PLAIN, 32),
    textColor = Color.decode("#FFFFFF"),
    avatarPadding = 40,
    otherViewsPaddingHorizontal = 32,
    paddingVertical = 48,
    winnerTextColor = Color.decode("#BE8400"),
    drawTextColor = Color.decode("#000000"),
    resultAnimationDuration = 256 / 16,
    ratingBackgroundColor = Color.decode("#212121"),
    ratingRadius = 50,
    ratingPaddingHorizontal = 24,
    ratingPaddingVertical = 12,
    resultViewBackgroundColor = Color.decode("#FFFFFF"),
    backgroundColorHex = 0x4D4D4D,
    winnerBackgroundColorHex = 0XCD9C47,
    drawBackgroundColorHex = 0X969696,
    resultViewPaddingHorizontal = 24,
    resultViewPaddingVertical = 12,
    winnerText = "ВЫИГРАЛ",
    drawText = "НИЧЬЯ",
    avatarSize = 96
)

val gameViewConfig = GameView.Config(
    piecePadding = 24,
    moveDuration = 500 / 16,
    blackColor = Color.decode("#B27B41"),
    whiteColor = Color.decode("#DEC496"),
    specialColor = Color.decode("#B24341")
)

val borderedGameViewConfig = BorderedGameView.Config(
    borderFont = Font("SansSerif", Font.PLAIN, 32),
    symbolsVertical = ('1' until '9').toList(),
    symbolsHorizontal = ('A' until 'I').toList(),
    gameViewPadding = 8,
    symbolsColor = Color(0x88FFFFFF.toInt(), true),
    borderColor = Color.decode("#272522"),
    gameViewConfig = gameViewConfig
)

val rootViewConfig = RootView.Config(
    backgroundColor = Color.decode("#212121"),
    bottomPadding = 148,
    eventDetailsViewConfig = eventDetailsViewConfig,
    playerDetailsViewConfig = playerDetailsViewConfig,
    borderedGameViewConfig = borderedGameViewConfig
)