package chess;

public class Move {
    final private Board board;
    final private int startSquare;
    final private int targetSquare;
    final private boolean castle;
    final private int rookPos;
    final private int castleType;
    final private int colorMoving;

    public Move(Board board, int start, int target) {
        this.board = board;
        this.startSquare = start;
        this.targetSquare = target;
        this.castle = false;
        this.rookPos = -1;
        this.castleType = -1;
        this.colorMoving = board.getArrangement()[start].getColor();
    }

    public Move(Board board, int start, int target, int rookPos, int castleType) {
        this.board = board;
        this.startSquare = start;
        this.targetSquare = target;
        this.castle = true;
        this.rookPos = rookPos;
        this.castleType = castleType;
        this.colorMoving = board.getArrangement()[start].getColor();
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

    public int getRookPos() {
        return this.rookPos;
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
        StringBuilder move = new StringBuilder();
        move.append("[").append(startSquare).append("->").append(targetSquare);
        if (castleType == 0) {
            move.append(" KINGSIDE_CASTLE]");
        } else if (castleType == 1) {
            move.append(" QUEENSIDE_CASTLE]");
        } else {
            move.append("]");
        }
        return move.toString();
    }
}
