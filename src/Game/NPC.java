package Game;

import java.awt.*;

/**
 * This class represents any non-playable character (NPC) that can exist in the game
 */
public abstract class NPC extends Character {
    /** The distance that you have to be from the character for some interaction to occur */
    final int pass_distance;
    /** Where the NPC is on the map */
    Point position;

    /**
     * Creates an NPC with a specified pass distance
     *
     * @param pass_distance How far you have to be away from the face of an NPC for an interaction to occur
     */
    public NPC(int pass_distance) {
        this.pass_distance = pass_distance;
        direction = Direction.UP;
        position = new Point(3, 3);
    }

    /**
     * What happens when you walk up to the NPC and press enter
     */
    public abstract void onTalk();

    public abstract void onPass();

    public void draw() {

    }

    public void move(int distance) {
        for (int i = 0; i < distance * 32; i++) {
            switch (direction) {
                case DOWN: break;
                case LEFT: break;
                case RIGHT: break;
                case UP: break;
            }
        }
    }
}