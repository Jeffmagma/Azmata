import java.awt.*;

/**
 * This class represents a tile on the map
 *
 * @author Jeffrey Gao
 * @author Richard Yi
 */
public class Tile {
    private Image image;
    private boolean can_walk;
    private Character character;

    /**
     * Constructs a Tile
     *
     * @param image     The image of the tile
     * @param can_walk  If the player can walk on top of the tile
     * @param character The character on the tile (null if there is no character)
     */
    public Tile(Image image, boolean can_walk, Character character) {
        this.image = image;
        this.can_walk = can_walk;
        this.character = character;
    }

    /**
     * Constructs a Tile
     *
     * @param image    The image of the tile
     * @param can_walk If the player can walk on the tile
     */
    public Tile(Image image, boolean can_walk) {
        this(image, can_walk, null);
    }
}
