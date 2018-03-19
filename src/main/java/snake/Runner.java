package snake;

import org.deeplearning4j.rl4j.policy.DQNPolicy;
import org.deeplearning4j.rl4j.space.Box;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import snake.board.Board;
import snake.body.BodyPart;
import snake.nn.BoardEncodableWrapper;
import snake.nn.encoders.SnakeEncoder;
import snake.swing.GamePanel;
import snake.swing.listeners.DirectionKeyListener;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Arrays;

import static snake.nn.BoardEncodableWrapper.CHANNELS_COUNT;
import static snake.nn.BoardEncodableWrapper.EXTENDED_BOARD_SIZE;


public class Runner {
    private static GamePanel panel;
    private static DirectionKeyListener keyListener;

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setPreferredSize(new Dimension(600,600));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel = new GamePanel();
        panel.setDoubleBuffered(true);
        panel.setSize(new Dimension(600,600));
        panel.setVisible(true);
        frame.add(panel);
        keyListener = new DirectionKeyListener();
        frame.addKeyListener(keyListener);
        frame.pack();
        frame.setVisible(true);

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
        final DQNPolicy<BoardEncodableWrapper> policy = DQNPolicy.load("F:/policy.nn");
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
            Thread.sleep(150);
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
