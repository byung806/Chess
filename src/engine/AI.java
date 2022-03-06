package engine;

import chess.Board;
import chess.Move;

import java.util.ArrayList;
import java.util.Random;

public abstract class AI {
    public static Move getBestMove(ArrayList<Move> moves, Board board, int depth) {
        // assumes list of moves is not empty
        Random r = new Random();
        return moves.get(r.nextInt(moves.size()));
    }

    public static int getMoveScore(Move move, Board board) {
        // depth 1
        return -1;
    }
}
