package chess.pieces;

import chess.Board;

public class Rook extends Piece {
    public Rook(boolean isWhite, Board board, int row, int col) {
        this.pieceType = isWhite ? (Piece.ROOK | Piece.WHITE) : (Piece.ROOK | Piece.BLACK);
        this.board = board;
        this.image = isWhite ? WHITE_ROOK_IMAGE : BLACK_ROOK_IMAGE;
        this.row = row;
        this.col = col;
        this.squareId = col + row * board.getSize();
    }
}
