package io.chessy.tool

val whitePawn = Piece(PieceName.Pawn, Side.White)
val blackPawn = Piece(PieceName.Pawn, Side.Black)
val whiteKnight = Piece(PieceName.Knight, Side.White)
val blackKnight = Piece(PieceName.Knight, Side.Black)
val whiteBishop = Piece(PieceName.Bishop, Side.White)
val blackBishop = Piece(PieceName.Bishop, Side.Black)
val whiteRook = Piece(PieceName.Rook, Side.White)
val blackRook = Piece(PieceName.Rook, Side.Black)
val whiteQueen = Piece(PieceName.Queen, Side.White)
val blackQueen = Piece(PieceName.Queen, Side.Black)
val whiteKing = Piece(PieceName.King, Side.White)
val blackKing = Piece(PieceName.King, Side.Black)

private val realInitialBoardFigures = listOf(
    blackRook, blackKnight, blackBishop, blackQueen, blackKing, blackBishop, blackKnight, blackRook,
    blackPawn, blackPawn, blackPawn, blackPawn, blackPawn, blackPawn, blackPawn, blackPawn,
    null, null, null, null, null, null, null, null,
    null, null, null, null, null, null, null, null,
    null, null, null, null, null, null, null, null,
    null, null, null, null, null, null, null, null,
    whitePawn, whitePawn, whitePawn, whitePawn, whitePawn, whitePawn, whitePawn, whitePawn,
    whiteRook, whiteKnight, whiteBishop, whiteQueen, whiteKing, whiteBishop, whiteKnight, whiteRook
)

private val realCells = (0 until 64).map { ix ->
    val x = ix % 8
    val y = (ix / 8)
    Cell(x, y)
}

val realBoard = Board(
    realInitialBoardFigures.zip(realCells) { piece: Piece?, cell ->
        CellWithPiece(cell, piece)
    }
)