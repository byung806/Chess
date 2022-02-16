package listener;

import chess.Board;
import chess.Move;
import chess.pieces.Piece;
import renderer.BoardPanel;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ClickListener extends MouseAdapter {
    boolean dragging;
    int mouseClicks;
    Board board;
    BoardPanel panel;

    public ClickListener(Board board) {
        dragging = false;
        mouseClicks = 0;
        this.board = board;
    }

    public void setPanel(BoardPanel panel) {
        this.panel = panel;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        mouseClicks++;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        this.dragging = true;
        int squareId = board.getPieceFromScreenCoords(e.getX(), e.getY());
        Piece piece = squareId != -1 ? board.getArrangement()[squareId] : null;
        if (e.getButton() == 3) {
            // todo: add arrows and square highlighting
        } else if (e.getButton() == 1) {
            if (board.getSelectedPiece() != null) {
                int start = board.getSelectedPiece().getSquareId();
                Move move = board.findMatchInValidMoves(new Move(board, start, squareId));
                if (move != null) {
                    board.clearHighlightedSquares();
                    board.addHighlightedSquare(start, Board.MOVED_COLOR);
                    board.addHighlightedSquare(squareId, Board.MOVED_COLOR);
                    board.executeMove(move);
                    board.setSelectedPiece(null);
                    panel.repaint();
                    return;
                } else {
                    board.removeHighlightedSquare(start);
                    if (start == squareId) {
                        piece = null;
                    }
                }
            }
            board.setSelectedPiece(piece);
            board.setDraggedPiece(piece);
            if (piece != null) {
                board.addHighlightedSquare(squareId, Board.SELECTED_COLOR);
            }
        }
        panel.repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        this.dragging = false;
        int squareId = board.getPieceFromScreenCoords(e.getX(), e.getY());
        Piece draggedPiece = board.getDraggedPiece();
        if (draggedPiece != null) {
            int start = draggedPiece.getSquareId();
            Move move = squareId != -1 && squareId != start ? new Move(board, start, squareId) : null;
            if (move != null && board.isCurrentValidMove(move)) {
                board.clearHighlightedSquares();
                board.addHighlightedSquare(start, Board.MOVED_COLOR);
                board.addHighlightedSquare(squareId, Board.MOVED_COLOR);
                board.executeMove(board.findMatchInValidMoves(move));
                board.setSelectedPiece(null);
            }
        }
        board.setDraggedPiece(null);
        panel.repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}
