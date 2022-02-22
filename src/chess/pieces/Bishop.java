package chess.pieces;

import chess.Board;

public class Bishop extends Piece {
    public Bishop(int color, Board board, int row, int col) {
        this.pieceType = color == WHITE ? (WHITE | BISHOP) : (BLACK | BISHOP);
        this.board = board;
        this.image = color == WHITE ? WHITE_BISHOP_IMAGE : BLACK_BISHOP_IMAGE;
        this.row = row;
        this.col = col;
        this.squareId = col + row * board.getSize();
    }
}
