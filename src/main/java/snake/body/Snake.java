package snake.body;

import snake.Drawable;
import snake.general.Point;

import java.awt.*;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedDeque;

import static snake.board.Board.CELL_SIZE;
import static snake.body.BodyPart.BODY;
import static snake.body.BodyPart.TOP;

public class Snake implements Drawable{
    private BodyPart nextDirection;
    private BodyPart previousDirection;
    private Deque<Point> body = new ConcurrentLinkedDeque<>();
    private int desiredSize;

    public Snake(Point startLocation) {
        this.desiredSize = 2;
        this.previousDirection = TOP;
        this.nextDirection = TOP;
        addHeadPoint(startLocation);
    }

    public void setDirection(BodyPart nextDirection) {
        //if (nextDirection.getOpposite() != previousDirection) {
            this.nextDirection = nextDirection;
        //}
    }

    public void eatCherry() {
        desiredSize++;
    }

    public Point move() {
        Point headPoint = getHead();
        Point newHeadPoint = headPoint.add(nextDirection.getVelocity());
        previousDirection = nextDirection;
        return newHeadPoint;
    }

    public Point getHead() {
        return body.getLast();
    }

    public void addHeadPoint(Point newHeadPoint) {
        body.add(newHeadPoint);
    }

    public void checkSize() {
        if (body.size() > desiredSize) {
            body.removeFirst();
        }
    }

    @Override
    public void draw(Graphics g) {
        Iterator<Point> iterator = body.iterator();
        Point point = null;
        while (true) {
            point = iterator.next();
            if (iterator.hasNext()) {
                drawBody(g, point);
            } else {
                drawHead(g, point);
                break;
            }
        }
    }

    public int getDesiredSize() {
        return desiredSize;
    }

    private void drawHead(Graphics g, Point point) {
        g.drawImage(previousDirection.getImg(), point.getX() * CELL_SIZE, point.getY() * CELL_SIZE, null);
    }

    private void drawBody(Graphics g, Point point) {
        g.drawImage(BODY.getImg(), point.getX() * CELL_SIZE, point.getY() * CELL_SIZE, null);
    }

    public boolean isFree(Point point) {
        return !body.contains(point);
    }

    public Deque<Point> getBody() {
        return body;
    }
}
