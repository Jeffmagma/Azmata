package Game;

import Main.Azmata;

import java.awt.*;

/**
 * This class represents any non-playable character (NPC) that can exist in the game
 */
public abstract class NPC extends Character {
    /** Where the NPC is on the map */
    Point position;

    /**
     * Creates an NPC with a specified pass distance
     *
     * @param position Which tile on the map the NPC is on
     */
    public NPC(Point position, SpriteSheet sprites) {
        direction = Direction.UP;
        this.sprites = sprites;
        this.position = position;
    }

    /**
     * What happens when you walk up to the NPC and press enter
     */
    public abstract void onTalk();

    public void battle() {

    }

    /**
     * Draw the NPC based in its position
     */
    public void draw() {
        Point draw_point = Game.getRelativePosition(position);
        Azmata.graphics.drawImage(sprites.sprites[Direction.DOWN.ordinal()][SpriteSheet.STANDING], draw_point.x, draw_point.y, null);
    }
}