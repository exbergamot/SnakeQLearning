package snake.nn.encoders;

import org.deeplearning4j.rl4j.space.Encodable;
import snake.board.Board;
import snake.general.Point;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import static snake.board.Board.BOARD_SIZE;

public class Encoder2Layers4Values extends SnakeEncoder {
    private static final int CHANNELS_COUNT = 2;
    private static final int EXTENDED_BOARD_SIZE = BOARD_SIZE * 2 + 1;
    private static final int FLATTEN_LAYER_SIZE = EXTENDED_BOARD_SIZE * EXTENDED_BOARD_SIZE;
    private static final int TOTAL_OUTPUT_SIZE = FLATTEN_LAYER_SIZE * CHANNELS_COUNT;
    private static final int[] DIMENSIONS = new int[]{CHANNELS_COUNT, EXTENDED_BOARD_SIZE, EXTENDED_BOARD_SIZE};

    public Encoder2Layers4Values(Board board) {
        super(board);
    }

    protected double[] fillData(Board board) {
        double[] fieldLayer = new double[FLATTEN_LAYER_SIZE];
        double[] headTailLayer = new double[FLATTEN_LAYER_SIZE];

        return null;
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
                    int index = flattenIndex(new Point(k,j), i);
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



    @Override
    public int[] getShape() {
        return DIMENSIONS;
    }
}
