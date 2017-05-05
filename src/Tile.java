import java.awt.image.BufferedImage;

public class Tile {
    BufferedImage image;
    boolean can_walk;
    Character character;

    public Tile(BufferedImage image, boolean can_walk, Character character) {
        this.image = image;
        this.can_walk = can_walk;
        this.character = character;
    }

    public Tile(BufferedImage image, boolean can_walk) {
        this(image, can_walk, null);
    }
}
