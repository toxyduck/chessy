package io.chessy.tool.chess


data class Cell(val x: Int, val y: Int) {
    companion object {
        fun fromNotation(notationName: String): Cell? {
            if (notationName.length != 2) return null
            val x = getX(notationName[0]) ?: return null
            val y = getY(notationName[1]) ?: return null
            return Cell(x, 7 - y)
        }

        private fun getX(char: Char): Int? {
            return when (char) {
                in 'a'..'h' -> char - 'a'
                in 'A'..'H' -> char - 'A'
                else -> null
            }
        }

        private fun getY(char: Char): Int? {
            return if (char in '1'..'8') {
                char - '1'
            } else {
                null
            }
        }
    }
}

data class CellWithPiece(val cell: Cell, val piece: Piece?)