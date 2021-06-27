package io.chessy.tool.chess

import com.github.bhlangonijr.chesslib.Board
import com.github.bhlangonijr.chesslib.pgn.PgnHolder
import io.chessy.tool.FFmpegVideoMaker
import io.chessy.tool.controller.Controller


class Chessy {

    private val videoMaker = FFmpegVideoMaker(1080, 1920, 60)

    fun fromPgn(pgnString: String) {
        val pgn = PgnHolder("")
        pgn.loadPgn(pgnString)
        if (pgn.games.isNotEmpty()) {
            val game = pgn.games.first()
            game.loadMoveText()
            val moves = game.halfMoves
            val board = Board()
            val mateMoves = mutableListOf<Pair<Boolean, Boolean>>()
            val piecesMoved = mutableListOf<Pair<Piece, PromotionPiece>>()
            for (move in moves) {
                piecesMoved.add(Pair(board.getPiece(move.from).toDomainPiece()!!, move.promotion.toDomainPiece()))
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
                    piecesMoved[ix].first,
                    piecesMoved[ix].second
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
            val controller = Controller(initialState, domainMoves, 1080, 1920, 60) {
                videoMaker.addFrame(it)
            }
            controller.startRender()
            videoMaker.endRecord()
        }
    }
}