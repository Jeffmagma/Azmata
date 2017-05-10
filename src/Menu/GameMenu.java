package Menu;

import javax.swing.*;
import java.awt.*;

public class GameMenu extends JPanel {
    Image background_image;

    /**
     * Constructs the game menu Panel
     */
    public GameMenu() {
        super();
    }

    @Override
    public void paintComponent(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(0, 0, 550, 505);
        g.setColor(Color.BLACK);
        g.drawString("PLACEHOLDHAHSKGD MENU THIKNGSG", 50, 50);
    }
}
