package Game;

import java.awt.*;
import java.util.List;

/**
 * This class represents a tile on the map
 *
 * @author Jeffrey Gao
 * @author Richard Yi
 */
public class Tile {
    /** The images on this tile, from first drawn (bottom-most) to last drawn (top-most) */
    private List<Image> images;
    /** If the player can walk on the tile */
    private boolean can_walk;
    /** The character on the tile, null if there is none */
    private Character character;

    public Tile(boolean can_walk) {
        this.can_walk = can_walk;
    }

    public List<Image> getImages() {
        return images;
    }

    public void draw() {

    }
}
