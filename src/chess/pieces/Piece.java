package chess.pieces;

import chess.Board;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

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

    protected static Image WhitePawnImage, WhiteKnightImage, WhiteBishopImage, WhiteRookImage, WhiteQueenImage, WhiteKingImage;
    protected static Image BlackPawnImage, BlackKnightImage, BlackBishopImage, BlackRookImage, BlackQueenImage, BlackKingImage;

    protected int pieceType;
    protected Image image;
    protected Board board;
    protected int row, col;  // row going downwards from 0 to 7, col going rightwards from 0 to 7
    protected int squareId;

    // Each piece can be represented by CCTTT in binary where CC represents the color and TTT represents the piece type

    protected Piece() {
        try {
            BufferedImage spriteSheet = ImageIO.read(new File("assets/textures/pieces.png"));
            WhiteKingImage = spriteSheet.getSubimage(0, 0, 200, 200);
            WhiteQueenImage = spriteSheet.getSubimage(200, 0, 200, 200);
            WhiteBishopImage = spriteSheet.getSubimage(400, 0, 200, 200);
            WhiteKnightImage = spriteSheet.getSubimage(600, 0, 200, 200);
            WhiteRookImage = spriteSheet.getSubimage(800, 0, 200, 200);
            WhitePawnImage = spriteSheet.getSubimage(1000, 0, 200, 200);

            BlackKingImage = spriteSheet.getSubimage(0, 200, 200, 200);
            BlackQueenImage = spriteSheet.getSubimage(200, 200, 200, 200);
            BlackBishopImage = spriteSheet.getSubimage(400, 200, 200, 200);
            BlackKnightImage = spriteSheet.getSubimage(600, 200, 200, 200);
            BlackRookImage = spriteSheet.getSubimage(800, 200, 200, 200);
            BlackPawnImage = spriteSheet.getSubimage(1000, 200, 200, 200);
        } catch (IOException e) {
            System.out.println("Pieces didn't load :(");
            e.printStackTrace();
        }
    }

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
        return this.pieceType % 0b1000 == Piece.King;
    }

    public boolean isKnight() {
        return this.pieceType % 0b1000 == Piece.Knight;
    }

    public boolean isPawn() {
        return this.pieceType % 0b1000 == Piece.Pawn;
    }

    public boolean isSlidingPiece() {
        return (this.pieceType & 0b100) != 0;
    }

    public String toString() {
        HashMap<Integer, String> asciiChess = new HashMap<>();
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
