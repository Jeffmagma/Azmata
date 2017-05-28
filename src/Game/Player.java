package Game;

import Main.Azmata;

/**
 * The player that the user will be playing
 */
public class Player extends Character {
    /**
     * The current state of the game, so that the player can access it
     */
    GameState state;

    /**
     * Constructs a player from a sprite
     */
    public Player(GameState state) {
        sprites = new SpriteSheet(Azmata.imageFromFile("Game/player.png"));
        direction = Direction.UP;
        this.state = state;
    }

    /**
     * Draws the player on the screen
     */
    public void draw(int animation_state) {
        Azmata.graphics.drawImage(sprites.sprites[direction.ordinal()][animation_state], Azmata.SCREEN_WIDTH / 2, Azmata.SCREEN_HEIGHT / 2, null);
    }

    /**
     * If the player can move in a certain direction
     *
     * @param dir The direction the player will move in
     * @return if the player can move in that direction
     */
    boolean canMove(Direction dir) {
        switch (dir) {
            case DOWN:
                if (state.player_pos.y + 1 >= state.current_map.map[state.player_pos.intX()].length) return false;
                if (!state.current_map.map[(int) state.player_pos.x][(int) state.player_pos.y + 1].can_walk)
                    return false;
                break;
            case LEFT:
                if (state.player_pos.x - 1 < 0) return false;
                if (!state.current_map.map[(int) state.player_pos.x - 1][(int) state.player_pos.y].can_walk)
                    return false;
                break;
            case RIGHT:
                if (state.player_pos.x + 1 >= state.current_map.map.length) return false;
                if (!state.current_map.map[(int) state.player_pos.x + 1][(int) state.player_pos.y].can_walk)
                    return false;
                break;
            case UP:
                if (state.player_pos.y - 1 < 0) return false;
                if (!state.current_map.map[(int) state.player_pos.x][(int) state.player_pos.y - 1].can_walk)
                    return false;
                break;
        }
        return true;
    }
}
