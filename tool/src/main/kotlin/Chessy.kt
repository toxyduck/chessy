package io.chessy.tool

import com.github.bhlangonijr.chesslib.Board
import com.github.bhlangonijr.chesslib.pgn.PgnHolder

class Chessy {

    private val drawer = JFrameMoveDrawer(1080, 1920, 60)
    private val videoMaker = FFmpegVideoMaker(1080, 1920, 60)

    init {
        drawer.addFrameListener(videoMaker::addFrame)
    }

    fun fromPgn(pgnString: String) {
        val pgn = PgnHolder("")
        pgn.loadPgn(pgnString)
        if (pgn.games.isNotEmpty()) {
            val game = pgn.games.first()
            game.loadMoveText()
            val moves = game.halfMoves
            val board = Board()
            val mateMoves = mutableListOf<Pair<Boolean, Boolean>>()
            for (move in moves) {
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
                    mateMoves[ix].second
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
            domainMoves.forEach { move ->
                drawMove(move, currentBoard)
                currentBoard = currentBoard.mutate(move)
            }
//**************************Test*************************************
//            domainMoves.subList(0, 10).forEach {
//                drawMove(it, currentBoard)
//                currentBoard = currentBoard.mutate(it)
//            }
//            videoMaker.endRecord()
//********************************************************************
        }
    }

    private fun drawMove(move: Move, currentBoard: io.chessy.tool.Board) {
        when {
            move.isCastleMove -> {
                move.rookCastleMove()?.let { rookMove ->
                    drawer.drawCastle(currentBoard, move, rookMove)
                }
            }
//            move.isMateMove -> {
//                move.rookCastleMove()?.let { rookMove ->
//                    drawer.drawCastle(currentBoard, move, rookMove)
//                }
//            }
            move.isKingAttacked -> {
                move.rookCastleMove()?.let { rookMove ->
                    drawer.drawCastle(currentBoard, move, rookMove)
                }
            }
            else -> {
                drawer.drawMove(currentBoard, move)
            }


        }
    }
}