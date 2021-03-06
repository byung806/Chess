package chess.pieces;

import chess.Board;

public class Queen extends Piece {
    public Queen(int color, Board board, int row, int col) {
        this.pieceType = color | QUEEN;
        this.board = board;
        this.image = color == WHITE ? WHITE_QUEEN_IMAGE : BLACK_QUEEN_IMAGE;
        this.row = row;
        this.col = col;
        this.squareId = col + row * board.getSize();
        this.moved = false;
    }
}
