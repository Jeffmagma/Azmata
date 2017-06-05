package Game;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a tile on the map
 *
 * @author Jeffrey Gao
 */
public class Tile implements Serializable {
    /** The images on this tile, from first drawn (bottom-most) to last drawn (top-most) */
    public List<Image> images;
    /** If the player can walk on the tile */
    boolean can_walk;

    /**
     * Creates a tile with a specification if you can/can't walk on it
     *
     * @param can_walk If you can walk on this tile
     */
    public Tile(boolean can_walk) {
        this.can_walk = can_walk;
        images = new ArrayList<>();
    }

    /**
     * Draws the tile
     *
     * @param point the point to draw it at
     * @param g     The graphics to draw with (because double buffering)
     */
    public void draw(Point point, Graphics g) {
        for (Image i : images) {
            g.drawImage(i, point.x, point.y, null);
        }
    }
}
