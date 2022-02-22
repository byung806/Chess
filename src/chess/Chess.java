package chess;

import chess.pieces.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static chess.pieces.Piece.*;

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
                    int color = !Character.toString(piece).equals(Character.toString(piece).toLowerCase()) ? WHITE : BLACK;
                    piece = Character.toString(piece).toLowerCase().charAt(0);
                    if (piece == 'k') {
                        board[counter] = new King(color, cboard, counter / size, counter % size);
                    } else if (piece == 'p') {
                        board[counter] = new Pawn(color, cboard, counter / size, counter % size);
                    } else if (piece == 'n') {
                        board[counter] = new Knight(color, cboard, counter / size, counter % size);
                    } else if (piece == 'b') {
                        board[counter] = new Bishop(color, cboard, counter / size, counter % size);
                    } else if (piece == 'r') {
                        board[counter] = new Rook(color, cboard, counter / size, counter % size);
                    } else if (piece == 'q') {
                        board[counter] = new Queen(color, cboard, counter / size, counter % size);
                    } else {
                        System.out.println("Something went wrong when loading fen string '" + fen + "' with " + piece);
                    }
                    counter++;
                }
            }
        }
        return board;
    }

    public static String generateFenPosition(Board board) {
        Piece[] arrangement = board.getArrangement();
        StringBuilder fen = new StringBuilder();
        int size = board.getSize();
        int emptyCount = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (arrangement[i * 8 + j] == null) {
                    emptyCount++;
                } else {
                    fen.append(emptyCount != 0 ? emptyCount : "");
                    fen.append(arrangement[i * 8 + j]);
                    emptyCount = 0;
                }
            }
            fen.append(emptyCount != 0 ? emptyCount : "");
            emptyCount = 0;
            if (i != size - 1) {
                fen.append("/");
            }
        }
        fen.append(board.getColorToMove() == Piece.WHITE ? " w " : " b ");
        fen.append(board.WhiteKingSideCastle() ? "K" : "").append(board.WhiteQueenSideCastle() ? "Q" : "");
        fen.append(board.BlackKingSideCastle() ? "k" : "").append(board.BlackQueenSideCastle() ? "q " : " ");
        fen.append(board.getEnPassantSquare() != -1 ? squareToAlgebraicNotation(board.getEnPassantSquare(), board.getSize()) : "-");
        fen.append(" ").append(board.getHalfMoveClock()).append(" ").append(board.getNumMoves());
        return fen.toString();
    }

    public static String squareToAlgebraicNotation(int squareId, int size) {
        int x = squareId % size;  // letter
        int y = size - (squareId / size);  // number
        return xToPrintableLetters(x) + y;
    }

    public static String xToPrintableLetters(int x) {
        StringBuilder letters = new StringBuilder();
        while (x > 0) {
            letters.append((char) (x % 26 + 96));
            x = x / 26;
        }
        letters.reverse();
        return letters.toString();
    }

    private static ArrayList<Move> generateMoves(Piece piece) {
        // todo: en passant
        ArrayList<Move> moves = new ArrayList<>();
        Board board = piece.getBoard();
        Piece[] arrangement = board.getArrangement();
        int posX = piece.getCol();
        int posY = piece.getRow();
        int sqId = piece.getSquareId();
        int size = board.getSize();
        int pieceColor = piece.color();
        int start = piece.getSquareId();

        int[][] directions = switch (piece.pieceType() & TYPE_MASK) {
            case ROOK -> ROOK_MOVES;
            case BISHOP -> BISHOP_MOVES;
            case QUEEN -> QUEEN_MOVES;
            case KING -> KING_MOVES;
            case KNIGHT -> KNIGHT_MOVES;
            case PAWN -> pieceColor == WHITE ? WHITE_PAWN_MOVES : BLACK_PAWN_MOVES;
            default -> new int[][]{};
        };

        if (piece.isKing()) {
            // castling
            ArrayList<Integer> castleDirections = new ArrayList<>();
            if (board.WhiteKingSideCastle() || board.BlackKingSideCastle()) castleDirections.add(1);
            if (board.WhiteQueenSideCastle() || board.BlackQueenSideCastle()) castleDirections.add(-1);
            for (int dir : castleDirections) {
                for (int x = posX; x < size; x += dir) {
                    Piece pieceInWay = arrangement[posY * size + x];
                    boolean squareAttacked = squareIsAttacked(posY * size + x, piece.color(), arrangement);
                    if ((squareAttacked || pieceInWay != null)) {
                        if (pieceInWay != piece) {
                            Piece p = arrangement[posY * size + x];
                            if (p != null && p.pieceType() == (piece.color() | Piece.ROOK) && Math.abs(x - posX) > 2) {
                                int rookPos = posY * size + x;
                                moves.add(new Move(board, sqId, sqId + dir * 2, rookPos, dir == 1 ? Board.KING_SIDE_CASTLE : Board.QUEEN_SIDE_CASTLE));
                            }
                            break;
                        } else if (squareAttacked) {
                            break;
                        }
                    }
                }
            }
        }

        directionLoop:
        for (int[] direction : directions) {
            int x = posX;
            int y = posY;
            while (x >= 0 && x < size && y >= 0 && y < size) {
                Piece pieceInWay = arrangement[y * size + x];
                if (pieceInWay != piece) {
                    if (pieceInWay != null) {
                        if (!pieceInWay.isColor(pieceColor) && ((piece.isPawn() && direction[0] != 0) || !piece.isPawn())) {
                            // if piece in way of sliding piece or pawn has a piece to capture or piece is normal piece
                            moves.add(new Move(board, start, y * size + x));
                        } else {  // piece is same color or move is normal pawn move or piece is not pawn
                            if (piece.isPawn() && direction[0] == 0) {  // piece in way of pawn
                                break directionLoop;
                            }
                        }
                        break;
                    } else {  // no piece in way
                        if (piece.isPawn() && direction[0] != 0) {  // pawn finds no piece to capture
                            break;
                        }
                        moves.add(new Move(board, start, y * size + x));
                    }
                    if (!piece.isSlidingPiece()) {
                        break;
                    }
                }
                x += direction[0];
                y += direction[1];
            }
        }
        for (int i = moves.size() - 1; i >= 0; i--) {
            Move move = moves.get(i);
            Piece[] testArrangement = arrangement.clone();
            Piece movingPiece = testArrangement[move.getStartSquare()];
            testArrangement[move.getStartSquare()] = null;
            testArrangement[move.getTargetSquare()] = movingPiece;
            for (int sq = 0; sq < testArrangement.length; sq++) {
                Piece king = testArrangement[sq];
                if (king != null && king.isKing() && king.isColor(pieceColor)) {
                    if (squareIsAttacked(sq, pieceColor, testArrangement)) {
                        moves.remove(move);
                    }
                }
            }
        }
        return moves;
    }

    public static boolean squareIsAttacked(int squareId, int originSquarePieceColor, Piece[] arrangement) {
        int size = (int) Math.ceil(Math.sqrt(arrangement.length));
        Piece piece = arrangement[squareId];
        int originX = squareId % size;
        int originY = squareId / size;
        for (int[] dir : QUEEN_MOVES) {
            int x = originX;
            int y = originY;
            while (x >= 0 && x < size && y >= 0 && y < size) {
                Piece pieceInWay = arrangement[y * size + x];
                if (pieceInWay != null && pieceInWay != piece) {
                    if (((Arrays.stream(ROOK_MOVES).anyMatch(e -> Arrays.equals(e, dir)) && pieceInWay.isRook())
                            || (Arrays.stream(BISHOP_MOVES).anyMatch(e -> Arrays.equals(e, dir)) && pieceInWay.isBishop())
                            || pieceInWay.isQueen()) && !pieceInWay.isColor(originSquarePieceColor)) {
                        return true;
                    }
                    break;
                }
                x += dir[0];
                y += dir[1];
            }
        }
        int[][] pawnAttacks;
        if (originSquarePieceColor == Piece.WHITE) {
            pawnAttacks = new int[][]{{1, -1}, {-1, -1}};
        } else {
            pawnAttacks = new int[][]{{1, 1}, {-1, 1}};
        }
        for (int[] dir : KNIGHT_MOVES) {
            int x = originX + dir[0];
            int y = originY + dir[1];
            if (x >= 0 && x < size && y >= 0 && y < size) {
                Piece inWay = arrangement[y * size + x];
                if (inWay != null && inWay.isKnight() && !inWay.isColor(originSquarePieceColor)) {
                    return true;
                }
            }
        }
        for (int[] dir : pawnAttacks) {
            int x = originX + dir[0];
            int y = originY + dir[1];
            if (x >= 0 && x < size && y >= 0 && y < size) {
                Piece inWay = arrangement[y * size + x];
                if (inWay != null && inWay.isPawn() && !inWay.isColor(originSquarePieceColor)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean kingInCheck(int color, Piece[] arrangement) {
        for (int sq = 0; sq < arrangement.length; sq++) {
            Piece king = arrangement[sq];
            if (king != null && king.isKing() && king.isColor(color)) {
                if (squareIsAttacked(sq, color, arrangement)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static ArrayList<Move> generateAllMoves(Board board) {
        ArrayList<Move> moves = new ArrayList<>();
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

    public static boolean isValidMove(Move move, Piece[] arrangement) {
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
                printOut.append(piece != null ? piece : ".");
                if (x != size - 1) {
                    printOut.append(" ");
                }
            }
            printOut.append(y != size - 1 ? " |\n" : " |");
        }
        return printOut.toString();
    }
}
