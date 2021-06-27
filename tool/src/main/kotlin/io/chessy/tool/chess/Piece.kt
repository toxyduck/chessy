package io.chessy.tool.chess

typealias PromotionPiece = Piece?

data class Piece(val pieceName: PieceName, val side: Side)

fun com.github.bhlangonijr.chesslib.Piece.toDomainPiece(): Piece? {
    return when (this) {
        com.github.bhlangonijr.chesslib.Piece.WHITE_PAWN -> Piece(PieceName.Pawn, Side.White)
        com.github.bhlangonijr.chesslib.Piece.WHITE_KNIGHT -> Piece(PieceName.Knight, Side.White)
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

enum class PieceName {
    Pawn,
    Knight,
    Bishop,
    Rook,
    Queen,
    King
}