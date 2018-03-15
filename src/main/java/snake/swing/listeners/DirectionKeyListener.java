package snake.swing.listeners;

import snake.body.BodyPart;
import snake.body.Snake;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Optional;

public class DirectionKeyListener implements KeyListener{
    private Snake snake;

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        Optional<BodyPart> bodyPart = BodyPart.getByKey(e.getKeyChar());
        bodyPart.ifPresent(action -> snake.setDirection(action));
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public void setSnake(Snake snake) {
        this.snake = snake;
    }
}
