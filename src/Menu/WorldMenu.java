package Menu;

import Game.Game.World;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class WorldMenu extends JPanel {
    private volatile boolean selected;
    private World selected_world;

    public WorldMenu() {
        selected = false;
        selected_world = World.EARTHLOO;
        InputMap input_map = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        // Just in case, not sure if the input maps are retained from previous panels
        input_map.clear();
        getActionMap().clear();
        input_map.put(KeyStroke.getKeyStroke("LEFT"), "next");
        input_map.put(KeyStroke.getKeyStroke("RIGHT"), "prev");
        input_map.put(KeyStroke.getKeyStroke("UP"), "top");
        input_map.put(KeyStroke.getKeyStroke("DOWN"), "bottom");
        input_map.put(KeyStroke.getKeyStroke("ENTER"), "select");
        getActionMap().put("next", option("next"));
        getActionMap().put("prev", option("prev"));
        getActionMap().put("top", option("top"));
        getActionMap().put("bottom", option("bottom"));
        getActionMap().put("select", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selected = true;
            }
        });
    }

    private AbstractAction option(String dir) {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switch (dir) {
                    case "next": selected_world = selected_world.next();
                    case "prev": selected_world = selected_world.prev();
                    case "top": selected_world = selected_world.top();
                    case "bottom": selected_world = selected_world.bottom();
                }
                repaint();
            }
        };
    }

    @Override
    public void paintComponent(Graphics g) {

    }


    public World selected() {
        revalidate();
        repaint();
        while (!selected) ;
        return selected_world;
    }
}
