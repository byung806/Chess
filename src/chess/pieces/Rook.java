package chess.pieces;

import chess.Board;

public class Rook extends Piece {
    public Rook(int color, Board board, int row, int col) {
        this.pieceType = color == WHITE ? (WHITE | ROOK) : (BLACK | ROOK);
        this.board = board;
        this.image = color == WHITE ? WHITE_ROOK_IMAGE : BLACK_ROOK_IMAGE;
        this.row = row;
        this.col = col;
        this.squareId = col + row * board.getSize();
    }
}
