package renderer;

import chess.Board;
import chess.Chess;
import chess.pieces.Piece;
import listener.ClickListener;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

import static chess.Board.PLAYER_VS_COMPUTER;
import static chess.pieces.Piece.WHITE;

public class ChessboardPanel extends JPanel {
    private Board board;

    public ChessboardPanel(Board board, int width, int height) {
        this.board = board;
        this.setPreferredSize(new Dimension(width, height));
        this.setMinimumSize(new Dimension(600, 400));
        ClickListener cl = new ClickListener(board, this);
        this.addMouseListener(cl);
    }

    @Override
    public void paintComponent(Graphics gr) {
        super.paintComponent(gr);
        // render function
        board.setScreenX(getWidth() / 2);
        board.setScreenY(getHeight() / 2);
        board.setScreenLength(Math.round(((float) Math.min(getWidth(), getHeight()) * 13 / 16) / board.getSize()) * board.getSize());
        Graphics2D g = (Graphics2D) gr;
        Point mousePos = getMousePosition();
        Piece piece = board.getDraggedPiece();
        if (board.isDirty()) {
            // draw background, board, and pieces
            drawBaseBoard(g, board);
            drawRankFileLabels(g, board);
            drawHighlightedSquares(g, board);
            drawPieces(g, board);
            if (board.getDraggedPiece() != null && mousePos != null) {
                // draw dragged piece
                int squareLength = board.getScreenLength() / board.getSize();
                int x = (int) (mousePos.getX() - squareLength / 2);
                int y = (int) (mousePos.getY() - squareLength / 2);
                g.drawImage(piece.getImage(), x, y, squareLength, squareLength, null);
                repaint();
            } else {
                board.setClean();
            }
        }
    }

    public void drawBaseBoard(Graphics2D g, Board board) {
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
                boolean isLightSquare = (file + rank) % 2 == 0;
                if (isLightSquare) {
                    g.setPaint(new Color(0.937f, 0.850f, 0.717f, 1.0f));
                } else {
                    g.setPaint(new Color(0.705f, 0.533f, 0.401f, 1.0f));
                }
                int x = centerX - boardSideLength / 2 + rank * boardSideLength / board.getSize();
                int y = centerY - boardSideLength / 2 + file * boardSideLength / board.getSize();
                g.fillRect(x, y, boardSideLength / board.getSize(), boardSideLength / board.getSize());
            }
        }
    }

    public void drawRankFileLabels(Graphics2D g, Board board) {
        int boardSideLength = board.getScreenLength();
        int size = board.getSize();
        int playAs = board.getPlayAs();
        int fontSize = 1;
        while (g.getFontMetrics(new Font("Trebuchet MS", Font.BOLD, fontSize)).getHeight() < boardSideLength / (size * 4)) {
            fontSize++;  // finding font to match board size
        }
        Font font = new Font("Trebuchet MS", Font.BOLD, fontSize);
        g.setFont(font);

        for (int i = 0; i < size * size; i++) {  // drawString draws lower left corner
            int x = i % size;
            int y = i / size;
            if ((x + y) % 2 == 0) {
                g.setPaint(new Color(0.705f, 0.533f, 0.401f, 1.0f));
            } else {
                g.setPaint(new Color(0.937f, 0.850f, 0.717f, 1.0f));
            }
            if (x == 0) {  // left side numbers
                int drawX = board.getScreenX() - boardSideLength / 2 + boardSideLength / size / 12;
                int drawY = board.getScreenY() + (int) ((y - size / 2f) * (boardSideLength / size)) + boardSideLength / size / 4;
                g.drawString(String.valueOf(playAs == WHITE ? size - y : y + 1), drawX, drawY);
            }
            if (y == size - 1) {  // bottom side letters
                int drawX = board.getScreenX() + (int) ((x - size / 2f + 1) * (boardSideLength / size)) - boardSideLength / size / 6;
                int drawY = board.getScreenY() + boardSideLength / 2 - boardSideLength / size / 12;
                g.drawString(Chess.xToPrintableLetters(playAs == WHITE ? x + 1 : size - x), drawX, drawY);
            }
        }
    }

    public void drawHighlightedSquares(Graphics2D g, Board board) {
        HashMap<Integer, ArrayList<Color>> highlighted = board.getHighlightedSquares();
        for (int squareId : highlighted.keySet()) {
            int size = board.getSize();
            int playAs = board.getPlayAs();
            int boardSideLength = board.getScreenLength();
            int col = squareId % size;
            int row = squareId / size;
            int x = board.getScreenX() - boardSideLength / 2 + (playAs == WHITE ? col : size - 1 - col) * boardSideLength / size;
            int y = board.getScreenY() - boardSideLength / 2 + (playAs == WHITE ? row : size - 1 - row) * boardSideLength / size;
            for (Color c : highlighted.get(squareId)) {
                g.setPaint(c);
                g.fillRect(x - 1, y - 1, boardSideLength / size + 2, boardSideLength / size + 2);
            }
        }
    }

    public void drawPieces(Graphics2D g, Board board) {
        int boardSideLength = board.getScreenLength();
        int playAs = board.getPlayAs();
        int size = board.getSize();
        int imageLength = boardSideLength / size;
        Piece draggedPiece = board.getDraggedPiece();
        for (Piece piece : board.getArrangement()) {
            if (piece != null && piece != draggedPiece) {
                int x = getWidth() / 2 - boardSideLength / 2 + (playAs == WHITE ? piece.getCol() : size - 1 - piece.getCol()) * boardSideLength / size;
                int y = getHeight() / 2 - boardSideLength / 2 + (playAs == WHITE ? piece.getRow() : size - 1 - piece.getRow()) * boardSideLength / size;
                g.drawImage(piece.getImage(), x, y, imageLength, imageLength, null);
            }
        }
    }

    public void reset() {
        this.board = new Board("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", WHITE, PLAYER_VS_COMPUTER);
    }
}
