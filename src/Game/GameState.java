package Game;

import java.awt.*;
import java.io.Serializable;

public class GameState implements Serializable {
    GameMap current_map;
    Point player_pos;

    public GameState(Point player_pos) {
        this.player_pos = player_pos;
    }
}
