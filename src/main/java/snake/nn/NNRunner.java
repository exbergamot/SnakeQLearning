package snake.nn;

import com.sun.media.jfxmedia.logging.Logger;
import org.deeplearning4j.rl4j.learning.sync.qlearning.QLearning;
import org.deeplearning4j.rl4j.mdp.MDP;
import org.deeplearning4j.rl4j.network.dqn.DQN;
import org.deeplearning4j.rl4j.network.dqn.DQNFactoryStdConv;
import org.deeplearning4j.rl4j.network.dqn.IDQN;
import org.deeplearning4j.rl4j.policy.DQNPolicy;
import org.deeplearning4j.rl4j.policy.NNExtractor;
import org.deeplearning4j.rl4j.space.DiscreteSpace;
import org.deeplearning4j.rl4j.util.DataManager;
import snake.nn.encoders.SnakeEncoder;

import java.io.IOException;

public class NNRunner {

    public static QLearning.QLConfiguration CARTPOLE_QL =
            new QLearning.QLConfiguration(
                    1232825431,    //Random seed
                    2000,    //Max step By epoch
                    100000, //Max step
                    100000, //Max size of experience replay
                    64,     //size of batches
                    5000,    //target update (hard)
                    5,     //num step noop warmup
                    1f,   //reward scaling
                    0.99,   //gamma
                    1.0,    //td-error clipping
                    0.00f,   //min epsilon
                    10000,   //num step for eps greedy anneal
                    true    //double DQN
            );

    public static DQNFactoryStdConv.Configuration CONV_CONFIG =
            DQNFactoryStdConv.Configuration.builder()
                    .l2(0.000).learningRate(0.00005).build();

    public static void main(String[] args) throws IOException {
        Logger.setLevel(Logger.ERROR);
        trainSnake();
        //loadCartpole();
    }

    public static void trainSnake() throws IOException {

        //record the training data in rl4j-data in a new folder (save)
        DataManager manager = new DataManager(true);

        //define the mdp from gym (name, render)
        MDP<SnakeEncoder, Integer, DiscreteSpace> mdp = new SnakeMdpAdapter();

        //SnakeDQNConvFactory factory = new SnakeDQNConvFactory(CONV_CONFIG);
        //DQN dqnConv = factory.buildDQN(mdp);

        IDQN dqnConv = loadNN();

        QLearningDiscreteImpl dql = new QLearningDiscreteImpl(mdp, dqnConv, CARTPOLE_QL, manager, CARTPOLE_QL.getEpsilonNbStep());

        //define the training
        //QLearningDiscrete<BoardEncodableWrapper> dql = new QLearningDiscreteDense<>(mdp, CARTPOLE_NET, CARTPOLE_QL, manager);

        //train
        dql.train();

        //get the final policy
        DQNPolicy<SnakeEncoder> pol = dql.getPolicy();

        //serialize and save (serialization showcase, but not required)
        pol.save("C:/policies/policyOut.nn");

        //close the mdp (close http)
        mdp.close();

    }

    public static IDQN loadNN() throws IOException {
        final DQNPolicy<SnakeEncoder> policy = DQNPolicy.load("C:/policies/policy.nn");
        return NNExtractor.extract(policy);
    }
}
