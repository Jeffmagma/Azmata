package Game;

import Main.Azmata;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.Serializable;

/**
 * A set of sprites that a character can have
 */
public class SpriteSheet implements Serializable {
    /** The standing position of all the sprite images */
    static final int STANDING = 1;
    /** The array of all the sprites, the first index for image, the second for animation state */
    private transient Image[][] sprites;
    /** The image of the character's face */
    private transient Image face;
    private String sheet_path;
    private String face_path;

    /**
     * Constructs a sprite sheet with paths to the small sprites and face sprite
     *
     * @param image The path to their sheet
     * @param face  The path to their face
     */
    public SpriteSheet(String image, String face) {
        sheet_path = image;
        face_path = face;
    }

    public Image[][] sprites() {
        if (sprites == null) {
            sprites = new Image[4][3];
            BufferedImage sheet = Azmata.imageFromFile(sheet_path);
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 3; j++) {
                    sprites[i][j] = sheet.getSubimage(j * Azmata.BLOCK_SIZE, i * Azmata.BLOCK_SIZE, Azmata.BLOCK_SIZE, Azmata.BLOCK_SIZE);
                }
            }
        }
        return sprites;
    }

    Image face() {
        if (face != null) return face;
        return face = Azmata.imageFromFile(face_path);
    }
}