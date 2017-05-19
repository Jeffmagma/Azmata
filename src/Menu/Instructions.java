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
    /** The background image of the instructions */
    private Image background;
    /** The current page the user is looking at */
    private int current_instruction;
    /** The lines of text in the instructions */
    //TODO: Replace with images on release
    private String[][] instructions;
    /** If the user wants to quit the game */
    private boolean quit;

    /**
     * Creates an instance of the Instructions class
     */
    public Instructions() {
        System.out.println("Instructions Created");
        current_instruction = 0;
        background = Azmata.imageFromFile("instructions_background.png");
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
            }
        });
        input_map.put(KeyStroke.getKeyStroke("LEFT"), "previous_page");
        input_map.put(KeyStroke.getKeyStroke('a'), "previous_page");
        input_map.put(KeyStroke.getKeyStroke('h'), "previous_page");
        getActionMap().put("previous_page", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (current_instruction > 0) current_instruction--;
            }
        });
    }

    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(background, 0, 0, null);
        for (String line : instructions[current_instruction]) g.drawString(line, 0, 0);
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