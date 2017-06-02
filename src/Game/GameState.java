package Game;

import java.awt.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A state of the game, used for loading and saving the game
 */
public class GameState implements Serializable {
    static final Set<NPC>[] npc_sets = new Set[4];
    /** The position of the player */
    final Point player_pos;
    /** The current map */
    GameMap current_map;
    /** Which worlds the player has beaten */
    boolean[] beaten_worlds;
    /** The list of NPCs that are currently present in the game */
    Set<NPC> npc_list;

    /**
     * Constructs a game state with the player at a specified position
     *
     * @param player_pos The position of the player
     */
    public GameState(Point player_pos) {
        npc_sets[0] = new HashSet<>();
        this.player_pos = player_pos;
        npc_list = new HashSet<>();
        beaten_worlds = new boolean[] {false, false, false};
    }
}