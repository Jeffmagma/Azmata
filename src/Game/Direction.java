package Game;

import java.awt.*;
import java.io.Serializable;

/**
 * Represents a cardinal? direction
 */
public enum Direction implements Serializable {
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

    Direction opposite() {
        switch (this) {
            case DOWN: return UP;
            case LEFT: return RIGHT;
            case RIGHT: return LEFT;
            case UP: return DOWN;
            default: throw new IllegalStateException("How did you add another direction?");
        }
    }
}
