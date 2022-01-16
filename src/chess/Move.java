package chess;

public class Move {
    final private Board board;
    final private int startSquare;
    final private int targetSquare;
    final private boolean castle;
    final private int castleType;
    final private int colorMoving;

    public Move(Board board, int start, int target) {
        this.board = board;
        this.startSquare = start;
        this.targetSquare = target;
        this.castle = false;
        this.castleType = -1;
        this.colorMoving = board.getArrangement()[start].color();
    }

    public Move(Board board, int start, int target, int castleType) {
        this.board = board;
        this.startSquare = start;
        this.targetSquare = target;
        this.castle = true;
        this.castleType = castleType;
        this.colorMoving = board.getArrangement()[start].color();
    }

    public int getStartSquare() {
        return startSquare;
    }

    public int getTargetSquare() {
        return targetSquare;
    }

    public int getColor() {
        return colorMoving;
    }

    public Board getBoard() {
        return board;
    }

    public boolean isCastle() {
        return this.castle;
    }

    public int getCastleType() {
        return this.castleType;
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
