package Menu;

import Main.Azmata;

import java.awt.*;

/**
 * A class that represents an option in the menu
 */
public class MenuOption {
    /** the location that the image should be displayed at */
    public Point location;
    /** The image of the menu option */
    public Image image;

    /**
     * Constructs a menu option, with an image and a location
     *
     * @param image_path The path to the image
     * @param location   The location on the screen to draw the option
     */
    public MenuOption(String image_path, Point location) {
        image = Azmata.imageFromFile(image_path);
        this.location = location;
    }

    /**
     * Draws the current menu option
     */
    public void draw() {
        Azmata.graphics.drawImage(image, location.x, location.y, null);
    }

    /**
     * Draws the current menu option, bigger
     */
    void drawBigger() {
        Azmata.debug("drawn bigger");
        Azmata.graphics.drawImage(image, location.x - 20, location.y - 20, image.getWidth(null) + 40, image.getHeight(null) + 40, null);
    }
}
