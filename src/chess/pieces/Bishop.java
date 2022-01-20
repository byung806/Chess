package chess.pieces;

import chess.Board;

public class Bishop extends Piece {
    public Bishop(boolean isWhite, Board board, int row, int col) {
        this.pieceType = isWhite ? (Piece.Bishop | Piece.White) : (Piece.Bishop | Piece.Black);
        this.board = board;
        this.image = isWhite ? WhiteBishopImage : BlackBishopImage;
        this.row = row;
        this.col = col;
        this.squareId = col + row * board.getSize();
    }
}
