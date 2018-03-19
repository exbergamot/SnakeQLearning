package snake.swing;

import snake.board.Board;
import snake.nn.BoardEncodableWrapper;

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

            double[] data = new BoardEncodableWrapper(board).toArray();
            double max = Arrays.stream(data).max().getAsDouble();
            double min = Arrays.stream(data).min().getAsDouble();
            int[] shape = new int[]{2, 19, 19};
            int topPoint = BOARD_SIZE * (CELL_SIZE + 2);


            for (int i = 0; i < shape[0]; i++) {
                int leftPoint = i * shape[1]*CELL_SIZE + i * 10 + 50;
                for (int j = 0; j < shape[2]; j++) {
                    for (int k = 0; k < shape[1]; k++) {
                        int index = flattenIndex(k, j, i, shape);
                        Color color = Color.WHITE;
                        if (data[index] > 0) {
                            color = new Color(0, (int) (data[index] / max * 255), 0);
                        } else if (data[index] < 0) {
                            color = new Color((int) (data[index] / (min) * 255), 0, 0);
                        }
                        g.setColor(color);
                        g.fillRect(leftPoint + CELL_SIZE * k, topPoint + j * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                    }
                }

                g.setColor(Color.BLACK);
                g.fillRect(leftPoint + CELL_SIZE * 9, topPoint + 9 * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            }
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
