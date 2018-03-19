package snake.nn;

import org.deeplearning4j.gym.StepReply;
import org.deeplearning4j.rl4j.mdp.MDP;
import org.deeplearning4j.rl4j.space.ArrayObservationSpace;
import org.deeplearning4j.rl4j.space.DiscreteSpace;
import org.deeplearning4j.rl4j.space.ObservationSpace;
import snake.board.Board;
import snake.body.BodyPart;
import snake.nn.encoders.EncoderSingleHeadCenteredLayer;
import snake.nn.encoders.SnakeEncoder;

import static snake.nn.BoardEncodableWrapper.CHANNELS_COUNT;
import static snake.nn.BoardEncodableWrapper.EXTENDED_BOARD_SIZE;
import static snake.nn.BoardEncodableWrapper.TOTAL_OUTPUT_SIZE;

public class SnakeMdpAdapter implements MDP<SnakeEncoder, Integer, DiscreteSpace> {
    private static final int TARGET_LENGTH = 80;

    private DiscreteSpace actionSpace = new DiscreteSpace(4);
    private ObservationSpace<SnakeEncoder> observationSpace = new ArrayObservationSpace<>(createWrapper(new Board()).getShape());
    private Board board = new Board();

    @Override
    public ObservationSpace<SnakeEncoder> getObservationSpace() {
        return observationSpace;
    }

    @Override
    public DiscreteSpace getActionSpace() {
        return actionSpace;
    }

    @Override
    public SnakeEncoder reset() {
        board = new Board();
        return createWrapper(board);
    }

    @Override
    public void close() {
        System.out.println("Close");
    }

    @Override
    public StepReply<SnakeEncoder> step(Integer integer) {
        board.getSnake().setDirection(BodyPart.values()[integer]);
        int lengthBefore = board.getSnake().getDesiredSize();

        boolean done = false;
        double reward = 0;
        boolean moved = board.move();
        if (!moved) {
            reward = -1;
            done = true;
        } else {
            int lengthAfter = board.getSnake().getDesiredSize();
            reward = ((double)lengthAfter - lengthBefore) / 4 - 0.01;
            if (lengthAfter == TARGET_LENGTH) {
                done = true;
            }
        }

        return new StepReply<>(createWrapper(board), reward, done, null);
    }

    @Override
    public boolean isDone() {
        if (board.getSnake().getBody().size() >= TARGET_LENGTH) {
            return true;
        } else {
            return board.isOver();
        }
    }

    @Override
    public MDP<SnakeEncoder, Integer, DiscreteSpace> newInstance() {
        return new SnakeMdpAdapter();
    }

    private SnakeEncoder createWrapper(Board board) {
        return SnakeEncoder.createEncoder(board);
    }
}
