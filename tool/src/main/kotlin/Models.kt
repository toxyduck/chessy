enum class FigureName {
    Pawn,
    Knight,
    Bishop,
    Rock,
    Queen,
    King
}

enum class Side {
    Black, White
}

data class Figure(val figureName: FigureName, val side: Side)

data class Cell(val x: Int, val y: Int) {
    fun isBlack(): Boolean {
        return ((x + y) % 2) == 0
    }
    companion object {
        fun fromNotation(notationName: String): Cell? {
            if (notationName.length != 2) return null
            val x = getX(notationName[0]) ?: return null
            val y = getY(notationName[1]) ?: return null
            return Cell(x, y)
        }
        private fun getX(char: Char): Int? {
            return when (char) {
                in 'a'..'h' -> char - 'a'
                in 'A'..'H' -> char - 'A'
                else -> null
            }
        }
        private fun getY(char: Char): Int? {
            return if (char in '1' .. '8') {
                char - '1'
            } else {
                null
            }
        }
    }
}

data class CellWithFigure(val cell: Cell, val figure: Figure?)

data class Board(val cells: List<CellWithFigure>)

data class Move(val from: Cell, val to: Cell)

data class Game(
    val whitePlayer: String,
    val blackPlayer: String,
    val where: String,
    val initialState: Board,
    val moves: List<Move>
)