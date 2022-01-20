package chess.pieces;

import chess.Board;

public class King extends Piece {
    public King(boolean isWhite, Board board, int row, int col) {
        this.pieceType = isWhite ? (Piece.King | Piece.White) : (Piece.King | Piece.Black);
        this.board = board;
        this.image = isWhite ? WhiteKingImage : BlackKingImage;
        this.row = row;
        this.col = col;
        this.squareId = col + row * board.getSize();
    }
}
