package Menu;

import Main.Azmata;

import javax.swing.*;
import java.awt.*;

public class Instructions extends JPanel {
    Image background;
    int current_instruction;
    String[][] instructions;

    public Instructions() {
        current_instruction = 0;
        background = Azmata.imageFromFile("instructions_background.png");
    }

    @Override
    public void paintComponent(Graphics g) {
        for (String line : instructions[current_instruction]) g.drawString(line, 0, 0);
    }
}
