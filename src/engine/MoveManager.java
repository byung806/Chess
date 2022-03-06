package engine;

import chess.Board;
import chess.Move;
import renderer.ChessboardPanel;

import java.util.ArrayList;

public class MoveManager implements Runnable {
    private final Board board;
    private final ChessboardPanel panel;
    private ArrayList<Move> moves;

    public MoveManager(Board board, ChessboardPanel panel) {
        this.board = board;
        this.panel = panel;
    }

    @Override
    public void run() {

    }

    public void playerJustMoved() {
        Thread moveSearcher = new Thread(this::startMoveSearchAndMove);
        moveSearcher.start();
    }

    public void aiJustMoved() {

    }

    private void startMoveSearchAndMove() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.moves = board.getMoves();
        Move bestMove = AI.getBestMove(moves, board, 1);
        board.makeMove(bestMove);
        panel.repaint();
    }
}
