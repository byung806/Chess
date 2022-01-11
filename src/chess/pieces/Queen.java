package chess.pieces;

import chess.Board;

public class Queen extends Piece {
    public Queen(boolean isWhite, Board board, int row, int col) {
        this.pieceType = isWhite ? (Piece.Queen + Piece.White) : (Piece.Queen + Piece.Black);
        this.board = board;
        this.image = isWhite ? WhiteQueenImage : BlackQueenImage;
        this.row = row;
        this.col = col;
        this.squareId = col + row * board.getSize();
    }
}
