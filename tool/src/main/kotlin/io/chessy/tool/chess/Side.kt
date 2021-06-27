package io.chessy.tool.chess

enum class Side {
    Black, White
}

fun Side.opposite(): Side {
    return when (this) {
        Side.Black -> Side.White
        Side.White -> Side.Black
    }
}