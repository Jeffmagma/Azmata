package Main;

/**
 * A coordinate that stores the x and y values in {@code doubles}s.
 * Similar to {@link java.awt.geom.Point2D.Double}
 */
public class DoublePoint {
    /**
     * The [x, y] coordinates of this point
     */
    public double x, y;

    /**
     * Constructs a Double Point with specified coordinates
     *
     * @param x The x coordinate
     * @param y The y coordinate
     */
    public DoublePoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Make sure the position is whole numbers in case of floating point rounding errors
     */
    public void normalize() {
        x = Math.round(x);
        y = Math.round(y);
    }
}