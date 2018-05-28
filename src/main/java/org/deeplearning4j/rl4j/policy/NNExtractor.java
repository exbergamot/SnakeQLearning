package org.deeplearning4j.rl4j.policy;

import org.deeplearning4j.rl4j.network.dqn.IDQN;
import snake.nn.encoders.SnakeEncoder;

public class NNExtractor {
    public static IDQN extract (DQNPolicy<SnakeEncoder> policy) {
        return policy.getNeuralNet();
    }


}
