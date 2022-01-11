package renderer;

import chess.Board;
import chess.pieces.Piece;
import listener.ClickListener;

import javax.swing.*;
import java.awt.*;
import java.util.Hashtable;

public class BoardPanel extends JPanel {
    Graphics2D g;
    Board board;
    ClickListener cl;

    public BoardPanel(Board board, int width, int height) {
        this.board = board;
        this.setPreferredSize(new Dimension(width, height));
        this.setMinimumSize(new Dimension(600, 400));
        this.cl = new ClickListener(board);
        this.addMouseListener(cl);
        cl.setPanel(this);
    }

    @Override
    public void paintComponent(Graphics g) {
        board.setScreenX(getWidth() / 2);
        board.setScreenY(getHeight() / 2);
        board.setScreenLength(Math.round(((float) Math.min(getWidth(), getHeight()) * 13 / 16) / board.getSize()) * board.getSize());
        this.g = (Graphics2D) g;
        Point mousePos = getMousePosition();
        Piece piece = board.getDraggedPiece();
        if (board.isDirty() || (board.getDraggedPiece() != null && mousePos != null)) {
            // draw board
            drawBaseBoard(board);
            drawHighlightedSquares(board);
            drawPieces(board);
            board.setClean();
        }
        if (board.getDraggedPiece() != null && mousePos != null) {
            // draw dragged piece
            int squareLength = board.getScreenLength() / board.getSize();
            int x = (int) (mousePos.getX()) - squareLength / 2;
            int y = (int) (mousePos.getY()) - squareLength / 2;
            g.drawImage(piece.getImage(), x, y, squareLength, squareLength, null);
            repaint();
        }
    }

    public void drawBaseBoard(Board board) {
        g.setBackground(new Color(0.125f, 0.122f, 0.125f, 1.0f));
        g.clearRect(0, 0, getWidth(), getHeight());

        int centerX = board.getScreenX();
        int centerY = board.getScreenY();
        int boardSideLength = board.getScreenLength();

        g.setPaint(new Color(0.243f, 0.239f, 0.223f, 1.0f));
        g.fillRect(centerX - boardSideLength * 17 / 32, centerY - boardSideLength * 17 / 32, boardSideLength * 17 / 16,
                boardSideLength * 17 / 16);
        int size = board.getSize();
        for (int rank = 0; rank < size; rank++) {
            for (int file = 0; file < size; file++) {
                boolean isLightSquare = (file + rank) % 2 != 0;
                if (isLightSquare) {
                    g.setPaint(new Color(0.937f, 0.850f, 0.717f, 1.0f));
                } else {
                    g.setPaint(new Color(0.705f, 0.533f, 0.401f, 1.0f));
                }
                g.fillRect(centerX - boardSideLength / 2 + rank * boardSideLength / board.getSize(),
                        centerY - boardSideLength / 2 + file * boardSideLength / board.getSize(),
                        boardSideLength / board.getSize(),
                        boardSideLength / board.getSize());
            }
        }
    }

    public void drawHighlightedSquares(Board board) {
        Hashtable<Integer, Color> highlighted = board.getHighlightedSquares();
        for (int squareId : highlighted.keySet()) {
            int size = board.getSize();
            int boardSideLength = board.getScreenLength();
            int col = squareId % size;
            int row = squareId / size;
            int x = board.getScreenX() - boardSideLength / 2 + col * boardSideLength / size;
            int y = board.getScreenY() - boardSideLength / 2 + row * boardSideLength / size;
            g.setPaint(highlighted.get(squareId));
            g.fillRect(x, y, boardSideLength / size, boardSideLength / size);
        }
    }

    public void drawPieces(Board board) {
        int boardSideLength = board.getScreenLength();
        int size = board.getSize();
        int imageLength = boardSideLength / size;
        for (Piece piece : board.getArrangement()) {
            if (piece != null) {
                int x = getWidth() / 2 - boardSideLength / 2 + piece.getCol() * boardSideLength / size;
                int y = getHeight() / 2 - boardSideLength / 2 + piece.getRow() * boardSideLength / size;
                g.drawImage(piece.getImage(), x, y, imageLength, imageLength, null);
            }
        }
    }
}
