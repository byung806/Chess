package renderer;

import chess.Board;

import javax.swing.*;

public class Window extends JFrame {
    public Window() {
        Board board = new Board("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        ChessboardPanel panel = new ChessboardPanel(board, 400, 400);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Chess");
        this.setIconImage(new ImageIcon("assets/icon/icon.png").getImage());

        this.add(panel);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}
