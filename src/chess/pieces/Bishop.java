package chess.pieces;

import chess.Board;

public class Bishop extends Piece {
    public Bishop(boolean isWhite, Board board, int row, int col) {
        this.pieceType = isWhite ? (Piece.BISHOP | Piece.WHITE) : (Piece.BISHOP | Piece.BLACK);
        this.board = board;
        this.image = isWhite ? WHITE_BISHOP_IMAGE : BLACK_BISHOP_IMAGE;
        this.row = row;
        this.col = col;
        this.squareId = col + row * board.getSize();
    }
}
