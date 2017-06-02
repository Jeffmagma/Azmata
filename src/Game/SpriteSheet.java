package Game;

import Main.Azmata;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * A set of sprites that a character can have
 */
public class SpriteSheet {
    /** The standing position of all the sprite images */
    static final int STANDING = 1;
    /** The array of all the sprites, the first index for image, the second for animation state */
    Image[][] sprites = new Image[4][3];
    /** The image of the character's face */
    Image face;

    /**
     * Constructs a sprite sheet with paths to the small sprites and face sprite
     *
     * @param image The path to their sheet
     * @param face  The path to their face
     */
    public SpriteSheet(String image, String face) {
        BufferedImage sheet = Azmata.imageFromFile(image);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                sprites[i][j] = sheet.getSubimage(j * Azmata.BLOCK_SIZE, i * Azmata.BLOCK_SIZE, Azmata.BLOCK_SIZE, Azmata.BLOCK_SIZE);
            }
        }
        this.face = Azmata.imageFromFile(face);
    }
}