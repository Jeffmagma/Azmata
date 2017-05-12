package Menu;

import Main.Azmata;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class GameMenu extends JPanel {
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
    public GameMenu() {
        super();
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
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("UP"), "previous_option");
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('k'), "previous_option");
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('w'), "previous_option");
        getActionMap().put("previous_option", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selected_option = selected_option.prev();
                repaint();
            }
        });
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("DOWN"), "next_option");
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('j'), "next_option");
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('s'), "next_option");
        getActionMap().put("next_option", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selected_option = selected_option.next();
                repaint();
            }
        });
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ENTER"), "select_option");
        getActionMap().put("select_option", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switch (selected_option) {
                    case NEW_GAME:
                        break;
                    case CONTINUE_GAME:
                        break;
                    case OPTIONS:
                        break;
                    case INSTRUCTIONS:
                        break;
                }
            }
        });
    }

    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(background_image, 0, 0, null);
        g.drawImage(selector_image, locations[selected_option.value].x - 25 - selector_image.getWidth(null), locations[selected_option.value].y, null);
        for (int i = 0; i < options.length; i++) {
            g.drawImage(options[i], locations[i].x, locations[i].y, null);
        }
    }

    enum MenuOption {
        NEW_GAME, CONTINUE_GAME, OPTIONS, INSTRUCTIONS;
        int value = ordinal();

        public MenuOption next() {
            return values()[ordinal() + 1 < values().length ? ordinal() + 1 : ordinal()];
        }

        public MenuOption prev() {
            return values()[ordinal() > 0 ? ordinal() - 1 : ordinal()];
        }
    }
}
