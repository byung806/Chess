package chess.pieces;

import chess.Board;

public class Pawn extends Piece {
    public Pawn(int color, Board board, int row, int col) {
        this.pieceType = color | PAWN;
        this.board = board;
        this.image = color == WHITE ? WHITE_PAWN_IMAGE : BLACK_PAWN_IMAGE;
        this.row = row;
        this.col = col;
        this.squareId = col + row * board.getSize();
        this.moved = false;
    }
}
