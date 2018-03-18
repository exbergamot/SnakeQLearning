package snake.nn;

import org.deeplearning4j.rl4j.space.Encodable;
import snake.board.Board;
import snake.general.Point;

import java.util.Arrays;

import static snake.board.Board.BOARD_SIZE;

public class BoardEncodableWrapper implements Encodable {
    public static final int CHANNELS_COUNT = 3;
    public static final int EXTENDED_BOARD_SIZE = BOARD_SIZE + 2;
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
        double[] obstacles = new double[FLATTEN_LAYER_SIZE];
        double[] head = new double[FLATTEN_LAYER_SIZE];
        double[] cherries = new double[FLATTEN_LAYER_SIZE];

        fillBorders(obstacles);
        fillSnakeBody(obstacles, board);
        fillHead(head, board);
        fillCherries(cherries, board);

        double[] result = new double[obstacles.length + head.length + cherries.length];
        System.arraycopy(obstacles, 0, result, 0, obstacles.length);
        System.arraycopy(head, 0, result, obstacles.length, head.length);
        System.arraycopy(cherries, 0, result, obstacles.length + head.length, cherries.length);
        return result;
    }

    private void fillHead(double[] head, Board board) {
        Arrays.fill(head, -0.004);
        Point headPoint = board.getSnake().getHead();
        head[(headPoint.getY() + 1) * EXTENDED_BOARD_SIZE + headPoint.getX() + 1] = 2;
    }

    private void fillCherries(double[] field, Board board) {
        Arrays.fill(field, -0.076);
        for (Point each : board.getCherries()) {
            field[(each.getY() + 1) * EXTENDED_BOARD_SIZE + each.getX() + 1] = 1.924;
        }
    }

    private void fillSnakeBody(double[] field, Board board) {
        for (Point each : board.getSnake().getBody()) {
            field[(each.getY() + 1) * EXTENDED_BOARD_SIZE + each.getX() + 1] = 1.67;
        }
        Point head = board.getSnake().getHead();
        field[(head.getY() + 1) * EXTENDED_BOARD_SIZE + head.getX() + 1] = -0.33;
    }

    private void fillBorders(double[] field) {
        Arrays.fill(field, -0.33);
        for (int i = 0; i < field.length; i++) {
            if (i < EXTENDED_BOARD_SIZE ||
                i >= (EXTENDED_BOARD_SIZE - 1) * EXTENDED_BOARD_SIZE ||
                i % EXTENDED_BOARD_SIZE  == 0 ||
                    (i % EXTENDED_BOARD_SIZE) == (EXTENDED_BOARD_SIZE - 1)) {
                field[i] = 1.67;
            }
        }
    }
}
