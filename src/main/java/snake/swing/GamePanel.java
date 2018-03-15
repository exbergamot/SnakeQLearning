package snake.swing;

import snake.board.Board;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    private Board board;
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (board != null) {
            board.draw(g);
        }
    }

    public void setBoard(Board board) {
        this.board = board;
    }
}
