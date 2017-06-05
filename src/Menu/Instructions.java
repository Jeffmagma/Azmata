package Menu;

import Main.Azmata;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * A class that shows the instructions to the user
 */
public class Instructions extends JPanel {
    /** The amount of pages of instructions */
    private final int MAX_INSTRUCTIONS = 4;
    /** The current page the user is looking at */
    private int current_instruction;
    /** The images containing the instructions */
    private Image[] instructions;
    /** If the user wants to quit the game */
    private boolean quit;

    /**
     * Creates an instance of the Instructions class
     *
     * @author Jeffrey Gao
     */
    public Instructions() {
        Azmata.debug("Instructions Constructed");
        current_instruction = 0;
        quit = false;
        InputMap input_map = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        input_map.put(KeyStroke.getKeyStroke("ESCAPE"), "quit");
        getActionMap().put("quit", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                quit = true;
            }
        });
        input_map.put(KeyStroke.getKeyStroke("RIGHT"), "next_page");
        input_map.put(KeyStroke.getKeyStroke('d'), "next_page");
        input_map.put(KeyStroke.getKeyStroke('l'), "next_page");
        getActionMap().put("next_page", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (current_instruction < MAX_INSTRUCTIONS - 1) current_instruction++;
                repaint();
            }
        });
        input_map.put(KeyStroke.getKeyStroke("LEFT"), "previous_page");
        input_map.put(KeyStroke.getKeyStroke('a'), "previous_page");
        input_map.put(KeyStroke.getKeyStroke('h'), "previous_page");
        getActionMap().put("previous_page", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (current_instruction > 0) current_instruction--;
                repaint();
            }
        });
    }

    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(instructions[current_instruction], 0, 0, null);
    }

    /**
     * Show the instructions, returning when the user decides to quit
     */
    public void show() {
        revalidate();
        repaint();
        while (!quit) ;
    }
}