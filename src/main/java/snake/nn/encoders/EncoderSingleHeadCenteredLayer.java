package snake.nn.encoders;

import snake.board.Board;

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



        return new double[0];
    }
}
