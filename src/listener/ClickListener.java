package listener;

import chess.Board;
import chess.Move;
import chess.pieces.Piece;
import renderer.ChessboardPanel;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ClickListener extends MouseAdapter {
    int mouseClicks;
    Board board;
    ChessboardPanel panel;

    public ClickListener(Board board) {
        mouseClicks = 0;
        this.board = board;
    }

    public void setPanel(ChessboardPanel panel) {
        this.panel = panel;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        mouseClicks++;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // todo: pre-moves and disable both sides playing
        int squareId = board.getPieceFromScreenCoords(e.getX(), e.getY());
        Piece piece = squareId != -1 ? board.getArrangement()[squareId] : null;
        if (e.getButton() == 3) {
            // todo: add arrows and square highlighting
        } else if (e.getButton() == 1) {
            if (board.getSelectedPiece() != null) {
                int start = board.getSelectedPiece().getSquareId();
                Move move = board.findMatchInValidMoves(new Move(board, start, squareId));
                if (move != null) {
                    // make move by clicking square (without dragging)
                    board.clearHighlightedSquares();
                    board.addHighlightedSquare(start, Board.MOVED_COLOR);
                    board.addHighlightedSquare(squareId, Board.MOVED_COLOR);
                    board.executeMove(move);
                    board.setSelectedPiece(null);
                    panel.repaint();
                    return;
                } else {
                    // non valid move
                    if (start == squareId && !board.containsHighlightedSquare(squareId, Board.SELECTED_COLOR)) {
                        board.addHighlightedSquare(squareId, Board.SELECTED_COLOR);
                    } else {
                        board.removeHighlightedSquare(start);
                        if (start != squareId && piece != null) {
                            board.addHighlightedSquare(squareId, Board.SELECTED_COLOR);
                        } else {
                            board.setSelectedPiece(null);
                            board.setDraggedPiece(piece);
                            panel.repaint();
                            return;
                        }
                    }
                }
            } else if (piece != null) {
                board.addHighlightedSquare(squareId, Board.SELECTED_COLOR);
            }
            board.setSelectedPiece(piece);
            board.setDraggedPiece(piece);
        }
        panel.repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
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
