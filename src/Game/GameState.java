package Game;

import Main.DoublePoint;

import java.io.Serializable;

/**
 * A state of the game, used for loading and saving the game
 */
public class GameState implements Serializable {
    /** The current map */
    GameMap current_map;
    /** The position of the player */
    DoublePoint player_pos;

    /**
     * Constructs a game state with the player at a specified position
     *
     * @param player_pos The position of the player
     */
    public GameState(DoublePoint player_pos) {
        this.player_pos = player_pos;
    }
}
