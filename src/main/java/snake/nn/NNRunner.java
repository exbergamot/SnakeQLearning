package snake.nn;

import org.deeplearning4j.rl4j.learning.sync.qlearning.QLearning;
import org.deeplearning4j.rl4j.learning.sync.qlearning.discrete.QLearningDiscrete;
import org.deeplearning4j.rl4j.learning.sync.qlearning.discrete.QLearningDiscreteConv;
import org.deeplearning4j.rl4j.learning.sync.qlearning.discrete.QLearningDiscreteDense;
import org.deeplearning4j.rl4j.mdp.MDP;
import org.deeplearning4j.rl4j.mdp.gym.GymEnv;
import org.deeplearning4j.rl4j.network.dqn.DQN;
import org.deeplearning4j.rl4j.network.dqn.DQNFactoryStdConv;
import org.deeplearning4j.rl4j.network.dqn.DQNFactoryStdDense;
import org.deeplearning4j.rl4j.network.dqn.IDQN;
import org.deeplearning4j.rl4j.policy.DQNPolicy;
import org.deeplearning4j.rl4j.space.Box;
import org.deeplearning4j.rl4j.space.DiscreteSpace;
import org.deeplearning4j.rl4j.util.DataManager;

import java.io.IOException;
import java.util.logging.Logger;

import static snake.nn.BoardEncodableWrapper.EXTENDED_BOARD_SIZE;

public class NNRunner {

    public static QLearning.QLConfiguration CARTPOLE_QL =
            new QLearning.QLConfiguration(
                    123,    //Random seed
                    400,    //Max step By epoch
                    300000, //Max step
                    300000, //Max size of experience replay
                    32,     //size of batches
                    500,    //target update (hard)
                    15,     //num step noop warmup
                    1,   //reward scaling
                    0.99,   //gamma
                    1.0,    //td-error clipping
                    0.001f,   //min epsilon
                    100000,   //num step for eps greedy anneal
                    true    //double DQN
            );

    public static DQNFactoryStdDense.Configuration CARTPOLE_NET =
            DQNFactoryStdDense.Configuration.builder()
                    .l2(0.00001).learningRate(0.0005).numHiddenNodes(64).numLayer(3).build();

    public static DQNFactoryStdConv.Configuration CONV_CONFIG =
            DQNFactoryStdConv.Configuration.builder()
                    .l2(0.00001).learningRate(0.0005).build();

    public static void main(String[] args) throws IOException {
        cartPole();
        //loadCartpole();
    }

    public static void cartPole() throws IOException {

        //record the training data in rl4j-data in a new folder (save)
        DataManager manager = new DataManager(true);

        //define the mdp from gym (name, render)
        MDP<BoardEncodableWrapper, Integer, DiscreteSpace> mdp = new SnakeMdpAdapter();

        DQNFactoryStdConv factory = new DQNFactoryStdConv(CONV_CONFIG);
        DQN dqnConv = factory.buildDQN(new int[]{1, EXTENDED_BOARD_SIZE, EXTENDED_BOARD_SIZE}, mdp.getActionSpace().getSize());
        QLearningDiscreteImpl dql = new QLearningDiscreteImpl(mdp, dqnConv, CARTPOLE_QL, manager, CARTPOLE_QL.getEpsilonNbStep());

        //define the training
        //QLearningDiscrete<BoardEncodableWrapper> dql = new QLearningDiscreteDense<>(mdp, CARTPOLE_NET, CARTPOLE_QL, manager);

        //train
        dql.train();

        //get the final policy
        DQNPolicy<BoardEncodableWrapper> pol = dql.getPolicy();

        //serialize and save (serialization showcase, but not required)
        pol.save("F:/policy.nn");

        //close the mdp (close http)
        mdp.close();

    }


    public static void loadCartpole() throws IOException {

        //showcase serialization by using the trained agent on a new similar mdp (but render it this time)

        //define the mdp from gym (name, render)
        GymEnv mdp2 = new GymEnv("CartPole-v0", true, false);

        //load the previous agent
        DQNPolicy<Box> pol2 = DQNPolicy.load("/tmp/pol1");

        //evaluate the agent
        double rewards = 0;
        for (int i = 0; i < 1000; i++) {
            mdp2.reset();
            double reward = pol2.play(mdp2);
            rewards += reward;
            Logger.getAnonymousLogger().info("Reward: " + reward);
        }

        Logger.getAnonymousLogger().info("average: " + rewards/1000);

    }

}
