package chess;

public class Move {
    public static final int DEFAULT = 0;
    public static final int EN_PASSANT = 1;
    public static final int DOUBLE_PAWN = 2;
    public static final int KING_SIDE_CASTLE = 3;
    public static final int QUEEN_SIDE_CASTLE = 4;

    final private Board board;
    final private int startSquare;
    final private int targetSquare;
    final private int rookPos;
    final private int castleType;
    final private int doublePawnMoveMiddle;
    final private int enPassantSquare;
    final private int colorMoving;

    public Move(Board board, int start, int target, int type, int rookPosOrEnPassantSquare) {
        this.board = board;
        this.startSquare = start;
        this.targetSquare = target;
        if (type == KING_SIDE_CASTLE || type == QUEEN_SIDE_CASTLE) {
            this.castleType = type;
            this.rookPos = rookPosOrEnPassantSquare;
        } else {
            this.castleType = -1;
            this.rookPos = -1;
        }
        if (type == EN_PASSANT) {
            this.enPassantSquare = rookPosOrEnPassantSquare;
        } else {
            this.enPassantSquare = -1;
        }
        if (type == DOUBLE_PAWN) {
            this.doublePawnMoveMiddle = (start + target) / 2;
        } else {
            this.doublePawnMoveMiddle = -1;
        }
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

    public boolean isEnPassant() {
        return this.enPassantSquare != -1;
    }

    public int getEnPassantSquare() {
        return this.enPassantSquare;
    }

    public boolean isDoublePawnMove() {
        return this.doublePawnMoveMiddle != -1;
    }

    public int getDoublePawnMiddle() {
        return this.doublePawnMoveMiddle;
    }

    public boolean isCastle() {
        return this.castleType != -1;
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
            move.append(" KINGSIDE_CASTLE");
        } else if (castleType == 1) {
            move.append(" QUEENSIDE_CASTLE");
        } else if (enPassantSquare != -1) {
            move.append(" EN_PASSANT ").append(enPassantSquare);
        }
        move.append("]");
        return move.toString();
    }
}
