package Game;

import java.awt.*;

/**
 * A 4-way [theres a word for this] direction TODO
 */
public enum Direction {
    DOWN, LEFT, RIGHT, UP;

    Point to(Point start) {
        switch (this) {
            case DOWN: return new Point(start.x, start.y + 1);
            case LEFT: return new Point(start.x - 1, start.y);
            case RIGHT: return new Point(start.x + 1, start.y);
            case UP: return new Point(start.x, start.y - 1);
        }
        return start;
    }
}
