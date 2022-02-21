package chess.pieces;

import chess.Board;

public class Knight extends Piece {
    public Knight(boolean isWhite, Board board, int row, int col) {
        this.pieceType = isWhite ? (Piece.KNIGHT | Piece.WHITE) : (Piece.KNIGHT | Piece.BLACK);
        this.board = board;
        this.image = isWhite ? WHITE_KNIGHT_IMAGE : BLACK_KNIGHT_IMAGE;
        this.row = row;
        this.col = col;
        this.squareId = col + row * board.getSize();
    }
}
