package snake.nn.encoders;

import org.deeplearning4j.rl4j.space.Encodable;
import snake.board.Board;
import snake.general.Point;

import static snake.board.Board.BOARD_SIZE;

public abstract class SnakeEncoder implements Encodable {

    private double[] data;
    protected Board board;
    private Point shift;
    private Point farthestPoint;

    public SnakeEncoder(Board board) {
        this.board = board;
        Point head = board.getSnake().getHead();
        Point fullDiagonalVector = new Point(BOARD_SIZE, BOARD_SIZE);
        this.shift = fullDiagonalVector.subtract(head);
        this.farthestPoint = shift.add(fullDiagonalVector);
        this.data = fillData(board);
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
        for (int i = 0; i < layer.length; i++) {
            if (i < shift.getY() * getWidth() ||
                    i >= farthestPoint.getY() * getWidth() ||
                    i % getWidth() < shift.getX() ||
                    i % getWidth() >= farthestPoint.getX()) {
                layer[i] = -1;
            }
        }
        return layer;
    }

    protected double[] fillHeadCenteredCherries(double[] layer) {
        for (Point each : board.getCherries()) {
            layer[getLayerSize() + headCenterPoint(each).getY() * getWidth() + headCenterPoint(each).getX() + shift.getX()] = 1;
        }
        return layer;
    }


    public abstract int[] getShape();
    protected abstract double[] fillData(Board board);

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

}
