package snake.nn;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.inputs.InputType;
import org.deeplearning4j.nn.conf.layers.ConvolutionLayer;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.api.IterationListener;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.deeplearning4j.rl4j.mdp.MDP;
import org.deeplearning4j.rl4j.network.dqn.DQN;
import org.deeplearning4j.rl4j.network.dqn.DQNFactory;
import org.deeplearning4j.rl4j.network.dqn.DQNFactoryStdConv;
import org.deeplearning4j.rl4j.space.DiscreteSpace;
import org.deeplearning4j.rl4j.util.Constants;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.learning.config.IUpdater;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import snake.nn.encoders.SnakeEncoder;

@Value
public class SnakeDQNConvFactory implements DQNFactory {

    DQNFactoryStdConv.Configuration conf;

    public SnakeDQNConvFactory(DQNFactoryStdConv.Configuration conf) {
        this.conf = conf;
    }

    public DQN buildDQN(MDP<SnakeEncoder, Integer, DiscreteSpace> mdp) {
        int[] shapeInputs = mdp.getObservationSpace().getShape();
        int numOutputs = mdp.getActionSpace().getSize();
        return buildDQN(shapeInputs, numOutputs);
    }

    public DQN buildDQN(int shapeInputs[], int numOutputs) {
        int k = 0;
        if (shapeInputs.length == 1)
            throw new AssertionError("Impossible to apply convolutional layer on a shape == 1");


        NeuralNetConfiguration.ListBuilder confB = new NeuralNetConfiguration.Builder().seed(Constants.NEURAL_NET_SEED)
                .iterations(1).optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
                .learningRate(conf.getLearningRate())
                .regularization(conf.getL2() > 0)
                .l2(conf.getL2())
                //.updater(Updater.NESTEROVS).momentum(0.9)
                //.updater(Updater.RMSPROP).rmsDecay(conf.getRmsDecay())
                .updater(conf.getUpdater() != null ? conf.getUpdater() : new Adam())
                .weightInit(WeightInit.XAVIER).regularization(true).l2(conf.getL2()).list()
                .layer(k++, new ConvolutionLayer.Builder(2, 2).nIn(shapeInputs[0]).nOut(16).stride(1, 1).padding(1,1)
                        .activation(Activation.RELU).build());


        //confB.layer(k++, new ConvolutionLayer.Builder(3, 3).nOut(64).stride(1, 1).padding(2,2).activation(Activation.RELU).build());

        confB.layer(k++, new ConvolutionLayer.Builder(3, 3).nOut(64).stride(1, 1).padding(2,2).activation(Activation.RELU).build());

        confB.layer(k++, new DenseLayer.Builder().nOut(256).activation(Activation.RELU).build());

        confB.layer(k++, new OutputLayer.Builder(LossFunctions.LossFunction.MSE).activation(Activation.IDENTITY).nOut(numOutputs)
                .build());

        confB.setInputType(InputType.convolutional(shapeInputs[1], shapeInputs[2], shapeInputs[0]));
        MultiLayerConfiguration mlnconf = confB.pretrain(false).backprop(true).build();
        MultiLayerNetwork model = new MultiLayerNetwork(mlnconf);
        model.init();
        if (conf.getListeners() != null) {
            model.setListeners(conf.getListeners());
        } else {
            model.setListeners(new ScoreIterationListener(Constants.NEURAL_NET_ITERATION_LISTENER));
        }

        return new DQN(model);
    }


    @AllArgsConstructor
    @Builder
    @Value
    public static class Configuration {

        double learningRate;
        double l2;
        IUpdater updater;
        IterationListener[] listeners;
    }
}
