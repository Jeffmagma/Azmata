import Menu.GameMenu;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

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
    /** An arbritrary number that would make the window fit on most screens */
    private static final int SCALE = 2;
    static BufferedImage az;
    /** The JFrame that contains everything */
    private static JFrame frame;
    /** The primary JPanel */
    private static JPanel panel;
    /** The graphics that are drawn to */
    private static Graphics2D graphics;
    /** The current state that the animation is in */
    private static State current_state;
    private static int tick = 0;

    /**
     * Initalize the JFrame and JPanel to be able to use in the rest of the program
     */
    private static void initialize() {
        current_state = State.NAME;
        // Construct the JFrame
        frame = new JFrame();
        // Construct a new JPanel
        panel = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                graphics = (Graphics2D) g;
                switch (current_state) {
                    case LOGO:
                        drawLogo(tick++ % 256);
                        break;
                    case NAME:
                        drawName(tick++ % 256);
                        break;
                    case MAIN_MENU:
                        break;
                }
            }
        };
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
        try {
            URL res = Azmata.class.getResource("azmata.png");
            System.out.println(res == null);
            az = ImageIO.read(Azmata.class.getResource("azmata.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        initialize();
        while (current_state != State.MAIN_MENU) panel.repaint();

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
    private static void drawName(int alpha) {
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, panel.getWidth(), panel.getHeight());
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha / 255.f));
        graphics.drawImage(az, 0, 0, null);
        sleep(8);
    }

    /**
     * Draws the DNP Logo on the screen;
     */
    private static void drawLogo(int alpha) {
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, panel.getWidth(), panel.getHeight());
        graphics.setColor(Color.CYAN);
        graphics.drawString("DNP", 300, 300);
        sleep(10);
    }
}