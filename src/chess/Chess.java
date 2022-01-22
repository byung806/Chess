package chess;

import chess.pieces.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Chess {
    private static final ArrayList<ArrayList<Integer>> ROOK_QUEEN_MOVES = new ArrayList<>();
    private static final ArrayList<ArrayList<Integer>> BISHOP_QUEEN_MOVES = new ArrayList<>();
    private static final ArrayList<ArrayList<Integer>> KING_MOVES = new ArrayList<>();
    private static final ArrayList<ArrayList<Integer>> KNIGHT_MOVES = new ArrayList<>();
    private static final ArrayList<ArrayList<Integer>> WHITE_PAWN_MOVES = new ArrayList<>();
    private static final ArrayList<ArrayList<Integer>> BLACK_PAWN_MOVES = new ArrayList<>();

    public Chess() {
        ROOK_QUEEN_MOVES.add(new ArrayList<>(Arrays.asList(1, 0)));
        ROOK_QUEEN_MOVES.add(new ArrayList<>(Arrays.asList(-1, 0)));
        ROOK_QUEEN_MOVES.add(new ArrayList<>(Arrays.asList(0, 1)));
        ROOK_QUEEN_MOVES.add(new ArrayList<>(Arrays.asList(0, -1)));

        BISHOP_QUEEN_MOVES.add(new ArrayList<>(Arrays.asList(1, 1)));
        BISHOP_QUEEN_MOVES.add(new ArrayList<>(Arrays.asList(1, -1)));
        BISHOP_QUEEN_MOVES.add(new ArrayList<>(Arrays.asList(-1, 1)));
        BISHOP_QUEEN_MOVES.add(new ArrayList<>(Arrays.asList(-1, -1)));

        KING_MOVES.add(new ArrayList<>(Arrays.asList(1, 1)));
        KING_MOVES.add(new ArrayList<>(Arrays.asList(1, 0)));
        KING_MOVES.add(new ArrayList<>(Arrays.asList(1, -1)));
        KING_MOVES.add(new ArrayList<>(Arrays.asList(0, -1)));
        KING_MOVES.add(new ArrayList<>(Arrays.asList(-1, -1)));
        KING_MOVES.add(new ArrayList<>(Arrays.asList(-1, 0)));
        KING_MOVES.add(new ArrayList<>(Arrays.asList(-1, 1)));
        KING_MOVES.add(new ArrayList<>(Arrays.asList(0, 1)));

        KNIGHT_MOVES.add(new ArrayList<>(Arrays.asList(1, 2)));
        KNIGHT_MOVES.add(new ArrayList<>(Arrays.asList(2, 1)));
        KNIGHT_MOVES.add(new ArrayList<>(Arrays.asList(2, -1)));
        KNIGHT_MOVES.add(new ArrayList<>(Arrays.asList(1, -2)));
        KNIGHT_MOVES.add(new ArrayList<>(Arrays.asList(-1, -2)));
        KNIGHT_MOVES.add(new ArrayList<>(Arrays.asList(-2, -1)));
        KNIGHT_MOVES.add(new ArrayList<>(Arrays.asList(-2, 1)));
        KNIGHT_MOVES.add(new ArrayList<>(Arrays.asList(-1, 2)));

        WHITE_PAWN_MOVES.add(new ArrayList<>(Arrays.asList(1, -1)));
        WHITE_PAWN_MOVES.add(new ArrayList<>(Arrays.asList(-1, -1)));
        WHITE_PAWN_MOVES.add(new ArrayList<>(Arrays.asList(0, -1)));
        WHITE_PAWN_MOVES.add(new ArrayList<>(Arrays.asList(0, -2)));

        BLACK_PAWN_MOVES.add(new ArrayList<>(Arrays.asList(1, 1)));
        BLACK_PAWN_MOVES.add(new ArrayList<>(Arrays.asList(-1, 1)));
        BLACK_PAWN_MOVES.add(new ArrayList<>(Arrays.asList(0, 1)));
        BLACK_PAWN_MOVES.add(new ArrayList<>(Arrays.asList(0, 2)));
    }

    public static Piece[] loadFenPosition(String fen, Board cboard) {
        int size = cboard.getSize();
        Piece[] board = new Piece[size * size];
        int counter = 0;
        for (String rank : (fen.split(" ")[0]).split("/")) {
            for (char piece : rank.toCharArray()) {
                if (Character.isDigit(piece)) {
                    for (int tile = 0; tile < Character.getNumericValue(piece); tile++) {
                        board[counter] = null;
                        counter += 1;
                    }
                } else {
                    boolean isWhite = !Character.toString(piece).equals(Character.toString(piece).toLowerCase());
                    piece = Character.toString(piece).toLowerCase().charAt(0);
                    if (piece == 'k') {
                        board[counter] = new King(isWhite, cboard, counter / size, counter % size);
                    } else if (piece == 'p') {
                        board[counter] = new Pawn(isWhite, cboard, counter / size, counter % size);
                    } else if (piece == 'n') {
                        board[counter] = new Knight(isWhite, cboard, counter / size, counter % size);
                    } else if (piece == 'b') {
                        board[counter] = new Bishop(isWhite, cboard, counter / size, counter % size);
                    } else if (piece == 'r') {
                        board[counter] = new Rook(isWhite, cboard, counter / size, counter % size);
                    } else if (piece == 'q') {
                        board[counter] = new Queen(isWhite, cboard, counter / size, counter % size);
                    } else {
                        System.out.println("Something went wrong when loading fen string '" + fen + "' with " + piece);
                    }
                    counter++;
                }
            }
        }
        return board;
    }

    private static List<Move> generateMoves(Piece piece) {
        List<Move> moves = new ArrayList<>();
        Board board = piece.getBoard();
        Piece[] arrangement = board.getArrangement();
        int posX = piece.getCol();
        int posY = piece.getRow();
        int size = board.getSize();
        int pieceColor = piece.color();
        int start = piece.getSquareId();

        if (piece.isSlidingPiece()) {
            ArrayList<ArrayList<Integer>> directions = new ArrayList<>();
            if (piece.isRookOrQueen()) { directions.addAll(ROOK_QUEEN_MOVES); }
            if (piece.isBishopOrQueen()) { directions.addAll(BISHOP_QUEEN_MOVES); }

            for (List<Integer> direction : directions) {
                int x = posX;
                int y = posY;
                while (x >= 0 && x < size && y >= 0 && y < size) {
                    Piece pieceInWay = arrangement[y * size + x];
                    if (pieceInWay != null && pieceInWay != piece) {
                        if (!pieceInWay.isColor(pieceColor)) {
                            moves.add(new Move(board, start, y * size + x));
                        }
                        break;
                    }
                    if (start != y * size + x) {
                        moves.add(new Move(board, start, y * size + x));
                    }
                    x += direction.get(0);
                    y += direction.get(1);
                }
            }
        } else {
            ArrayList<ArrayList<Integer>> directions = new ArrayList<>();
            if (piece.isKing()) {
                // todo: castling
                directions = KING_MOVES;
            } else if (piece.isKnight()) {
                directions = KNIGHT_MOVES;
            } else if (piece.isPawn()) {
                if (pieceColor == Piece.White) {
                    directions = WHITE_PAWN_MOVES;
                } else {
                    directions = BLACK_PAWN_MOVES;
                }
            }
            for (List<Integer> direction : directions) {
                int x = posX + direction.get(0);
                int y = posY + direction.get(1);
                if (x >= 0 && x < size && y >= 0 && y < size) {
                    Piece pieceInWay = arrangement[y * size + x];
                    if (piece.isPawn()) {
                        // todo: add en passant
                        if (direction.get(0) == 1 || direction.get(0) == -1) {
                            if (pieceInWay == null) {
                                continue;
                            }
                        } else if ((pieceInWay != null || (direction.get(1) == -2 && posY != size - 2) || (direction.get(1) == 2 && posY != 1))) {
                            break;
                        }
                    } else {
                        if (pieceInWay != null && pieceInWay.isColor(pieceColor)) {
                            continue;
                        }
                    }
                    moves.add(new Move(board, start, y * size + x));
                }
            }
        }

        return moves;
    }

    public static List<Move> generateAllMoves(Board board) {
        List<Move> moves = new ArrayList<>();
        Piece[] arrangement = board.getArrangement();
        int colorToMove = board.getColorToMove();
        for (int start = 0; start < board.getSize() * board.getSize(); start++) {
            Piece piece = arrangement[start];
            if (piece != null && piece.isColor(colorToMove)) {
                moves.addAll(generateMoves(piece));
            }
        }
        return moves;
    }

    public static boolean isValidMove(Move move) {
        // todo: check and pins
        Piece[] arrangement = move.getBoard().getArrangement();
        Piece piece = arrangement[move.getStartSquare()];
//        int size = move.getBoard().getSize();
        List<Move> moves = piece.isColor(move.getBoard().getColorToMove()) ? generateMoves(piece) : new ArrayList<>();
//        for (Piece p : arrangement) {
//            if (p != null && p.isKing() && p.isColor(piece.color())) { // found same-color king to see if it's pinned
//                for (ArrayList<Integer> direction : KING_MOVES) {
//                    ArrayList<Piece> pinned = new ArrayList<>();
//                    ArrayList<Integer> allCoordsInDirection = new ArrayList<>();
//                    int x = piece.getCol();
//                    int y = piece.getRow();
//                    boolean attackingPiece = false;  // must be true for it to be a pin
//                    while (x >= 0 && x < size && y >= 0 && y < size) {
//                        Piece pieceInWay = arrangement[y*size+x];
//                        if (pieceInWay != null && pieceInWay != piece) {
//                            if (pieceInWay.isColor(piece.color())) {
//                                if (pinned.size() > 0) {
//                                    break;
//                                } else {
//                                    pinned.add(pieceInWay);
//                                }
//                            } else {
//                                if (pieceInWay.isSlidingPiece()) {
//                                    attackingPiece = true;
//                                    break;
//                                }
//                            }
//                        }
//                        x += direction.get(0);
//                        y += direction.get(1);
//                        allCoordsInDirection.add(y*8 + x);
//                    }
//                    if (!attackingPiece) {
//                        pinned.clear();
//                    }
//                    System.out.println(pinned);
//                    if (pinned.size() > 1) {
//                        move.getBoard().addPinnedPiece(pinned.get(0).getSquareId());
//                        for (Move m : moves) {
//                            if (m.getStartSquare() == pinned.get(0).getSquareId()) {
//                                if (!allCoordsInDirection.contains(m.getTargetSquare())) {
//                                    moves.remove(m);
//                                    System.out.println("removed pinned move " + m);
//                                }
//                            }
//                        }
//                    }
//                }
//                break;
//            }
//        }
        return moves.stream().filter(m -> m.equals(move)).toList().size() != 0;
    }
}
