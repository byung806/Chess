package chess.pieces;

import chess.Board;

public class Knight extends Piece {
    public Knight(boolean isWhite, Board board, int row, int col) {
        this.pieceType = isWhite ? (Piece.Knight & Piece.White) : (Piece.Knight & Piece.Black);
        this.board = board;
        this.image = isWhite ? WhiteKnightImage : BlackKnightImage;
        this.row = row;
        this.col = col;
        this.squareId = col + row * board.getSize();
    }
}
