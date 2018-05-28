package snake.swing;

import snake.board.Board;
import snake.nn.BoardEncodableWrapper;
import snake.nn.encoders.SnakeEncoder;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;

import static snake.board.Board.BOARD_SIZE;
import static snake.board.Board.CELL_SIZE;

public class GamePanel extends JPanel {
    private Board board;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (board != null) {
            board.draw(g);
            //SnakeEncoder.createEncoder(board).draw(g);
        }
    }

    private int flattenIndex(int x, int y, int layer, int[] shape) {
        int layerStart = layer * shape[1] * shape[2];
        return y * shape[1] + x + layerStart;
    }

    public void setBoard(Board board) {
        this.board = board;
    }
}
