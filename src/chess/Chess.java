package chess;

import chess.pieces.*;

import java.util.ArrayList;
import java.util.List;

public abstract class Chess {
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

    public static List<Move> generateMoves(Piece piece) {
        Board board = piece.getBoard();
        return new ArrayList<>();
    }

    public static boolean isValidMove(Move move) {
        return true;
    }
}
