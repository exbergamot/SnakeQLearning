package snake.nn;

import org.deeplearning4j.gym.StepReply;
import org.deeplearning4j.rl4j.mdp.MDP;
import org.deeplearning4j.rl4j.space.ArrayObservationSpace;
import org.deeplearning4j.rl4j.space.DiscreteSpace;
import org.deeplearning4j.rl4j.space.ObservationSpace;
import snake.board.Board;
import snake.body.BodyPart;

import static snake.nn.BoardEncodableWrapper.TOTAL_OUTPUT_SIZE;

public class SnakeMdpAdapter implements MDP<BoardEncodableWrapper, Integer, DiscreteSpace> {
    private DiscreteSpace actionSpace = new DiscreteSpace(4);
    private ObservationSpace<BoardEncodableWrapper> observationSpace = new ArrayObservationSpace<>(new int[]{1,41,41});
    private Board board = new Board();

    @Override
    public ObservationSpace<BoardEncodableWrapper> getObservationSpace() {
        return observationSpace;
    }

    @Override
    public DiscreteSpace getActionSpace() {
        return actionSpace;
    }

    @Override
    public BoardEncodableWrapper reset() {
        board = new Board();
        return new BoardEncodableWrapper(board);
    }

    @Override
    public void close() {
        System.out.println("Close");
    }

    @Override
    public StepReply<BoardEncodableWrapper> step(Integer integer) {
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
            reward = ((double)lengthAfter - lengthBefore) / 4;
            if (lengthAfter == 200) {
                done = true;
            }
        }

        return new StepReply<>(new BoardEncodableWrapper(board), reward, done, null);
    }

    @Override
    public boolean isDone() {
        if (board.getSnake().getBody().size() > 200) {
            return true;
        } else {
            return board.isOver();
        }
    }

    @Override
    public MDP<BoardEncodableWrapper, Integer, DiscreteSpace> newInstance() {
        return new SnakeMdpAdapter();
    }
}
