package snake.nn;

import org.deeplearning4j.rl4j.space.Encodable;
import snake.board.Board;
import snake.general.Point;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import static snake.board.Board.BOARD_SIZE;

public class BoardEncodableWrapper implements Encodable {
    public static final int CHANNELS_COUNT = 2;
    public static final int EXTENDED_BOARD_SIZE = BOARD_SIZE * 2 + 1;
    public static final int FLATTEN_LAYER_SIZE = EXTENDED_BOARD_SIZE * EXTENDED_BOARD_SIZE;
    public static final int TOTAL_OUTPUT_SIZE = FLATTEN_LAYER_SIZE * CHANNELS_COUNT;

    private double[] data;

    public BoardEncodableWrapper(Board board) {
        this.data = fillData(board);
    }

    public double[] toArray() {
        return data;
    }

    private double[] fillData(Board board) {
        double[] field = new double[TOTAL_OUTPUT_SIZE];
        //Arrays.fill(field, FLATTEN_LAYER_SIZE, TOTAL_OUTPUT_SIZE - 1, -0.2 * 10);
        Point fullDiagonalVector = new Point(BOARD_SIZE, BOARD_SIZE);
        Point head = board.getSnake().getHead();
        Point shift = fullDiagonalVector.subtract(head);//.add(new Point(1,1));
        Point farthestPoint = shift.add(fullDiagonalVector);
        fillBorders(field, shift, farthestPoint);
        fillSnakeBody(field, board, shift);
        fillCherries(field, board, shift);
        return field;
    }

    private void fillCherries(double[] field, Board board, Point shift) {
        for (Point each : board.getCherries()) {
            field[FLATTEN_LAYER_SIZE + (each.getY() + shift.getY()) * EXTENDED_BOARD_SIZE + each.getX() + shift.getX()] = 1;
        }
    }

    private void fillSnakeBody(double[] field, Board board, Point shift) {
        for (Point each : board.getSnake().getBody()) {
            field[(each.getY() + shift.getY()) * EXTENDED_BOARD_SIZE + each.getX() + shift.getX()] = -1;
        }
    }

    private void fillBorders(double[] field, Point shift, Point farthestPoint) {
/*        for (int i = 0; i < field.length; i++) {
            if (i < shift.getY() * BOARD_SIZE ||
                i >= farthestPoint.getY() * BOARD_SIZE ||
                i % BOARD_SIZE < shift.getX() ||
                i % BOARD_SIZE > farthestPoint.getX()) {
                field[i] = -1;
            }
        }*/

        for (int i = 0; i < FLATTEN_LAYER_SIZE; i++) {
            if (i < shift.getY() * EXTENDED_BOARD_SIZE ||
                    i >= farthestPoint.getY() * EXTENDED_BOARD_SIZE ||
                    i % EXTENDED_BOARD_SIZE < shift.getX() ||
                    i % EXTENDED_BOARD_SIZE >= farthestPoint.getX()) {
                field[i] = -1;
            }
        }
    }
    private void drawArray(double[] data) throws IOException {
        drawArray(data, new int[]{CHANNELS_COUNT, EXTENDED_BOARD_SIZE, EXTENDED_BOARD_SIZE});
    }

    private void drawArray(double[] data, int[] shape) throws IOException {
        double max = Arrays.stream(data).max().getAsDouble();
        double min = Arrays.stream(data).min().getAsDouble();

        BufferedImage image = new BufferedImage(shape[1] * shape[0] + (shape[0] -  1) * 10, shape[2], BufferedImage.TYPE_INT_ARGB);

        for (int i = 0; i < shape[0]; i++) {
            int leftPoint = i * shape[1] + i * 10;
            for (int j = 0; j < shape[2]; j++) {
                for (int k = 0; k < shape[1]; k++) {
                    int index = flattenIndex(k,j,i,shape);
                    Color color = Color.WHITE;
                    if (data[index] > 0) {
                        color = new Color(0,(int) (data[index] / max * 255), 0);
                    } else if (data[index] < 0) {
                        color = new Color((int) (data[index] / (min) * 255), 0,0);
                    }

                    image.setRGB(leftPoint + k,j, color.getRGB());
                }
            }
        }
        ImageIO.write(image, "png", new File("D:/output_image.png"));
    }

    private int flattenIndex(int x, int y, int layer, int[] shape) {
        int layerStart = layer * shape[1] * shape[2];
        return y * shape[1] + x + layerStart;
    }
}
