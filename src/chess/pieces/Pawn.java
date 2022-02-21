package chess.pieces;

import chess.Board;

public class Pawn extends Piece {
    public Pawn(boolean isWhite, Board board, int row, int col) {
        this.pieceType = isWhite ? (Piece.PAWN | Piece.WHITE) : (Piece.PAWN | Piece.BLACK);
        this.board = board;
        this.image = isWhite ? WHITE_PAWN_IMAGE : BLACK_PAWN_IMAGE;
        this.row = row;
        this.col = col;
        this.squareId = col + row * board.getSize();
    }
}
