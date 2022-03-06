package chess;

import audio.Sound;
import chess.pieces.Piece;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Board extends Chess {
    public static Color MOVED_COLOR = new Color(0.188f, 0.670f, 0.556f, 0.8f);
    public static Color SELECTED_COLOR = new Color(0.396f, 0.886f, 0.772f, 0.8f);
    public static Color VALID_MOVES_COLOR = new Color(0.501f, 0.501f, 0.501f, 0.2f);
    public static Color RED = new Color(0.982f, 0.102f, 0.105f, 0.8f);
    public static int KING_SIDE_CASTLE = 0;
    public static int QUEEN_SIDE_CASTLE = 1;
    public static int PLAYER_VS_PLAYER = 0;
    public static int PLAYER_VS_COMPUTER = 1;
    private final Piece[] arrangement;
    private final int size;
    private final int playAs;
    private final int mode;
    private Sound soundManager;
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

    public Board(String fen, int playAs, int mode) {
        // rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1
        String[] splitFen = fen.split(" ");
        colorToMove = splitFen[1].equals("w") ? 8 : 16;

        int size = 0;
        for (char c : splitFen[0].split("/")[0].toCharArray()) {
            size += Character.isDigit(c) ? Character.getNumericValue(c) : 1;
        }
        this.size = size;
        String castleAvailability = fen.split(" ")[2];
        if (castleAvailability.contains("K")) whiteKingSideCastle = true;
        if (castleAvailability.contains("Q")) whiteQueenSideCastle = true;
        if (castleAvailability.contains("k")) blackKingSideCastle = true;
        if (castleAvailability.contains("q")) blackQueenSideCastle = true;
        this.halfMoveClock = Integer.parseInt(fen.split(" ")[4]);
        this.numMoves = Integer.parseInt(fen.split(" ")[5]);
        this.arrangement = loadFenPosition(fen, this);

        try {
            soundManager = new Sound();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Could not load some sound assets.");
        }

        this.playAs = playAs;
        this.mode = mode;
        this.fen = fen;
        this.moves = generateAllMoves(this);
        this.highlightedSquares = new HashMap<>();
        this.enPassantSquare = -1;
        this.dirty = true;
    }

//    public Board(Board board) {
//        this.arrangement = board.arrangement; // todo: implement deep instaed of shallow
//        this.size = board.size;
//        this.playAs = board.playAs;
//        this.soundManager = board.soundManager;
//        this.draggedPiece = board.draggedPiece;
//    }

    public void makeMove(Move move) {
        // doesn't check for valid moves so a valid move should be passed in
        // todo: disable castling if rook moves
        int start = move.getStartSquare();
        int target = move.getTargetSquare();
        clearHighlightedSquares();
        addHighlightedSquare(start, Board.MOVED_COLOR);
        addHighlightedSquare(target, Board.MOVED_COLOR);
        Piece toMove = arrangement[start];
        if (toMove.isColor(Piece.BLACK)) incrementNumMoves();
        if (move.isCastle()) {
            if (toMove.isColor(Piece.WHITE)) {
                whiteKingSideCastle = false;
                whiteQueenSideCastle = false;
            } else {
                blackKingSideCastle = false;
                blackQueenSideCastle = false;
            }
            Piece rook = arrangement[move.getRookPos()];
            rook.setSquareId(target + (move.getCastleType() == KING_SIDE_CASTLE ? -1 : 1));
            arrangement[move.getRookPos()] = null;
            arrangement[target + (move.getCastleType() == KING_SIDE_CASTLE ? -1 : 1)] = rook;
        }
        arrangement[start] = null;
        arrangement[target] = toMove;
        toMove.setSquareId(target);
        this.colorToMove = this.colorToMove == Piece.WHITE ? Piece.BLACK : Piece.WHITE;
        this.fen = generateFenPosition(this);
        this.moves = generateAllMoves(this);
        if (this.moves.isEmpty()) {
            if (kingInCheck(this.colorToMove, arrangement)) {
                System.out.println("Checkmate!");
            } else {
                System.out.println("Stalemate!");
            }
        }
        this.dirty = true;
    }

    public int getPieceFromScreenCoords(int x, int y) {
        int xCol = (int) Math.floor((float) (x - (screenX - screenLength / 2)) / ((float) screenLength / size));
        int yCol = (int) Math.floor((float) (y - (screenY - screenLength / 2)) / ((float) screenLength / size));
        if (xCol < 0 || xCol >= size || yCol < 0 || yCol >= size) {
            return -1;
        }
        return this.playAs == Piece.WHITE ? yCol * size + xCol : size * size - (yCol * size + xCol) - 1;
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
        if (!highlightedSquares.containsKey(squareId)) {
            return;
        }
        ArrayList<Color> hlSquares = highlightedSquares.get(squareId);
        hlSquares.remove(hlSquares.size() - 1);
        dirty = true;
    }

    public void clearHighlightedSquares() {
        highlightedSquares = new HashMap<>();
    }

    public boolean containsHighlightedSquare(int square, Color color) {
        return highlightedSquares.containsKey(square) && highlightedSquares.get(square).contains(color);
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

    public int getPlayAs() {
        return this.playAs;
    }

    public String toString() {
        return getPrintableArrangement(this.arrangement);
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

    public ArrayList<Move> getMoves() {
        return this.moves;
    }

    public int getMode() {
        return this.mode;
    }

    public boolean isCurrentValidMove(Move move) {
        return findMatchInValidMoves(move) != null;
    }

    public Move findMatchInValidMoves(Move move) {
        List<Move> matches = this.moves.stream().filter(m -> m.equals(move)).toList();
        return matches.size() == 0 ? null : matches.get(0);
    }
}