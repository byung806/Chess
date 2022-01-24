package chess;

import chess.pieces.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Chess {
    private static final int[][] ROOK_MOVES = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
    private static final int[][] BISHOP_MOVES = {{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
    private static final int[][] QUEEN_MOVES = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
    private static final int[][] KING_MOVES = {{1, 1}, {1, 0}, {1, -1}, {0, -1}, {-1, -1}, {-1, 0}, {-1, 1}, {0, 1}};
    private static final int[][] KNIGHT_MOVES = {{1, 2}, {2, 1}, {2, -1}, {1, -2}, {-1, -2}, {-2, -1}, {-2, 1}, {-1, 2}};
    private static final int[][] WHITE_PAWN_MOVES = {{1, -1}, {-1, -1}, {0, -1}, {0, -2}};
    private static final int[][] BLACK_PAWN_MOVES = {{1, 1}, {-1, 1}, {0, 1}, {0, 2}};

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

        int[][] directions = {};
        if (piece.isSlidingPiece()) {
            if (piece.isRook()) {
                directions = ROOK_MOVES;
            } else if (piece.isBishop()) {
                directions = BISHOP_MOVES;
            } else if (piece.isQueen()) {
                directions = QUEEN_MOVES;
            }
            for (int[] direction : directions) {
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
                    x += direction[0];
                    y += direction[1];
                }
            }
        } else {
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
            for (int[] direction : directions) {
                int x = posX + direction[0];
                int y = posY + direction[1];
                if (x >= 0 && x < size && y >= 0 && y < size) {
                    Piece pieceInWay = arrangement[y * size + x];
                    if (piece.isPawn()) {
                        // todo: add en passant
                        if (direction[0] == 1 || direction[0] == -1) {
                            if (pieceInWay == null) {
                                continue;
                            }
                        } else if ((pieceInWay != null || (direction[1] == -2 && posY != size - 2) || (direction[1] == 2 && posY != 1))) {
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

        for (int i = moves.size() - 1; i >= 0; i--) {
            Move move = moves.get(i);
            Piece[] testArrangement = arrangement.clone();
            Piece movingPiece = testArrangement[move.getStartSquare()];
            testArrangement[move.getStartSquare()] = null;
            testArrangement[move.getTargetSquare()] = movingPiece;
            outerLoop:
            for (Piece king : board.getKings()) {
                int originX = king.getCol();
                int originY = king.getRow();
                if (king.isColor(pieceColor)) {
                    for (int[] dir : QUEEN_MOVES) {
                        int x = originX;
                        int y = originY;
                        while (x >= 0 && x < size && y >= 0 && y < size) {
                            Piece pieceInWay = testArrangement[y * 8 + x];
                            if (pieceInWay != null && pieceInWay != king) {
                                if (((Arrays.stream(ROOK_MOVES).anyMatch(e -> Arrays.equals(e, dir)) && pieceInWay.isRook())
                                        || (Arrays.stream(BISHOP_MOVES).anyMatch(e -> Arrays.equals(e, dir)) && pieceInWay.isBishop())
                                        || pieceInWay.isQueen()) && !pieceInWay.isColor(king.color())) {
                                    moves.remove(move);
                                    break outerLoop;
                                }
                                break;
                            }
                            x += dir[0];
                            y += dir[1];
                        }
                    }
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
        // todo: check
        Piece[] arrangement = move.getBoard().getArrangement();
        Piece piece = arrangement[move.getStartSquare()];
        List<Move> moves = piece.isColor(move.getBoard().getColorToMove()) ? generateMoves(piece) : new ArrayList<>();
        return moves.stream().filter(m -> m.equals(move)).toList().size() != 0;
    }

    public static String getPrintableArrangement(Piece[] arrangement) {
        int size = (int) Math.sqrt(arrangement.length);
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
