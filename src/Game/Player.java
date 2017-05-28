package Game;

import Main.Azmata;

/**
 * The player that the user will be playing
 */
public class Player extends Character {
    /**
     * Constructs a player from a sprite
     */
    public Player() {
        sprites = new SpriteSheet(Azmata.imageFromFile("Game/test.png"));
        direction = Direction.UP;
    }

    /**
     * Draws the player on the screen
     */
    public void draw() {
        Azmata.graphics.drawImage(sprites.sprites[direction.ordinal()][SpriteSheet.STANDING], Azmata.SCREEN_WIDTH / 2, Azmata.SCREEN_HEIGHT / 2, null);
    }

    public boolean canMove(Direction dir) {
        return true;
    }
}
