package chess.pieces;

import chess.Board;

public class King extends Piece {
    public King(int color, Board board, int row, int col) {
        this.pieceType = color | KING;
        this.board = board;
        this.image = color == WHITE ? WHITE_KING_IMAGE : BLACK_KING_IMAGE;
        this.row = row;
        this.col = col;
        this.squareId = col + row * board.getSize();
        this.moved = false;
    }
}
