package chess;

import chess.pieces.Piece;

import java.awt.*;
import java.util.ArrayList;
import java.util.Hashtable;

public class Board extends Chess {
    public static Color MOVED_COLOR = new Color(0.188f, 0.670f, 0.556f, 0.8f);
    public static Color SELECTED_COLOR = new Color(0.396f, 0.886f, 0.772f, 0.8f);
    public static Color RED = new Color(0.982f, 0.102f, 0.105f, 0.8f);

    public static int KING_SIDE_CASTLE = 0;
    public static int QUEEN_SIDE_CASTLE = 1;

    private final String fen;
    private final Piece[] arrangement;
    private final int size;
    private int screenLength;
    private int screenX;
    private int screenY;
    private int colorToMove;
    public boolean whiteKingSideCastle;
    public boolean whiteQueenSideCastle;
    public boolean blackKingSideCastle;
    public boolean blackQueenSideCastle;
    private int halfMoveClock;
    private int numMoves;

    private Hashtable<Integer, ArrayList<Color>> highlightedSquares;
    private boolean dirty;

    private Piece draggedPiece;
    private Piece selectedPiece;

    public Board(String fen) {
        // rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1
        int size = 0;
        colorToMove = fen.split(" ")[1].equals("w") ? 8 : 16;
        for (char c : fen.split(" ")[0].split("/")[0].toCharArray()) {
            if (Character.isDigit(c)) {
                size += Character.getNumericValue(c);
            } else {
                size++;
            }
        }
        this.size = size;
        String castleAvailability = fen.split(" ")[2];
        if (castleAvailability.contains("K")) {
            whiteKingSideCastle = true;
        }
        if (castleAvailability.contains("Q")) {
            whiteQueenSideCastle = true;
        }
        if (castleAvailability.contains("k")) {
            blackKingSideCastle = true;
        }
        if (castleAvailability.contains("q")) {
            blackQueenSideCastle = true;
        }
        this.halfMoveClock = Integer.parseInt(fen.split(" ")[4]);
        this.numMoves = Integer.parseInt(fen.split(" ")[5]);
        this.arrangement = Chess.loadFenPosition(fen, this);
        this.fen = fen;
        this.highlightedSquares = new Hashtable<>();
        this.dirty = true;
    }

    public int getPieceFromScreenCoords(int x, int y) {
        int xCol = (int) Math.floor((float) (x - (screenX - screenLength / 2)) / ((float) screenLength / size));
        int yCol = (int) Math.floor((float) (y - (screenY - screenLength / 2)) / ((float) screenLength / size));
        if (xCol < 0 || xCol >= size || yCol < 0 || yCol >= size) {
            return -1;
        }
        return yCol * size + xCol;
    }

    public void addHighlightedSquare(int squareId, Color color) {
        ArrayList<Color> colors = highlightedSquares.get(squareId);
        if (colors == null) {
            colors = new ArrayList<>();
        }
        colors.add(color);
        highlightedSquares.put(squareId, colors);
        dirty = true;
    }

    public void removeHighlightedSquare(int squareId) {
        ArrayList<Color> hlSquares = highlightedSquares.get(squareId);
        hlSquares.remove(hlSquares.size() - 1);
        dirty = true;
    }

    public void clearHighlightedSquares() {
        highlightedSquares = new Hashtable<>();
    }

    public void executeMove(Move move) {
        // doesn't check for valid moves so a valid move should be passed in
        int start = move.getStartSquare();
        Piece toMove = arrangement[start];
        if (toMove.isColor(Piece.Black)) {
            incrementNumMoves();
        }
        if (move.isCastle()) {
            if (move.getCastleType() == KING_SIDE_CASTLE && move.getColor() == Piece.White) {
                whiteKingSideCastle = false;
            } else if (move.getCastleType() == KING_SIDE_CASTLE && move.getColor() == Piece.Black) {
                blackKingSideCastle = false;
            } else if (move.getCastleType() == QUEEN_SIDE_CASTLE && move.getColor() == Piece.White) {
                whiteQueenSideCastle = false;
            } else if (move.getCastleType() == QUEEN_SIDE_CASTLE && move.getColor() == Piece.Black) {
                blackQueenSideCastle = false;
            }
        }
        toMove.setSquareId(move.getTargetSquare());
        arrangement[start] = null;
        arrangement[move.getTargetSquare()] = toMove;
        this.colorToMove = this.colorToMove == Piece.White ? Piece.Black : Piece.White;
        this.dirty = true;
    }

    public boolean isDirty() {
        return this.dirty;
    }

    public void setClean() {
        this.dirty = false;
    }

    public int getSize() {
        return this.size;
    }

    public int getScreenLength() {
        return this.screenLength;
    }

    public void setScreenLength(int screenLength) {
        dirty = true;
        if (screenLength != this.screenLength) {
            this.screenLength = screenLength;
        }
    }

    public Hashtable<Integer, ArrayList<Color>> getHighlightedSquares() {
        return this.highlightedSquares;
    }

    public String getFenPosition() {
        return this.fen;
    }

    public Piece[] getArrangement() {
        return arrangement;
    }

    public int getColorToMove() {
        return colorToMove;
    }

    public void setColorToMove(int colorToMove) {
        this.colorToMove = colorToMove;
    }

    public int getHalfMoveClock() {
        return halfMoveClock;
    }

    public void incrementHalfMoveClock() {
        this.halfMoveClock++;
    }

    public int getNumMoves() {
        return numMoves;
    }

    public void incrementNumMoves() {
        this.numMoves++;
    }

    public int getScreenX() {
        return screenX;
    }

    public void setScreenX(int screenX) {
        if (screenX != this.screenX) {
            this.screenX = screenX;
            dirty = true;
        }
    }

    public int getScreenY() {
        return screenY;
    }

    public void setScreenY(int screenY) {
        if (screenY != this.screenY) {
            this.screenY = screenY;
            dirty = true;
        }
    }

    public Piece getDraggedPiece() {
        return this.draggedPiece;
    }

    public void setDraggedPiece(Piece piece) {
        this.draggedPiece = piece;
        dirty = true;
    }

    public Piece getSelectedPiece() {
        return this.selectedPiece;
    }

    public void setSelectedPiece(Piece piece) {
        this.selectedPiece = piece;
        dirty = true;
    }

    public String toString() {
        StringBuilder printOut = new StringBuilder();
        for (int y = 0; y < size; y++) {
            printOut.append("| ");
            for (int x = 0; x < size; x++) {
                Piece piece = arrangement[y * size + x];
                printOut.append(piece != null ? piece : " ");
                if (x != size - 1) {
                    printOut.append(" ");
                }
            }
            printOut.append(y != size - 1 ? " |\n" : " |");
        }
        return printOut.toString();
    }
}