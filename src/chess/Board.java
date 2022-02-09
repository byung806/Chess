package chess;

import chess.pieces.Piece;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Board extends Chess {
    public static Color MOVED_COLOR = new Color(0.188f, 0.670f, 0.556f, 0.8f);
    public static Color SELECTED_COLOR = new Color(0.396f, 0.886f, 0.772f, 0.8f);
    public static Color VALID_MOVES_COLOR = new Color(0.501f, 0.501f, 0.501f, 0.2f);
    public static Color RED = new Color(0.982f, 0.102f, 0.105f, 0.8f);
    public static int KING_SIDE_CASTLE = 0;
    public static int QUEEN_SIDE_CASTLE = 1;

    private final Piece[] arrangement;
    private final ArrayList<Piece> kings = new ArrayList<>();
    private final int size;
    private Piece draggedPiece;
    private Piece selectedPiece;
    private int enPassantSquare;
    private Piece enPassantPieceToBeTaken;
    private HashMap<Integer, ArrayList<Color>> highlightedSquares;
    private ArrayList<Move> moves;
    private boolean whiteKingSideCastle;
    private boolean whiteQueenSideCastle;
    private boolean blackKingSideCastle;
    private boolean blackQueenSideCastle;
    private int colorToMove;
    private int halfMoveClock;
    private int numMoves;
    private String fen;
    private boolean dirty;
    private int screenLength;
    private int screenX;
    private int screenY;

    public Board(String fen) {
        // rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1
        int size = 0;
        colorToMove = fen.split(" ")[1].equals("w") ? 8 : 16;
        for (char c : fen.split(" ")[0].split("/")[0].toCharArray()) {
            size += Character.isDigit(c) ? Character.getNumericValue(c) : 1;
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
        for (Piece piece : arrangement) {
            if (piece != null && piece.isKing()) {
                this.kings.add(piece);
            }
        }

        this.fen = fen;
        this.moves = generateAllMoves(this);
        this.highlightedSquares = new HashMap<>();
        this.enPassantSquare = -1;
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
        highlightedSquares = new HashMap<>();
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
        this.fen = generateFenPosition(this);
        this.moves = generateAllMoves(this);
        if (this.moves.isEmpty()) {
            System.out.println("Checkmate!");
        }
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

    public HashMap<Integer, ArrayList<Color>> getHighlightedSquares() {
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

    public int getEnPassantSquare() {
        return this.enPassantSquare;
    }

    public void setEnPassantSquare(int square) {
        this.enPassantSquare = square;
        if (square / size == 2) {  // black's side
            enPassantPieceToBeTaken = arrangement[square + size];
        } else if (square / size == size - 3) {  // white's side
            enPassantPieceToBeTaken = arrangement[square - size];
        } else {
            enPassantPieceToBeTaken = null;
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

    public ArrayList<Piece> getKings() {
        return this.kings;
    }

    public String toString() {
        return Chess.getPrintableArrangement(this.arrangement);
    }

    public boolean WhiteKingSideCastle() {
        return whiteKingSideCastle;
    }

    public boolean WhiteQueenSideCastle() {
        return whiteQueenSideCastle;
    }

    public boolean BlackKingSideCastle() {
        return blackKingSideCastle;
    }

    public boolean BlackQueenSideCastle() {
        return blackQueenSideCastle;
    }

    public boolean isCurrentValidMove(Move move) {
        return this.moves.stream().filter(m -> m.equals(move)).toList().size() != 0;
    }
}