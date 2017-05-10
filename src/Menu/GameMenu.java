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
    private int selected_option;
    /** The array of menu options */
    private MenuOption[] options;
    /** The locations of where the menu options should be */
    private Point[] locations;

    /**
     * Constructs the game menu Panel
     */
    public GameMenu() {
        super();
        // Allow placement by coordinates
        setLayout(null);
        // Load the images of the menu options
        options = new MenuOption[4];
        options[0] = new MenuOption(Azmata.imageFromFile("Main/azmata.png"));
        options[1] = new MenuOption(Azmata.imageFromFile("Main/azmata.png"));
        options[2] = new MenuOption(Azmata.imageFromFile("Main/azmata.png"));
        options[3] = new MenuOption(Azmata.imageFromFile("Main/azmata.png"));
        // Set the locations of the menu options
        locations = new Point[4];
        locations[0] = new Point(200, 100);
        locations[1] = new Point(200, 200);
        locations[2] = new Point(200, 300);
        locations[3] = new Point(200, 400);
        for (int i = 0; i < options.length; i++) {
            options[i].setLocation(locations[i]);
            options[i].revalidate();
        }
        background_image = Azmata.imageFromFile("Menu/background.png");
        selector_image = Azmata.imageFromFile("Menu/selector.png");
        getInputMap().clear();
        getActionMap().clear();
        // Set the arrow keys to select the next/previous menu options
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("UP"), "previous_option");
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('K'), "previous_option");
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('W'), "previous_option");
        getActionMap().put("previous_option", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selected_option > 0) selected_option--;
                repaint();
            }
        });
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("DOWN"), "next_option");
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('J'), "next_option");
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('S'), "next_option");
        getActionMap().put("next_option", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selected_option < options.length - 1) selected_option++;
                repaint();
            }
        });
    }

    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(background_image, 0, 0, null);
        g.drawImage(selector_image, locations[selected_option].x - 50 - selector_image.getWidth(null), locations[selected_option].y, null);
        for (MenuOption o : options) {
            o.revalidate();
        }
    }
}
