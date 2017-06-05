package Menu;

import Main.Azmata;

import java.awt.*;

/**
 * A class that represents an option in the menu
 *
 * @author Jeffrey Gao
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
        Dimension size = new Dimension(image.getWidth(null), image.getHeight(null));
        Dimension newsize = new Dimension((int) (image.getWidth(null) * 1.1), (int) (image.getHeight(null) * 1.1));
        Azmata.graphics.drawImage(image, location.x - ((newsize.width - size.width) / 2), location.y - ((newsize.height - size.height) / 2), newsize.width, newsize.height, null);
    }
}