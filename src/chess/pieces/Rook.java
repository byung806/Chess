package chess.pieces;

import chess.Board;

public class Rook extends Piece {
    public Rook(boolean isWhite, Board board, int row, int col) {
        this.pieceType = isWhite ? (Piece.Rook + Piece.White) : (Piece.Rook + Piece.Black);
        this.board = board;
        this.image = isWhite ? WhiteRookImage : BlackRookImage;
        this.row = row;
        this.col = col;
        this.squareId = col + row * board.getSize();
    }
}
