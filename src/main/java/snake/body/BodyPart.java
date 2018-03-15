package snake.body;

import snake.general.Point;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

public enum BodyPart {
    TOP('w', new Point(0,-1), "TopHead.png"),
    BOTTOM('s', new Point(0,1), "BottomHead.png"),
    LEFT('a', new Point(-1,0), "LeftHead.png"),
    RIGHT('d', new Point(1,0), "RightHead.png"),
    BODY(null, null, "BodyPart.png"),
    CHERRY(null, null, "Cherry.png");

    private static final String imagesPath = "images/";

    private Character key;
    private Point velocity;
    private Image img;

    private BodyPart(Character key, Point velocity, String image) {
        this.key = key;
        this.velocity = velocity;
        try {
            this.img = ImageIO.read(ClassLoader.getSystemResourceAsStream(imagesPath + image));
        } catch (IOException e) {
            throw new IllegalArgumentException("unexistant image - " + image);
        }

    }

    public static Optional<BodyPart> getByKey(Character key) {
        return Arrays.stream(BodyPart.values())
                     .filter(each -> each.key.equals(Character.toLowerCase(key)))
                     .findAny();
    }

    public BodyPart getOpposite() {
        switch(this) {
            case TOP: return BOTTOM;
            case BOTTOM: return TOP;
            case LEFT: return RIGHT;
            case RIGHT: return LEFT;
            default: return null;
        }
    }

    public Point getVelocity() {
        return velocity;
    }

    public Image getImg() {
        return img;
    }
}
