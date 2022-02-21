package chess.pieces;

import chess.Board;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public abstract class Piece {
    public static final int KING = 0b001;   // 1
    public static final int PAWN = 0b010;   // 2
    public static final int KNIGHT = 0b011; // 3
    public static final int BISHOP = 0b101; // 5
    public static final int ROOK = 0b110;   // 6
    public static final int QUEEN = 0b111;  // 7

    public static final int WHITE = 0b01000; // 8
    public static final int BLACK = 0b10000; // 16
    public static final int TYPE_MASK = 0b00111;
    public static final int COLOR_MASK = 0b11000;

    // Each piece can be represented by CCTTT in binary where CC represents the color and TTT represents the piece type

    protected static final Image WHITE_PAWN_IMAGE = new ImageIcon("assets/textures/white_pawn.png").getImage();
    protected static final Image WHITE_KING_IMAGE = new ImageIcon("assets/textures/white_king.png").getImage();
    protected static final Image WHITE_KNIGHT_IMAGE = new ImageIcon("assets/textures/white_knight.png").getImage();
    protected static final Image WHITE_BISHOP_IMAGE = new ImageIcon("assets/textures/white_bishop.png").getImage();
    protected static final Image WHITE_ROOK_IMAGE = new ImageIcon("assets/textures/white_rook.png").getImage();
    protected static final Image WHITE_QUEEN_IMAGE = new ImageIcon("assets/textures/white_queen.png").getImage();
    protected static final Image BLACK_PAWN_IMAGE = new ImageIcon("assets/textures/black_pawn.png").getImage();
    protected static final Image BLACK_KING_IMAGE = new ImageIcon("assets/textures/black_king.png").getImage();
    protected static final Image BLACK_KNIGHT_IMAGE = new ImageIcon("assets/textures/black_knight.png").getImage();
    protected static final Image BLACK_BISHOP_IMAGE = new ImageIcon("assets/textures/black_bishop.png").getImage();
    protected static final Image BLACK_ROOK_IMAGE = new ImageIcon("assets/textures/black_rook.png").getImage();
    protected static final Image BLACK_QUEEN_IMAGE = new ImageIcon("assets/textures/black_queen.png").getImage();

    protected int pieceType;
    protected Image image;
    protected Board board;
    protected int row, col;  // upper left corner is (0,0)
    protected int squareId;

    public Image getImage() {
        return this.image;
    }

    public int getRow() {
        return this.row;
    }

    public int getCol() {
        return this.col;
    }

    public Board getBoard() {
        return this.board;
    }

    public int getSquareId() {
        return this.squareId;
    }

    public void setSquareId(int squareId) {
        this.squareId = squareId;
        this.col = squareId % board.getSize();
        this.row = (squareId - this.col) / board.getSize();
    }

    public boolean isColor(int color) {
        return (this.pieceType & COLOR_MASK) == color;
    }

    public int color() {
        return this.pieceType & COLOR_MASK;
    }

    public int pieceType() {
        return this.pieceType;
    }

    public boolean isRook() {
        return (this.pieceType & TYPE_MASK) == ROOK;
    }

    public boolean isBishop() {
        return (this.pieceType & TYPE_MASK) == BISHOP;
    }

    public boolean isQueen() {
        return (this.pieceType & TYPE_MASK) == QUEEN;
    }

    public boolean isKing() {
        return (this.pieceType & TYPE_MASK) == KING;
    }

    public boolean isKnight() {
        return (this.pieceType & TYPE_MASK) == KNIGHT;
    }

    public boolean isPawn() {
        return (this.pieceType & TYPE_MASK) == PAWN;
    }

    public boolean isSlidingPiece() {
        return (this.pieceType & 0b100) == 0b100;
    }

    public String toString() {
        HashMap<Integer, String> asciiChess = new HashMap<>();
        asciiChess.put(WHITE | KING, "K");
        asciiChess.put(WHITE | QUEEN, "Q");
        asciiChess.put(WHITE | ROOK, "R");
        asciiChess.put(WHITE | BISHOP, "B");
        asciiChess.put(WHITE | KNIGHT, "N");
        asciiChess.put(WHITE | PAWN, "P");
        asciiChess.put(BLACK | KING, "k");
        asciiChess.put(BLACK | QUEEN, "q");
        asciiChess.put(BLACK | ROOK, "r");
        asciiChess.put(BLACK | BISHOP, "b");
        asciiChess.put(BLACK | KNIGHT, "n");
        asciiChess.put(BLACK | PAWN, "p");
        return asciiChess.get(pieceType);
    }
}
