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
    private List<Image> images;
    private Image image;
    /** If the player can walk on the tile */
    private boolean can_walk;
    /** The character on the tile, null if there is none */
    private Character character;

    /**
     * Constructs a Game.Tile
     *
     * @param image     The image of the tile
     * @param can_walk  If the player can walk on top of the tile
     * @param character The character on the tile (null if there is no character)
     */
    public Tile(Image image, boolean can_walk, Character character) {
        images = new ArrayList<>();
        this.image = image;
        this.can_walk = can_walk;
        this.character = character;
    }

    /**
     * Constructs a Game.Tile
     *
     * @param image    The image of the tile
     * @param can_walk If the player can walk on the tile
     */
    public Tile(Image image, boolean can_walk) {
        this(image, can_walk, null);
    }

    public void draw() {
        for (Image i : images) ;
    }
}
