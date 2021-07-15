package io.chessy.tool.chess

import com.github.bhlangonijr.chesslib.Board
import com.github.bhlangonijr.chesslib.pgn.PgnHolder
import io.chessy.tool.FFmpegVideoMaker
import io.chessy.tool.controller.Controller
import io.chessy.tool.view.PieceView
import io.chessy.tool.view.RootView
import java.net.URL


class Chessy(
    private val width: Int,
    private val height: Int,
    private val fps: Int,
    private val whitePlayer: Player?,
    private val blackPlayer: Player?,
    private val event: String?,
    private val tournament: String?,
    private val date: String?,
    private val pgnString: String,
    private val config: RootView.Config
) {

    fun fromPgn(outputFilePath: String) {
        val videoMaker = FFmpegVideoMaker(width, height, fps, outputFilePath)

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
            val blackPlayerName = game.blackPlayer.name
            val whitePlayerName = game.whitePlayer.name
            val where = game.round.event.name
            val tournamentName = game.round.event.site
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
                whitePlayer = whitePlayer ?: Player(whitePlayerName, game.whitePlayer.elo, null),
                blackPlayer = blackPlayer ?: Player(blackPlayerName, game.blackPlayer.elo, null),
                event ?: where,
                tournament ?: tournamentName,
                date ?: game.date,
                initialState,
                domainMoves
            )
            videoMaker.startRecord()
            val controller = Controller(domainGame, domainMoves, width, height, fps, config) {
                videoMaker.addFrame(it)
            }
            controller.startRender()
            videoMaker.endRecord()
        }
    }

}