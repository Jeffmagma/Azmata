package Menu;

import Game.Game;
import Game.Game.World;
import Main.Azmata;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class WorldMenu extends JPanel {
    private volatile boolean selected;
    private World selected_world;
    /** The array of menu options */
    private MenuOption[] options;
    /** The background image of the world selection menu */
    private Image background_image;

    public WorldMenu() {
        selected = false;
        World.prev_top = selected_world = World.EARTHLOO;
        InputMap input_map = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        // Create the menu options
        options = new MenuOption[4];
        options[World.EARTHLOO.value] = new MenuOption("Menu/earthloo.png", new Point(350, 220));
        options[World.FIRELOO.value] = new MenuOption("Menu/fireloo.png", new Point(360, 295));
        options[World.WATERLOO.value] = new MenuOption("Menu/options.png", new Point(370, 370));
        options[World.AIRLOO.value] = new MenuOption("Menu/instructions.png", new Point(355, 445));
        background_image = Azmata.imageFromFile("Menu/world_background.png");
        // Just in case, not sure if the input maps are retained from previous panels
        input_map.clear();
        getActionMap().clear();
        input_map.put(KeyStroke.getKeyStroke("RIGHT"), "next");
        input_map.put(KeyStroke.getKeyStroke("LEFT"), "prev");
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
                if (Game.state.unlocked() || selected_world != World.AIRLOO) selected = true;
                else JOptionPane.showMessageDialog(null, "You have not beaten the other 3 worlds!");
            }
        });
    }

    private AbstractAction option(String dir) {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switch (dir) {
                    case "next": selected_world = selected_world.next();
                        if (selected_world != World.AIRLOO) World.prev_top = selected_world;
                        break;
                    case "prev": selected_world = selected_world.prev();
                        if (selected_world != World.AIRLOO) World.prev_top = selected_world;
                        break;
                    case "top": selected_world = World.prev_top;
                        break;
                    case "bottom": selected_world = World.AIRLOO;
                        break;
                }
                Azmata.debug(selected_world.name());
                repaint();
            }
        };
    }

    @Override
    public void paintComponent(Graphics g) {
        Azmata.graphics = (Graphics2D) g;
        g.drawImage(background_image, 0, 0, null);
        // Make the airloo grayscale if it isn't unlocked, but wouldn't it just look the same?
        /*if (!Game.state.unlocked()) {
            Image original = options[World.AIRLOO.value].image;
            BufferedImage grayscale_airloo = new BufferedImage(original.getWidth(null), original.getHeight(null), BufferedImage.TYPE_BYTE_GRAY);
            Graphics2D graphics = grayscale_airloo.createGraphics();
            graphics.drawImage(original, 0, 0, null);
            options[World.AIRLOO.value].image = grayscale_airloo;
        }*/
        for (int i = 0; i < options.length; i++) {
            if (i == selected_world.value) {
                options[i].drawBigger();
            } else options[i].draw();
        }
    }

    /**
     * Returns the selected world, after the user has selected it
     *
     * @return The selected world, after the user has selected it
     */
    public World selected() {
        revalidate();
        repaint();
        while (!selected) ;
        return selected_world;
    }
}
