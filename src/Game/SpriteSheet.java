package Game;

import Main.Azmata;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * A set of sprites that a character can have
 */
public class SpriteSheet {
    static final int STANDING = 1;
    Image[][] sprites = new Image[4][3];
    Image face;

    public SpriteSheet(BufferedImage image, BufferedImage face) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                sprites[i][j] = image.getSubimage(j * Azmata.BLOCK_SIZE, i * Azmata.BLOCK_SIZE, Azmata.BLOCK_SIZE, Azmata.BLOCK_SIZE);
            }
        }
        this.face = face;
    }
}