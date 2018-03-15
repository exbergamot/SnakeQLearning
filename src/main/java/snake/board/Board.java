package snake.board;

import snake.Drawable;
import snake.body.Snake;
import snake.general.Point;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedDeque;

import static snake.body.BodyPart.CHERRY;


public class Board implements Drawable {

    public static int CELL_SIZE = 20;
    public static int BOARD_SIZE = 21;
    
    private static final int CHERRY_GENERATION_COOLdAWN = 2;
    private static final int MAX_CHERRIES = 40;

    private Snake snake = new Snake(new Point(BOARD_SIZE / 2, BOARD_SIZE / 2));
    private int turn = 0;
    private Random random = new Random();
    private Collection<Point> cherries = new ConcurrentLinkedDeque<>();

    private boolean gameOver = false;

    private double score = 0;

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(0,0, CELL_SIZE * BOARD_SIZE, CELL_SIZE * BOARD_SIZE);
        g.setColor(Color.GRAY);
        for (int i = 0; i < BOARD_SIZE; i++) {
            g.drawLine(CELL_SIZE * i, 0, CELL_SIZE * (i), CELL_SIZE * BOARD_SIZE);
            g.drawLine(0, CELL_SIZE * i, CELL_SIZE * BOARD_SIZE, CELL_SIZE * (i));
        }
        drawCherries(g);
        snake.draw(g);
    }

    private void drawCherries(Graphics g) {
        for (Point point : cherries) {
            g.drawImage(CHERRY.getImg(), point.getX() * CELL_SIZE, point.getY() * CELL_SIZE, null);
        }
    }

    public boolean move() {
        turn++;
        Point headPoint = snake.move();
        eatCherry(headPoint);
        generateCherry();
        boolean isAvailable = isAvailableSpace(headPoint);
        if (isAvailable) {
            snake.addHeadPoint(headPoint);
        } else {
            gameOver = true;
        }
        return isAvailable;
    }

    private void eatCherry(Point headPoint) {
        if (cherries.contains(headPoint)) {
            snake.eatCherry();
            cherries.remove(headPoint);
        }
    }

    private void generateCherry() {
        if (turn % CHERRY_GENERATION_COOLdAWN == 0 &&
                cherries.size() <= MAX_CHERRIES) {
            Point cherryPoint = null;
            do {
                int x = random.nextInt(BOARD_SIZE);
                int y = random.nextInt(BOARD_SIZE);
                cherryPoint = new Point(x, y);
            } while (!snake.isFree(cherryPoint));
            cherries.add(cherryPoint);
        }
    }

    private boolean isAvailableSpace(Point headPoint) {
        int x = headPoint.getX();
        int y = headPoint.getY();
        return (snake.isFree(new Point(x, y)) &&
                (x >= 0 && x < BOARD_SIZE) &&
                (y >= 0 && y < BOARD_SIZE));
    }

    public Snake getSnake() {
        return snake;
    }

    public Collection<Point> getCherries() {
        return cherries;
    }

    public boolean isOver() {
        return gameOver;
    }
}