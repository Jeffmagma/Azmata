package Game;

import java.awt.*;

public class GameState {
    GameMap current_map;
    Point player_pos;

    public GameState(Point player_pos) {
        this.player_pos = player_pos;
    }
}
