package Menu;

import Game.Game.World;

import javax.swing.*;

public class WorldMenu extends JPanel {
    volatile boolean selected;
    World selected_world;

    public WorldMenu() {
        selected = false;
        selected_world = World.EARTHLOO;
        InputMap input_map = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);

    }


    World selected() {
        while (!selected) ;
        return selected_world;
    }
}
