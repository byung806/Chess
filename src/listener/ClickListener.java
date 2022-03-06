package listener;

import chess.Board;
import chess.Move;
import chess.pieces.Piece;
import engine.MoveManager;
import renderer.ChessboardPanel;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static chess.Board.PLAYER_VS_COMPUTER;
import static chess.Board.SELECTED_COLOR;

public class ClickListener extends MouseAdapter {
    private final Board board;
    private final MoveManager moveManager;
    private final ChessboardPanel panel;
    private int mouseClicks;

    public ClickListener(Board board, ChessboardPanel panel) {
        this.mouseClicks = 0;
        this.board = board;
        this.panel = panel;
        if (board.getMode() == PLAYER_VS_COMPUTER) {
            this.moveManager = new MoveManager(board, panel);
        } else {
            this.moveManager = null;
        }
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
            Piece selected = board.getSelectedPiece();
            if (selected == null) {
                //no possibility of moving
                if (piece != null && piece.isColor(board.getColorToMove())) {
                    // start dragging and select drag square if piece is right color
                    board.setDraggedPiece(piece);
                    board.setSelectedPiece(piece);
                    board.addHighlightedSquare(squareId, Board.SELECTED_COLOR);
                }
            } else {
                Move move = new Move(board, selected.getSquareId(), squareId);
                if (board.isCurrentValidMove(move)) {
                    // if click to move is valid
                    makeMove(move, board);
                } else {
                    board.setSelectedPiece(null);
                    board.removeHighlightedSquare(selected.getSquareId());
                    if (piece != null) {
                        if (piece.isColor(board.getColorToMove())) {
                            if (selected.getSquareId() != squareId) {
                                // select new piece if another same-color piece is clicked (and deselect old)
                                board.setSelectedPiece(piece);
                                board.addHighlightedSquare(squareId, SELECTED_COLOR);
                            }
                            board.setDraggedPiece(piece);
                        }
                    } else {
                        // if random non-piece square is clicked
                        board.removeHighlightedSquare(squareId);
                    }
                }
            }
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
                makeMove(board.findMatchInValidMoves(move), board);
            }
        }
        board.setDraggedPiece(null);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    public void makeMove(Move move, Board board) {
        //todo: repaint only part of screen
        board.makeMove(move);
        board.setSelectedPiece(null);
        board.setDraggedPiece(null);
        panel.repaint();

        if (board.getMode() == PLAYER_VS_COMPUTER) {
            int colorToMove = board.getColorToMove();
            if (colorToMove == board.getPlayAs()) {
                this.moveManager.aiJustMoved();
            } else {
                this.moveManager.playerJustMoved();
            }
        }
    }
}
