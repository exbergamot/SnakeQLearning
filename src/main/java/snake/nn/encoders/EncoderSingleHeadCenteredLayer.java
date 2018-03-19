package snake.nn.encoders;

import snake.board.Board;
import snake.general.Point;

import java.util.Deque;
import java.util.Iterator;

public class EncoderSingleHeadCenteredLayer extends SnakeEncoder {

    private static final int FIELD_SIZE = Board.BOARD_SIZE * 2 + 1;
    private static final int CHANNELS = 1;
    private static final int[] DIMENSIONS = new int[]{CHANNELS,FIELD_SIZE, FIELD_SIZE};

    public EncoderSingleHeadCenteredLayer(Board board) {
        super(board);
    }

    @Override
    public int[] getShape() {
        return DIMENSIONS;
    }

    @Override
    protected double[] fillData(Board board) {
        double[] layer = new double[getLayerSize()];
        fillHeadCenteredBoards(layer);
        fillHeadCenteredCherries(layer);
        fillHeadCenteredIndexedBody(layer, board);
        return layer;
    }

    private void fillHeadCenteredIndexedBody(double[] layer, Board board) {
        int coef = Board.BOARD_SIZE * Board.BOARD_SIZE;
        Iterator<Point> iterator = board.getSnake().getBody().iterator();
        int k = 0;
        int length = board.getSnake().getDesiredSize();
        while (iterator.hasNext()) {
            Point point = iterator.next();
            if (iterator.hasNext()) {
                int i = flattenIndex(headCenterPoint(point), 0);
                layer[i] = -1d / (1 + (length - k - 2) / (double)20);
                k++;
            }
        }
    }
}
