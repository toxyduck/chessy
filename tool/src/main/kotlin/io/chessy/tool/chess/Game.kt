package io.chessy.tool.chess

data class Game(
    val whitePlayer: String,
    val blackPlayer: String,
    val where: String,
    val initialState: Board,
    val moves: List<Move>
)