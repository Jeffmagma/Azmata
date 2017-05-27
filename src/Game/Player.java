package Game;

import Main.Azmata;

/**
 * The player that the user will be playing
 */
public class Player extends Character {
    public Player() {
        sprites = new SpriteSheet(Azmata.imageFromFile("Game/test.png"));
        direction = Direction.UP;
    }

    public void draw() {
        Azmata.graphics.drawImage(sprites.sprites[direction.ordinal()][SpriteSheet.STANDING], Azmata.SCREEN_WIDTH / 2, Azmata.SCREEN_HEIGHT / 2, null);
    }
}
