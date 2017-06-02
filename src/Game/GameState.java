package Game;

import java.awt.*;
import java.io.Serializable;

/**
 * A state of the game, used for loading and saving the game
 */
public class GameState implements Serializable {
    /** The position of the player */
    final Point player_pos;
    /** The current map */
    GameMap current_map;
    public double health;

    /**
     * Constructs a game state with the player at a specified position
     *
     * @param player_pos The position of the player
     */
    public GameState(Point player_pos, double health) {
        this.player_pos = player_pos;
        this.health = health;
    }
}