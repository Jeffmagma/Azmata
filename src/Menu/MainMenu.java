package Menu;

import Main.Azmata;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class MainMenu extends JPanel {
    public boolean selected;
    public boolean has_saved_game;
    ObjectInputStream saved_game;
    /** The background image of the main menu */
    private Image background_image;
    /** The image of the menu selector/pointer */
    private Image selector_image;
    /** The index of the currently selected option */
    private MenuOption selected_option;
    /** The array of menu options */
    private Image[] options;
    /** The locations of where the menu options should be */
    private Point[] locations;

    /**
     * Constructs the game menu Panel
     */
    public MainMenu() {
        selected = false;
        InputMap input_map = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        selected_option = MenuOption.NEW_GAME;
        // Load the images of the menu options
        options = new Image[4];
        options[MenuOption.NEW_GAME.value] = Azmata.imageFromFile("Menu/menu_option.png");
        options[1] = Azmata.imageFromFile("Menu/menu_option.png");
        options[2] = Azmata.imageFromFile("Menu/menu_option.png");
        options[3] = Azmata.imageFromFile("Menu/menu_option.png");
        // Set the locations of the menu options
        locations = new Point[4];
        locations[0] = new Point(200, 100);
        locations[1] = new Point(200, 200);
        locations[2] = new Point(200, 300);
        locations[3] = new Point(200, 400);
        // Get the background image and the option selector image
        background_image = Azmata.imageFromFile("Menu/background.png");
        selector_image = Azmata.imageFromFile("Menu/selector.png");
        // Clear the input and action maps
        getInputMap().clear();
        getActionMap().clear();
        // Set the arrow keys to select the next/previous menu options
        input_map.put(KeyStroke.getKeyStroke("UP"), "previous_option");
        input_map.put(KeyStroke.getKeyStroke('k'), "previous_option");
        input_map.put(KeyStroke.getKeyStroke('w'), "previous_option");
        getActionMap().put("previous_option", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selected_option = selected_option.prev();
                repaint();
            }
        });
        input_map.put(KeyStroke.getKeyStroke("DOWN"), "next_option");
        input_map.put(KeyStroke.getKeyStroke('j'), "next_option");
        input_map.put(KeyStroke.getKeyStroke('s'), "next_option");
        getActionMap().put("next_option", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selected_option = selected_option.next();
                repaint();
            }
        });
        input_map.put(KeyStroke.getKeyStroke("ENTER"), "select_option");
        getActionMap().put("select_option", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (has_saved_game || selected_option != MenuOption.CONTINUE_GAME) selected = true;
            }
        });

        try {
            saved_game = new ObjectInputStream(new FileInputStream(new File("save.xd")));
            has_saved_game = true;
        } catch (IOException e) {
            has_saved_game = false;
            if (Azmata.DEBUGGING) e.printStackTrace();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(background_image, 0, 0, null);
        g.drawImage(selector_image, locations[selected_option.value].x - 25 - selector_image.getWidth(null), locations[selected_option.value].y, null);
        for (int i = 0; i < options.length; i++) {
            g.drawImage(options[i], locations[i].x, locations[i].y, null);
        }
    }

    /**
     * Gets the selected option, when it is selected
     *
     * @return The selected option after it is selected
     */
    public MenuOption getSelected() {
        while (!selected) ;
        return selected_option;
    }

    /**
     * A enum to represent the current menu option
     */
    public enum MenuOption {
        /** The new game option */
        NEW_GAME,
        /** The continue saved game option */
        CONTINUE_GAME,
        /** The options option */
        OPTIONS,
        /** The instructions option */
        INSTRUCTIONS;
        int value = ordinal();

        /**
         * Returns the next menu option, but the current one if it's the last one
         *
         * @return The next menu option, but the current one if it's the last one
         */
        public MenuOption next() {
            return values()[value + 1 < values().length ? value + 1 : value];
        }

        /**
         * Returns the previous menu option, but the current one if it's the first one
         *
         * @return The previous menu option, but the current one if it's the first one
         */
        public MenuOption prev() {
            return values()[value > 0 ? value - 1 : value];
        }
    }
}
