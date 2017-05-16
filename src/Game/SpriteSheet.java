package Game;

import Main.Azmata;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * A set of sprites that a character can have
 */
public class SpriteSheet {
    public static final int STANDING = 1;
    public Image[][] sprites = new Image[4][3];

    public SpriteSheet(BufferedImage image) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                sprites[i][j] = image.getSubimage(j * Azmata.BLOCK_SIZE, i * Azmata.BLOCK_SIZE, Azmata.BLOCK_SIZE, Azmata.BLOCK_SIZE);
            }
        }
    }
}
