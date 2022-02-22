package engine;

import chess.Board;
import chess.Move;
import chess.pieces.Piece;

import java.util.ArrayList;

public abstract class AI {
    public static Move getBestMove(ArrayList<Move> moves, Board board, int depth) {
        for (Move move : moves) {
            for (int i = 0; i < depth; i++) {

            }
        }
        return moves.get(0); // temporary
    }
}
