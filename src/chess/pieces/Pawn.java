package chess.pieces;

import chess.Board;

public class Pawn extends Piece {
    public Pawn(boolean isWhite, Board board, int row, int col) {
        this.pieceType = isWhite ? (Piece.Pawn + Piece.White) : (Piece.Pawn + Piece.Black);
        this.board = board;
        this.image = isWhite ? WhitePawnImage : BlackPawnImage;
        this.row = row;
        this.col = col;
        this.squareId = col + row * board.getSize();
    }
}
