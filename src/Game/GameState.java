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
    public Set<NPC> npc_list;
    /** The name of the map, so that it can be retrieved later */
    public String map_name;
    /** The index of the next question */
    public int question;
    /** The amount of health the player has */
    public double health;
    /** The player's score */
    public long score;
    /** If the user is currently inside a game world */
    boolean in_game;
    /** Which worlds the player has beaten */
    boolean[] beaten_worlds;
    /** The world of the current game */
    Game.World world;
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
        beaten_worlds = new boolean[]{false, false, false};
        in_game = true;
        health = 100.0;
        score = 0;
        question = 0;
        this.world = world;
        for (Point p : world.npcPoints()) {
            String sheet, battler;
            switch ((int) (Math.random() * 3)) {
                case 0:
                    sheet = "Orc.png";
                    battler = "Orc.png";
                    break;
                case 1:
                    sheet = "ghost.png";
                    battler = "Ghost.png";
                    break;
                case 2:
                default:
                    sheet = "zombie_1.png";
                    battler = "Zombie.png";
            }
            npc_list.add(new NPC(p, new SpriteSheet(sheet, "eric.png"), battler) {
                @Override
                public void onTalk() {
                    battling = true;
                }
            });
        }
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