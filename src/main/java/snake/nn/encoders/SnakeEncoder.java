package snake.nn.encoders;

import org.deeplearning4j.rl4j.space.Encodable;
import snake.board.Board;
import snake.body.Snake;
import snake.general.Point;
import snake.nn.BoardEncodableWrapper;

import java.awt.*;
import java.util.Arrays;

import static snake.board.Board.BOARD_SIZE;
import static snake.board.Board.CELL_SIZE;

public abstract class SnakeEncoder implements Encodable {

    private double[] data;
    protected Board board;
    private Point shift;
    private Point farthestPoint;


    public abstract int[] getShape();
    protected abstract double[] fillData(Board board);

    protected SnakeEncoder(Board board) {
        this.board = board;
        Point head = board.getSnake().getHead();
        Point fullDiagonalVector = new Point(BOARD_SIZE, BOARD_SIZE);
        this.shift = fullDiagonalVector.subtract(head);
        this.farthestPoint = shift.add(fullDiagonalVector);
        this.data = fillData(board);
    }

    public static SnakeEncoder createEncoder(Board board) {
        return new EncoderSingleHeadCenteredLayer(board);
    }

    public double[] toArray() {
        return data;
    }

    protected int flattenIndex(Point point, int layer) {
        int layerStart = layer * getLayerSize();
        return point.getY() * getWidth() + point.getX() + layerStart;
    }

    protected Point headCenterPoint(Point point) {
        return point.add(shift);
    }

    protected double[] fillHeadCenteredBoards(double[] layer) {
        return fillHeadCenteredBoards(layer, -1d);
    }

    protected double[] fillHeadCenteredBoards(double[] layer, double value) {
        for (int i = 0; i < layer.length; i++) {
            if (i < shift.getY() * getWidth() ||
                    i >= farthestPoint.getY() * getWidth() ||
                    i % getWidth() < shift.getX() ||
                    i % getWidth() >= farthestPoint.getX()) {
                layer[i] = value;
            }
        }
        return layer;
    }

    protected double[] fillHeadCenteredCherries(double[] layer) {
        return fillHeadCenteredCherries(layer, 1);
    }

    protected double[] fillHeadCenteredCherries(double[] layer, double value) {
        for (Point each : board.getCherries()) {
            layer[headCenterPoint(each).getY() * getWidth() + headCenterPoint(each).getX()] = 1;
        }
        return layer;
    }

    protected int getDepth() {
        return getShape()[0];
    }

    protected int getWidth() {
        return getShape()[1];
    }

    protected int getHeight() {
        return getShape()[2];
    }

    protected int getLayerSize() {
        return getHeight() * getWidth();
    }

    public void draw(Graphics g) {
        double max = Arrays.stream(data).max().getAsDouble();
        double min = Arrays.stream(data).min().getAsDouble();
        int topPoint = BOARD_SIZE * (CELL_SIZE + 2);

        for (int i = 0; i < getDepth(); i++) {
            int leftPoint = i * getWidth() * CELL_SIZE + i * 10 + 50;
            for (int j = 0; j < getHeight(); j++) {
                for (int k = 0; k < getWidth(); k++) {
                    int index = flattenIndex(new Point(k, j), i);
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
