package chess.pieces;

import chess.Board;

import javax.swing.*;
import java.awt.*;
import java.util.Hashtable;

public abstract class Piece {
    public static final int King = 1;   // 001
    public static final int Pawn = 2;   // 010
    public static final int Knight = 3; // 011
    public static final int Bishop = 5; // 101
    public static final int Rook = 6;   // 110
    public static final int Queen = 7;  // 111

    public static final int White = 8;  // 01000
    public static final int Black = 16; // 10000
    public static final int typeMask = 0b00111;
    public static final int colorMask = 0b11000;

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
    protected int row, col;  // row going downwards from 0 to 7, col going rightwards from 0 to 7
    protected int squareId;

    // Each piece can be represented by CCTTT in binary where CC represents the color and TTT represents the piece type

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

    public int getTypeID() {
        return this.pieceType;
    }

    public void setPiece(int pieceType, int color) {
        this.pieceType = pieceType + color;
    }

    public boolean isColor(int color) {
        return (this.pieceType & colorMask) == color;
    }

    public int color() {
        return this.pieceType & colorMask;
    }

    public int pieceType() {
        return this.pieceType & typeMask;
    }

    public boolean isRookOrQueen() {
        return (this.pieceType & 0b110) == 0b110;
    }

    public boolean isBishopOrQueen() {
        return (this.pieceType & 0b101) == 0b101;
    }

    public boolean isKing() {
        return this.pieceType % 8 == Piece.King;
    }

    public boolean isKnight() {
        return this.pieceType % 8 == Piece.Knight;
    }

    public boolean isPawn() {
        return this.pieceType % 8 == Piece.Pawn;
    }

    public boolean isSlidingPiece() {
        return (this.pieceType & 0b100) != 0;
    }

    public String toString() {
        Hashtable<Integer, String> asciiChess = new Hashtable<>();
        asciiChess.put(White + King, "K");
        asciiChess.put(White + Queen, "Q");
        asciiChess.put(White + Rook, "R");
        asciiChess.put(White + Bishop, "B");
        asciiChess.put(White + Knight, "N");
        asciiChess.put(White + Pawn, "p");
        asciiChess.put(Black + King, "k");
        asciiChess.put(Black + Queen, "q");
        asciiChess.put(Black + Rook, "r");
        asciiChess.put(Black + Bishop, "b");
        asciiChess.put(Black + Knight, "n");
        asciiChess.put(Black + Pawn, "p");
        return asciiChess.get(pieceType);
    }
}
