package chess;

public class Move {
    final private Board board;
    final private int startSquare;
    final private int targetSquare;

    public Move(Board board, int start, int target) {
        this.board = board;
        this.startSquare = start;
        this.targetSquare = target;
    }

    public Move(Board board, int startX, int startY, int targetX, int targetY) {
        this.board = board;
        this.startSquare = startY * board.getSize() + startX;
        this.targetSquare = targetY * board.getSize() + targetX;
    }

    public int getStartSquare() {
        return startSquare;
    }

    public int getTargetSquare() {
        return targetSquare;
    }

    public Board getBoard() {
        return board;
    }

    public boolean equals(Move m) {
        if (m == null) {
            return false;
        }
        return this.board.equals(m.board) && this.startSquare == m.startSquare && this.targetSquare == m.targetSquare;
    }

    public String toString() {
        return "[" + startSquare + " -> " + targetSquare + "]";
    }
}
