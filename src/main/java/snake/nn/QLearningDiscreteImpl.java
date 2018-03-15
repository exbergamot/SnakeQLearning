package snake.nn;

import org.deeplearning4j.rl4j.learning.sync.qlearning.discrete.QLearningDiscrete;
import org.deeplearning4j.rl4j.mdp.MDP;
import org.deeplearning4j.rl4j.network.dqn.IDQN;
import org.deeplearning4j.rl4j.space.DiscreteSpace;
import org.deeplearning4j.rl4j.util.DataManager;

public class QLearningDiscreteImpl extends QLearningDiscrete<BoardEncodableWrapper> {

    public QLearningDiscreteImpl(MDP<BoardEncodableWrapper, Integer, DiscreteSpace> mdp, IDQN dqn, QLConfiguration conf, DataManager dataManager, int epsilonNbStep) {
        super(mdp, dqn, conf, dataManager, epsilonNbStep);
    }
}
