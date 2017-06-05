package Game;

import java.awt.*;
import java.io.Serializable;

/**
 * Represents a cardinal? direction
 *
 * @author Jeffrey Gao
 */
public enum Direction implements Serializable {
    DOWN, LEFT, RIGHT, UP;

    /**
     * Returns a point one away in the current direction
     *
     * @param start The point to start moving from
     * @return A point one away in the current direction
     */
    Point to(Point start) {
        switch (this) {
            case DOWN: return new Point(start.x, start.y + 1);
            case LEFT: return new Point(start.x - 1, start.y);
            case RIGHT: return new Point(start.x + 1, start.y);
            case UP: return new Point(start.x, start.y - 1);
        }
        return start;
    }

    /**
     * Returns the direction opposite of the current one
     *
     * @return the direction opposite of the current one
     */
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
