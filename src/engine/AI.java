package engine;

import chess.Board;
import chess.Move;

import java.util.ArrayList;
import java.util.Random;

public class AI {
    private final Board board;
    private ArrayList<Move> moves;

    public AI(Board board) {
        this.board = board;
    }

    public void start() {
        this.moves = board.getMoves();
    }

    private Move getBestMove(ArrayList<Move> moves, Board board, int depth) {
        if (moves.isEmpty()) {
            return null;
        }
        Random r = new Random();
        return moves.get(r.nextInt(moves.size()));
    }

    private int getMoveScore(Move move, Board board) {
        // depth 1
        return -1;
    }
}
