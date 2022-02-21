package chess.pieces;

import chess.Board;

public class Queen extends Piece {
    public Queen(boolean isWhite, Board board, int row, int col) {
        this.pieceType = isWhite ? (Piece.QUEEN | Piece.WHITE) : (Piece.QUEEN | Piece.BLACK);
        this.board = board;
        this.image = isWhite ? WHITE_QUEEN_IMAGE : BLACK_QUEEN_IMAGE;
        this.row = row;
        this.col = col;
        this.squareId = col + row * board.getSize();
    }
}
