package Game;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a tile on the map
 *
 * @author Jeffrey Gao
 * @author Richard Yi
 */
public class Tile {
    /** If the player can walk on the tile */
    boolean can_walk;
    /** The images on this tile, from first drawn (bottom-most) to last drawn (top-most) */
    private List<Image> images;
    /** The character on the tile, null if there is none */
    private Character character;

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
     * Gets the list of images
     *
     * @return the list of images
     */
    List<Image> getImages() {
        return images;
    }

    /**
     * Draws the tile
     *
     * @param point the point to draw it at
     */
    public void draw(Point point, Graphics2D g) {
        for (Image i : images) {
            g.drawImage(i, point.x, point.y, null);
        }
    }
}
