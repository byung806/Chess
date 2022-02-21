package chess.pieces;

import chess.Board;

public class King extends Piece {
    public King(boolean isWhite, Board board, int row, int col) {
        this.pieceType = isWhite ? (Piece.KING | Piece.WHITE) : (Piece.KING | Piece.BLACK);
        this.board = board;
        this.image = isWhite ? WHITE_KING_IMAGE : BLACK_KING_IMAGE;
        this.row = row;
        this.col = col;
        this.squareId = col + row * board.getSize();
    }
}
