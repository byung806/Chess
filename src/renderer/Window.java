package renderer;

import chess.Board;
import chess.pieces.Piece;

import javax.swing.*;

public class Window extends JFrame {
    public Window() {
        Board board = new Board("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", Piece.WHITE);
        ChessboardPanel panel = new ChessboardPanel(board, 600, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Chess");
        this.setIconImage(new ImageIcon("assets/icon/icon.png").getImage());

        this.add(panel);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}
