package snake;

import org.deeplearning4j.rl4j.policy.DQNPolicy;
import org.deeplearning4j.rl4j.space.Box;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import snake.board.Board;
import snake.body.BodyPart;
import snake.nn.BoardEncodableWrapper;
import snake.swing.GamePanel;
import snake.swing.listeners.DirectionKeyListener;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;


public class Runner {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setPreferredSize(new Dimension(600,600));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GamePanel panel = new GamePanel();
        panel.setDoubleBuffered(true);
        panel.setSize(new Dimension(600,600));
        panel.setVisible(true);
        frame.add(panel);
        DirectionKeyListener keyListener = new DirectionKeyListener();
        frame.addKeyListener(keyListener);
        frame.pack();
        frame.setVisible(true);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                while (true) {
                    try {
                        mainCycle();
                    } catch (InterruptedException e) {
                        break;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            private void mainCycle() throws InterruptedException, IOException {

                Board board = new Board();
                //keyListener.setSnake(board.getSnake());
                final DQNPolicy<BoardEncodableWrapper> policy = DQNPolicy.load("D:/policy.nn");
                panel.setBoard(board);

                while(true) {
                    panel.repaint();
                    INDArray input = Nd4j.create(new BoardEncodableWrapper(board).toArray());
                    INDArray reshape = input.reshape(1, 2, 41, 41);
                    Integer action = policy.nextAction(reshape);
                    board.getSnake().setDirection(BodyPart.values()[action]);
                    if (!board.move()) {
                        break;
                    }
                    Thread.sleep(150);
                }
            }
        });

        thread.start();

    }
}
