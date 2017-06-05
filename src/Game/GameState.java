package Game;

import java.awt.*;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * A state of the game, used for loading and saving the game
 *
 * @author Jeffrey Gao
 */
public class GameState implements Serializable {
    /** The position of the player */
    final Point player_pos;
    /** The list of NPCs that are currently present in the game */
    Set<NPC> npc_list;
    /** Which worlds the player has beaten */
    private boolean[] beaten_worlds;
    /** The name of the map, so that it can be retrieved later */
    private String map_name;
    /** The current map */
    private transient GameMap current_map;

    /**
     * Constructs a game state with the player at a specified position
     *
     * @param world The world that the player is currently in
     */
    public GameState(Game.World world) {
        map_name = world.getMapName();
        player_pos = world.getStartingPoint();
        npc_list = new HashSet<>();
        beaten_worlds = new boolean[] {false, false, false};
    }

    /**
     * Constructs the map if it hasn't been constructed, and returns it
     *
     * @return The game map
     */
    public GameMap getMap() {
        if (current_map != null) return current_map;
        return current_map = new GameMap("Maps/" + map_name);
    }

    /**
     * If the player has unlocked the last world
     *
     * @return Whether the player has unlocked the last world
     */
    public boolean unlocked() {
        return !Arrays.asList(Game.state.beaten_worlds).contains(false);
    }
}