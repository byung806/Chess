package chess.pieces;

import chess.Board;

public class Knight extends Piece {
    public Knight(int color, Board board, int row, int col) {
        this.pieceType = color | KNIGHT;
        this.board = board;
        this.image = color == WHITE ? WHITE_KNIGHT_IMAGE : BLACK_KNIGHT_IMAGE;
        this.row = row;
        this.col = col;
        this.squareId = col + row * board.getSize();
    }
}
