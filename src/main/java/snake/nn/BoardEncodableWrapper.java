package snake.nn;

import org.deeplearning4j.rl4j.space.Encodable;
import snake.board.Board;
import snake.general.Point;

import java.util.Arrays;

import static snake.board.Board.BOARD_SIZE;

public class BoardEncodableWrapper implements Encodable {
    public static final int EXTENDED_BOARD_SIZE = BOARD_SIZE * 2 - 1;
    public static final int FLATTEN_LAYER_SIZE = EXTENDED_BOARD_SIZE * EXTENDED_BOARD_SIZE;
    public static final int TOTAL_OUTPUT_SIZE = FLATTEN_LAYER_SIZE * 2;

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
        Point shift = fullDiagonalVector.subtract(head);
        Point farthestPoint = shift.add(fullDiagonalVector);
        fillBorders(field, shift, farthestPoint);
        fillSnakeBody(field, board);
        fillCherries(field, board);
        return field;
    }

    private void fillCherries(double[] field, Board board) {
        for (Point each : board.getCherries()) {
            field[FLATTEN_LAYER_SIZE + each.getY() * BOARD_SIZE + each.getX()] = 1;
            //field[each.getY() * BOARD_SIZE + each.getX()] = 1;
        }
    }

    private void fillSnakeBody(double[] field, Board board) {
        for (Point each : board.getSnake().getBody()) {
            field[each.getY() * BOARD_SIZE + each.getX()] = -1;
        }
    }

    private void fillBorders(double[] field, Point shift, Point farthestPoint) {
        for (int i = 0; i < field.length; i++) {
            if (i < shift.getY() * BOARD_SIZE ||
                i >= farthestPoint.getY() * BOARD_SIZE ||
                i % BOARD_SIZE < shift.getX() ||
                i % BOARD_SIZE > farthestPoint.getX()) {
                field[i] = -1;
            }
        }
    }
}
