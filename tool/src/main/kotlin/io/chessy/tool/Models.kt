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

fun Side.opposite(): Side {
    return when (this) {
        Side.Black -> Side.White
        Side.White -> Side.Black
    }
}

data class Piece(val pieceName: PieceName, val side: Side)

fun com.github.bhlangonijr.chesslib.Piece.toDomainPiece(): Piece? {
    return when (this) {
        com.github.bhlangonijr.chesslib.Piece.WHITE_PAWN -> Piece(PieceName.Pawn, Side.White)
        com.github.bhlangonijr.chesslib.Piece.WHITE_KNIGHT -> Piece(PieceName.King, Side.White)
        com.github.bhlangonijr.chesslib.Piece.WHITE_BISHOP -> Piece(PieceName.Bishop, Side.White)
        com.github.bhlangonijr.chesslib.Piece.WHITE_ROOK -> Piece(PieceName.Rook, Side.White)
        com.github.bhlangonijr.chesslib.Piece.WHITE_QUEEN -> Piece(PieceName.Queen, Side.White)
        com.github.bhlangonijr.chesslib.Piece.WHITE_KING -> Piece(PieceName.King, Side.White)
        com.github.bhlangonijr.chesslib.Piece.BLACK_PAWN -> Piece(PieceName.Pawn, Side.Black)
        com.github.bhlangonijr.chesslib.Piece.BLACK_KNIGHT -> Piece(PieceName.Knight, Side.Black)
        com.github.bhlangonijr.chesslib.Piece.BLACK_BISHOP -> Piece(PieceName.Bishop, Side.Black)
        com.github.bhlangonijr.chesslib.Piece.BLACK_ROOK -> Piece(PieceName.Rook, Side.Black)
        com.github.bhlangonijr.chesslib.Piece.BLACK_QUEEN -> Piece(PieceName.Queen, Side.Black)
        com.github.bhlangonijr.chesslib.Piece.BLACK_KING -> Piece(PieceName.King, Side.Black)
        com.github.bhlangonijr.chesslib.Piece.NONE -> null
    }
}

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
        var capturedCell: Cell? = null
        if (piece != null && piece.pieceName == PieceName.Pawn) {
            val isCapture = (move.from.x - move.to.x) != 0
            if (isCapture) {
                val capturedPiece = cells.find { it.cell == move.to }?.piece
                if (capturedPiece == null) {
                    capturedCell = Cell(move.to.x, move.from.y)
                }
            }
        }
        val mutated = this.copy(cells = this.cells.map {
            when (it.cell) {
                capturedCell -> it.copy(piece = null)
                move.from -> it.copy(piece = null)
                move.to -> it.copy(piece = piece)
                else -> it
            }
        })
        return if (move.isCastleMove) move.rookCastleMove()?.let { mutated.mutate(it) } ?: mutated else mutated
    }

    fun filter(filter: List<Cell>): Board {
        return copy(cells = this.cells.map {
            if (filter.contains(it.cell)) it.copy(piece = null) else it
        })
    }

    fun kingCell(side: Side): Cell {
        return cells.find { it.piece?.pieceName == PieceName.King && it.piece.side == side }?.cell!!
    }
}

data class Move(
    val from: Cell,
    val to: Cell,
    val isCastleMove: Boolean = false,
    val isMateMove: Boolean = false,
    val isKingAttacked: Boolean = false,
    val piece: Piece
) {

    val side = piece.side

    fun rookCastleMove(): Move? {
        if (!isCastleMove) return null
        return if (from.y == 7) {
            if (to.x >= 4) {
                Move(Cell(7, 7), Cell(5, 7), piece = Piece(PieceName.Rook, piece.side))
            } else {
                Move(Cell(0, 7), Cell(3, 7), piece = Piece(PieceName.Rook, piece.side))
            }
        } else {
            if (to.x >= 4) {
                Move(Cell(7, 0), Cell(5, 0), piece = Piece(PieceName.Rook, piece.side))
            } else {
                Move(Cell(0, 0), Cell(3, 0), piece = Piece(PieceName.Rook, piece.side))
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