package Game;

import Main.Azmata;

/**
 * The player that the user will be playing
 */
public class Player extends Character {
    public void draw() {
        Azmata.graphics.drawImage(sprites.sprites[direction.ordinal()][SpriteSheet.STANDING], 0, 0, null);
    }
}
