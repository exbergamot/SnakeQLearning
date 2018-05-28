package snake;

import org.deeplearning4j.rl4j.policy.DQNPolicy;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import snake.board.Board;
import snake.body.BodyPart;
import snake.nn.encoders.SnakeEncoder;
import snake.swing.GamePanel;
import snake.swing.listeners.DirectionKeyListener;
import snake.swing.menu.GameMenuBar;
import snake.swing.tabs.DataPanel;
import snake.swing.tabs.RunNetworkPanel;
import snake.swing.tabs.StartLearningPanel;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;


public class Runner {
    public static final String POLICY_FILE = "C:/policies/policy500k.nn";
    public static final int NN_CYCLE_DELAY = 50;
    private static GamePanel panel;
    private static DirectionKeyListener keyListener;

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setJMenuBar(new GameMenuBar());

        //JPanel panel = new DataPanel(DataPanel.RL_CONFIGURATION, new String[]{"1", "2", "3", "4", "5"});
        JPanel panel = new StartLearningPanel();
        //JPanel panel = new RunNetworkPanel();
        frame.add(panel);

        //startGame(frame);

        frame.pack();
        frame.setVisible(true);
    }

    public static void startGame(JFrame frame) {
        panel = new GamePanel();
        panel.setDoubleBuffered(true);
        panel.setSize(new Dimension(600,600));
        panel.setVisible(true);
        frame.add(panel);
        keyListener = new DirectionKeyListener();
        frame.addKeyListener(keyListener);


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                while (true) {
                    try {
                        mainCycleNN();
                        //mainCycleManual();
                    } catch (InterruptedException e) {
                        break;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        thread.start();
    }

    private static void mainCycleNN() throws InterruptedException, IOException {

        Board board = new Board();
        SnakeEncoder encoder = SnakeEncoder.createEncoder(board);
        int[] shape = encoder.getShape();
        shape = new int[]{1, shape[0], shape[1], shape[2]};
        final DQNPolicy<SnakeEncoder> policy = DQNPolicy.load(POLICY_FILE);
        panel.setBoard(board);

        while(true) {
            panel.repaint();
            INDArray input = Nd4j.create(SnakeEncoder.createEncoder(board).toArray());

            INDArray reshape = input.reshape(shape);
            Integer action = policy.nextAction(reshape);
            board.getSnake().setDirection(BodyPart.values()[action]);
            if (!board.move()) {
                break;
            }
            Thread.sleep(NN_CYCLE_DELAY);
        }
    }

    private static void mainCycleManual() throws InterruptedException, IOException {

        Board board = new Board();
        keyListener.setSnake(board.getSnake());
        panel.setBoard(board);

        while(true) {
            panel.repaint();
            if (!board.move()) {
                break;
            }
            Thread.sleep(200);
        }
    }
}
