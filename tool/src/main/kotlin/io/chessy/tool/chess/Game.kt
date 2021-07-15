package io.chessy.tool.chess

import io.chessy.tool.view.RootView

data class Game(
    val whitePlayer: Player,
    val blackPlayer: Player,
    val event: String,
    val tournament: String,
    val date: String,
    val initialState: Board,
    val moves: List<Move>,
    val result: RootView.GameResult
)
