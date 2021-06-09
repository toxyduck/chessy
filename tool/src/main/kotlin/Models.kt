package io.chessy.tool

enum class PieceName {
    Pawn,
    Knight,
    Bishop,
    Rook,
    Queen,
    King
}

enum class Side {
    Black, White
}

fun Side.opposite():Side{
    return when(this){
        Side.Black -> Side.White
        Side.White -> Side.Black
    }
}

data class Piece(val pieceName: PieceName, val side: Side)

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
            return if (char in '1'..'8') {
                char - '1'
            } else {
                null
            }
        }
    }
}

data class CellWithPiece(val cell: Cell, val piece: Piece?)

data class Board(val cells: List<CellWithPiece>) {
    fun mutate(move: Move): Board {
        val piece = cells.find { it.cell == move.from }?.piece
        val mutated = this.copy(cells = this.cells.map {
            when (it.cell) {
                move.from -> it.copy(piece = null)
                move.to -> it.copy(piece = piece)
                else -> it
            }
        })
        return if (move.isCastleMove) move.rookCastleMove()?.let { mutated.mutate(it) } ?: mutated else mutated
    }
}

data class Move(
    val from: Cell,
    val to: Cell,
    val isCastleMove: Boolean,
    val isMateMove: Boolean = false,
    val isKingAttacked: Boolean = false
) {

    fun rookCastleMove(): Move? {
        if (!isCastleMove) return null
        return if (from.y == 7) {
            if (to.x >= 4) {
                Move(Cell(7, 7), Cell(5, 7), false)
            } else {
                Move(Cell(0, 7), Cell(3, 7), false)
            }
        } else {
            if (to.x >= 4) {
                Move(Cell(7, 0), Cell(5, 0), false)
            } else {
                Move(Cell(0, 0), Cell(3, 0), false)
            }
        }
    }
}

data class Game(
    val whitePlayer: String,
    val blackPlayer: String,
    val where: String,
    val initialState: Board,
    val moves: List<Move>
)