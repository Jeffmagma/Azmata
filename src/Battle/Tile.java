package Battle;

import java.awt.*;

/**
 * Tile class to keep track of the on-screen Tile information
 * <h2>Course Info</h2>
 * <i>ICS4U0 with Mrs. Krasteva</i>
 *
 * @author Richard Yi
 * @version 1.1
 */

/*
Modifications:

Richard Yi
Version 1.2
Time Spent: 0.5 hours
Added blowing (where a tile just keeps moving left) was added

Richard Yi
Version 1.1
Time Spent: 0.5 hours
Reworked the class so that the color is randomly generated
Removed unused getter/setters.

Richard Yi
Version 1.0
Time Spent: 1 hour
Set up the class, with just a framework, made variables
and getters/setters
 */
public class Tile {
    /** All colors of the rainbow. All colors that can be spawned */
    private static final Color[] COLORS = {Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.BLUE, Color.MAGENTA};
    /** The letter that the tile displays. */
    private char letter;
    /** The current X-coordinate of the tile. */
    private int x;
    /** the current y-coordinate of the tile. */
    private int y;
    /** The size of the tile. */
    private int size;
    /** How many ticks the tile has been alive for. */
    private int age;
    /** The color of the tile. */
    private Color color;
    /** Whether or not the tile is being blown. */
    private boolean blowing;

    /** Main and only constructor. The tile is initialized with its letter, initial position, size, and a random color. */
    public Tile(char l, int initX, int initY, int initSize) {
        letter = l;
        x = initX;
        y = initY;
        size = initSize;
        blowing = false;
        color = COLORS[(int) (Math.random() * 6)];
    }

    /** Test main method for testing out hash code. */
    public static void main(String[] args) { //TODO: Remove this method
        Tile t = new Tile('A', 100, 200, 50);
        Tile u = new Tile('X', 69, 420, 69);
        System.out.println(t.hashCode());
        System.out.println(u.hashCode());
    }

    /** Increment how long the tile has been alive for */
    public void tick() {
        ++age;
    }

    /** Move the tile along the x-axis by an integral amount */
    public void moveX(int dx) {
        x += dx;
        if (x - size / 2 < Battle.MAIN_LEFT) x = Battle.MAIN_LEFT + size / 2;
        if (x + size / 2 > Battle.MAIN_RIGHT) x = Battle.MAIN_RIGHT - size / 2;
    }

    /** Move the tile along the y-axis by an integral amount */
    public void moveY(int dy) {
        y += dy;
        if (y - size / 2 < Battle.MAIN_TOP) y = Battle.MAIN_TOP + size / 2;
        if (y + size / 2 > Battle.MAIN_BOTTOM) y = Battle.MAIN_BOTTOM - size / 2;
    }

    /** Accessor method for letter. */
    public char getLetter() {
        return letter;
    }

    /**
     * Accessor method for the x-coordinate.
     *
     * @return The x-coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Accessor method for the y-coordinate.
     *
     * @return The y-coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Accessor method for the size of the tile
     *
     * @return The size of the tile
     */
    public int getSize() {
        return size;
    }

    /**
     * Accessor method for the color of the tile
     *
     * @return The color of the tile
     */
    public Color getColor() {
        return color;
    }

    /**
     * Accessor method for the age of the tile
     *
     * @return The age of the tile
     */
    public int getAge() {
        return age;
    }

    /** Flag the tile as "blowing" (will be moved differently) */
    public void startBlowing() {
        blowing = true;
    }

    /** Stop flagging the tile as "blowing" (will be moved differently) */
    public void stopBlowing() {
        blowing = false;
    }

    /**
     * Accessor method for the x-coordinate.
     *
     * @return The x-coordinate
     */
    public boolean isBlowing() {
        return blowing;
    }
}
