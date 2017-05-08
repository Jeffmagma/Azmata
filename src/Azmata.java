import Menu.GameMenu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

/**
 * States that the program can be in during or before the main menu
 */
enum State {
    /** When displaying the company logo */
    LOGO,
    /** When displaying the name of the game */
    NAME,
    /** While in the main menu */
    MAIN_MENU;

    /**
     * Gets the next state, if there is one
     *
     * @return The next state
     */
    public State next() {
        return values()[ordinal() + 1 < values().length ? ordinal() + 1 : ordinal()];
    }
}

public class Azmata {
    /** The size, in pixels, of a square in the grid of the game */
    private static final int BLOCK_SIZE = 32;
    /** The scale we want for the width of the screen (16 because we want 16:9) */
    private static final int SCALE_X = 16;
    /** The scale we want for the height of the screen (9 because we want 16:9) */
    private static final int SCALE_Y = 9;
    private static final int SCALE = 2;
    private static JFrame frame;
    private static JPanel panel;
    private static Graphics g;
    private static State current_state;

    /**
     * Initalize the JFrame and JPanel to be able to use in the rest of the program
     */
    private static void initialize() {
        // Construct the JFrame
        frame = new JFrame();
        // Construct a new JPanel
        panel = new JPanel();
        // Set the spacebar to move to the next splashscreen
        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("released SPACE"), "next_state");
        panel.getActionMap().put("next_state", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                current_state = current_state.next();
            }
        });
        // Set the size of the JFrame to be a 16:9 screen, and make sure it can hold 32x32 grid blocks evenly
        frame.getContentPane().setPreferredSize(new Dimension(BLOCK_SIZE * SCALE_X * SCALE, BLOCK_SIZE * SCALE_Y * SCALE));
        // Add the panel which will contains the content
        frame.add(panel);
        // Make sure the frame is the right size
        frame.pack();
        // Center the frame
        frame.setLocationRelativeTo(null);
        // Set the JFrame to exit on close
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        // Stop users from resizing the JFrame (we need to preserve the scale)
        frame.setResizable(false);
        // Show the frame
        frame.setVisible(true);
        // Get the graphics that will be used to draw
        g = panel.getGraphics();
    }

    public static void main(String[] args) {
        initialize();
        drawLogo();
        drawName();
        current_state = State.MAIN_MENU;
        panel = new GameMenu();
        panel.revalidate();
        System.out.println(panel.getGraphics() == null);
    }

    /**
     * Sleep for a certain amount of time
     *
     * @param time The amount of time to sleep
     */
    private static void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Draws the name of the game (Azmata) on the screen
     */
    private static void drawName() {
        current_state = State.NAME;
        Font title_font = null;
        try {
            title_font = Font.createFont(Font.TRUETYPE_FONT, new File("name.ttf")).deriveFont(30.f);
        } catch (FontFormatException | IOException e) {
            System.err.println("There was an error retrieving the font file!");
        }
        g.setFont(title_font);
        for (int i = 1; i < 512 && current_state == State.NAME; i++) {
            int alpha = Math.abs(i - 256);
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, panel.getWidth(), panel.getHeight());
            g.setColor(new Color(255, 0, 0, alpha));
            g.drawString("Azmata", 300, 300);
            panel.revalidate();
            sleep(8);
        }
    }

    /**
     * Draws the DNP Logo on the screen;
     */
    private static void drawLogo() {
        current_state = State.LOGO;
        for (int i = 1; i < 512 && current_state == State.LOGO; i++) {
            if (current_state == State.LOGO) {
                int alpha = Math.abs(i - 256);
                g.setColor(Color.BLACK);
                g.fillRect(0, 0, panel.getWidth(), panel.getHeight());
                g.setColor(Color.CYAN);
                g.drawString("DNP", 300, 300);
                panel.revalidate();
                sleep(10);
            }
        }
    }
}