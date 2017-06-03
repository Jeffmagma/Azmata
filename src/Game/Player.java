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
        sprites = new SpriteSheet("Sprites/Characters/eric.png", "Sprites/Faces/eric.png");
        direction = Direction.UP;
    }

    /**
     * Draws the player on the screen
     */
    public void draw(int animation_state) {
        Azmata.graphics.drawImage(sprites.sprites()[direction.ordinal()][animation_state], Azmata.SCREEN_WIDTH / 2, Azmata.SCREEN_HEIGHT / 2, null);
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
                if (Game.state.player_pos.y + 1 >= Game.state.getMap().map[Game.state.player_pos.x].length)
                    return false;
                if (!Game.state.getMap().map[Game.state.player_pos.x][Game.state.player_pos.y + 1].can_walk)
                    return false;
                break;
            case LEFT:
                if (Game.state.player_pos.x - 1 < 0) return false;
                if (!Game.state.getMap().map[Game.state.player_pos.x - 1][Game.state.player_pos.y].can_walk)
                    return false;
                break;
            case RIGHT:
                if (Game.state.player_pos.x + 1 >= Game.state.getMap().map.length) return false;
                if (!Game.state.getMap().map[Game.state.player_pos.x + 1][Game.state.player_pos.y].can_walk)
                    return false;
                break;
            case UP:
                if (Game.state.player_pos.y - 1 < 0) return false;
                if (!Game.state.getMap().map[Game.state.player_pos.x][Game.state.player_pos.y - 1].can_walk)
                    return false;
                break;
        }
        return true;
    }
}