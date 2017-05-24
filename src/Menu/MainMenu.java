package Menu;

import Main.Azmata;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * A class for the main menu
 */
public class MainMenu extends JPanel {
    /** An ObjectInputStream that reads from the saved game */
    public ObjectInputStream saved_game;
    /** If valid option has been selected */
    private boolean selected;
    /** If the user has a saved game */
    private boolean has_saved_game;
    /** The background image of the main menu */
    private Image background_image;
    /** The image of the menu selector/pointer */
    private Image selector_image;
    /** The index of the currently selected option */
    private Option selected_option;
    /** The array of menu options */
    private MenuOption[] options;

    /**
     * Constructs the game menu Panel
     */
    public MainMenu() {
        selected = false;
        InputMap input_map = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        selected_option = Option.NEW_GAME;
        // Create the menu options
        options = new MenuOption[4];
        options[Option.NEW_GAME.value] = new MenuOption("Menu/menu_option.png", new Point(200, 100));
        options[Option.CONTINUE_GAME.value] = new MenuOption("Menu/menu_option.png", new Point(200, 200));
        options[Option.OPTIONS.value] = new MenuOption("Menu/menu_option.png", new Point(200, 300));
        options[Option.INSTRUCTIONS.value] = new MenuOption("Menu/menu_option.png", new Point(200, 400));
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
        // The enter button selects an option if it's valid
        input_map.put(KeyStroke.getKeyStroke("ENTER"), "select_option");
        getActionMap().put("select_option", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (has_saved_game || selected_option != Option.CONTINUE_GAME) selected = true;
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
        Azmata.graphics = (Graphics2D) g;
        g.drawImage(background_image, 0, 0, null);
        for (MenuOption option : options) {
            option.draw();
        }
        g.drawImage(selector_image, options[selected_option.value].location.x - 25 - selector_image.getWidth(null), options[selected_option.value].location.y, null);
    }

    /**
     * Gets the selected option, when it is selected
     *
     * @return The selected option after it is selected
     */
    public Option getSelected() {
        revalidate();
        repaint();
        while (!selected) ;
        System.out.println("here");
        return selected_option;
    }

    /**
     * A enum to represent the current menu option
     */
    public enum Option {
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
        public Option next() {
            return values()[value + 1 < values().length ? value + 1 : value];
        }

        /**
         * Returns the previous menu option, but the current one if it's the first one
         *
         * @return The previous menu option, but the current one if it's the first one
         */
        public Option prev() {
            return values()[value > 0 ? value - 1 : value];
        }
    }
}