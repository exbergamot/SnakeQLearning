package org.deeplearning4j.rl4j.network.dqn;

import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;

public class MlnExtractor {
    public static MultiLayerNetwork extract(DQN dqn) {
        return dqn.mln;
    }
}
