package io.chessy.tool

import com.github.bhlangonijr.chesslib.Board
import com.github.bhlangonijr.chesslib.pgn.PgnHolder
import io.chessy.tool.controller.Controller

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


class Chessy {

    //    private val drawer = JFrameMoveDrawer(1080, 1920, 60)
    private val videoMaker = FFmpegVideoMaker(1080, 1920, 60)

//    init {
//        drawer.addFrameListener(videoMaker::addFrame)
//    }

    fun fromPgn(pgnString: String) {
        val pgn = PgnHolder("")
        pgn.loadPgn(pgnString)
        if (pgn.games.isNotEmpty()) {
            val game = pgn.games.first()
            game.loadMoveText()
            val moves = game.halfMoves
            val board = Board()
            val mateMoves = mutableListOf<Pair<Boolean, Boolean>>()
            val piecesMoved = mutableListOf<Piece>()
            for (move in moves) {
                piecesMoved.add(board.getPiece(move.from).toDomainPiece()!!)
                board.doMove(move)
                mateMoves.add(Pair(board.isMated, board.isKingAttacked))
            }
            val blackPlayer = game.blackPlayer.name
            val whitePlayer = game.whitePlayer.name
            val where = game.round.event.name
            val initialState = realBoard
            val domainMoves = moves.mapIndexed { ix, move ->
                val from = Cell.fromNotation(move.from.value()) ?: throw Exception()
                val to = Cell.fromNotation(move.to.value()) ?: throw Exception()
                Move(
                    from,
                    to,
                    board.backup[ix].isCastleMove,
                    mateMoves[ix].first,
                    mateMoves[ix].second,
                    piecesMoved[ix]
                )
            }
            val domainGame = Game(
                whitePlayer,
                blackPlayer,
                where,
                initialState,
                domainMoves
            )
            videoMaker.startRecord()
            var currentBoard = initialState

//**************************Prod*************************************
            val controller = Controller(currentBoard, domainMoves, 1080, 1920, 60) {
                videoMaker.addFrame(it)
            }
            controller.startRender()
//            domainMoves.forEach { move ->
//                drawMove(move, currentBoard)
//                currentBoard = currentBoard.mutate(move)
//            }
//**************************Test*************************************
//            domainMoves.subList(0, 10).forEach {
//                drawMove(it, currentBoard)
//                currentBoard = currentBoard.mutate(it)
//            }
//********************************************************************
            videoMaker.endRecord()
        }
    }

//    private fun drawMove(move: Move, currentBoard: io.chessy.tool.Board) {
//        when {
//            move.isCastleMove -> {
//                move.rookCastleMove()?.let { rookMove ->
//                    drawer.drawCastle(currentBoard, move, rookMove)
//                }
//            }
////            move.isMateMove -> {
////                move.rookCastleMove()?.let { rookMove ->
////                    drawer.drawCastle(currentBoard, move, rookMove)
////                }
////            }
//            move.isKingAttacked -> {
//                drawer.drawKingAttacked(currentBoard, move)
//            }
//            else -> {
//                drawer.drawMove(currentBoard, move)
//            }
//        }
//    }
}