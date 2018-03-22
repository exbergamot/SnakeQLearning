package snake.nn.reports;

import org.deeplearning4j.nn.api.Layer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.rl4j.network.dqn.DQN;
import org.deeplearning4j.rl4j.network.dqn.MlnExtractor;
import org.nd4j.linalg.api.ndarray.INDArray;

public class WeightsDrawer {
    public static void draw(DQN dqn) {
        MultiLayerNetwork mln = MlnExtractor.extract(dqn);
        Layer layer = mln.getLayer(0);
        INDArray params = layer.params();
    }


}
