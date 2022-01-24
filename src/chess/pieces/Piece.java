package chess.pieces;

import chess.Board;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public abstract class Piece {
    public static final int King = 0b001;   // 1
    public static final int Pawn = 0b010;   // 2
    public static final int Knight = 0b011; // 3
    public static final int Bishop = 0b101; // 5
    public static final int Rook = 0b110;   // 6
    public static final int Queen = 0b111;  // 7

    public static final int White = 0b01000; // 8
    public static final int Black = 0b10000; // 16
    public static final int typeMask = 0b00111;
    public static final int colorMask = 0b11000;

    // Each piece can be represented by CCTTT in binary where CC represents the color and TTT represents the piece type

    protected static final Image WhitePawnImage = new ImageIcon("assets/textures/white_pawn.png").getImage();
    protected static final Image WhiteKingImage = new ImageIcon("assets/textures/white_king.png").getImage();
    protected static final Image WhiteKnightImage = new ImageIcon("assets/textures/white_knight.png").getImage();
    protected static final Image WhiteBishopImage = new ImageIcon("assets/textures/white_bishop.png").getImage();
    protected static final Image WhiteRookImage = new ImageIcon("assets/textures/white_rook.png").getImage();
    protected static final Image WhiteQueenImage = new ImageIcon("assets/textures/white_queen.png").getImage();
    protected static final Image BlackPawnImage = new ImageIcon("assets/textures/black_pawn.png").getImage();
    protected static final Image BlackKingImage = new ImageIcon("assets/textures/black_king.png").getImage();
    protected static final Image BlackKnightImage = new ImageIcon("assets/textures/black_knight.png").getImage();
    protected static final Image BlackBishopImage = new ImageIcon("assets/textures/black_bishop.png").getImage();
    protected static final Image BlackRookImage = new ImageIcon("assets/textures/black_rook.png").getImage();
    protected static final Image BlackQueenImage = new ImageIcon("assets/textures/black_queen.png").getImage();

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
        return (this.pieceType & colorMask) == color;
    }

    public int color() {
        return this.pieceType & colorMask;
    }

    public int pieceType() {
        return this.pieceType;
    }

    public boolean isRook() {
        return (this.pieceType & typeMask) == Rook;
    }

    public boolean isBishop() {
        return (this.pieceType & typeMask) == Bishop;
    }

    public boolean isQueen() {
        return (this.pieceType & typeMask) == Queen;
    }

    public boolean isKing() {
        return (this.pieceType & typeMask) == King;
    }

    public boolean isKnight() {
        return (this.pieceType & typeMask) == Knight;
    }

    public boolean isPawn() {
        return (this.pieceType & typeMask) == Pawn;
    }

    public boolean isSlidingPiece() {
        return (this.pieceType & 0b100) == 0b100;
    }

    public String toString() {
        HashMap<Integer, String> asciiChess = new HashMap<>();
        asciiChess.put(White | King, "K");
        asciiChess.put(White | Queen, "Q");
        asciiChess.put(White | Rook, "R");
        asciiChess.put(White | Bishop, "B");
        asciiChess.put(White | Knight, "N");
        asciiChess.put(White | Pawn, "P");
        asciiChess.put(Black | King, "k");
        asciiChess.put(Black | Queen, "q");
        asciiChess.put(Black | Rook, "r");
        asciiChess.put(Black | Bishop, "b");
        asciiChess.put(Black | Knight, "n");
        asciiChess.put(Black | Pawn, "p");
        return asciiChess.get(pieceType);
    }
}
