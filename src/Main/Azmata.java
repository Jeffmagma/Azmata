package Main;

import Game.SpriteSheet;
import Menu.GameMenu;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * The main driver class of the program
 */
public class Azmata {
    /** A flag to show if we are debugging or not */
    public static final boolean DEBUGGING = true; // To be set to false on release

    /** The size, in pixels, of a square in the grid of the game */
    public static final int BLOCK_SIZE = 32;
    /** The scale we want for the width of the screen (16 because we want 16:9) */
    public static final int SCALE_X = 16;
    /** The scale we want for the height of the screen (9 because we want 16:9) */
    public static final int SCALE_Y = 9;
    /** An arbritrary number that would make the window fit on most screens */
    public static final int SCALE = 2;
    /** The "Azmata" image used in the starting animation */
    private static Image name_image;
    /** The "DNP" images used in the starting animation */
    private static Image dnp_image;
    /** The JFrame that contains everything */
    private static JFrame frame;
    /** The primary JPanel */
    private static JPanel panel;
    /** The graphics that are drawn to */
    private static Graphics2D graphics;
    /** The current state that the animation is in */
    private static State current_state;
    /** The alpha of the images in the intro */
    private static int alpha = 0;
    /** If the animation is fading the fading away phase */
    private static boolean animation_fading = false;

    /**
     * Retreive an image from a relative file path
     *
     * @param path The path to get the image from
     * @return an Image that was read from the file, null if there was an error
     */
    public static BufferedImage imageFromFile(String path) {
        try {
            return ImageIO.read(Azmata.class.getClassLoader().getResource(path));
        } catch (IOException | NullPointerException e) {
            System.err.println("There was an error retrieving " + path);
            if (DEBUGGING) e.printStackTrace();
        }
        return null;
    }

    /**
     * Initalize the JFrame and JPanel to be able to use in the rest of the program
     */
    private static void initialize() {
        // Set the current state to drawing the logo
        current_state = State.LOGO;
        // Gets the image used for the game name in the intro
        name_image = imageFromFile("Main/azmata.png");
        // Gets the image used for the company name in the intro
        dnp_image = imageFromFile("Main/dnpnew.png");
        // Construct the JFrame
        frame = new JFrame();
        // Construct a new JPanel
        panel = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                graphics = (Graphics2D) g;
                if (animation_fading) alpha--;
                else alpha++;

                if (alpha == 255) animation_fading = true;
                if (alpha == 0) nextState();

                switch (current_state) {
                    case LOGO:
                        fadeImage(dnp_image, 5);
                        break;
                    case NAME:
                        fadeImage(name_image, 8);
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
                nextState();
            }
        });
        // Set the size of the JFrame to be a 16:9 screen, and make sure it can hold 32x32 grid blocks evenly
        frame.setPreferredSize(new Dimension(BLOCK_SIZE * SCALE_X * SCALE, BLOCK_SIZE * SCALE_Y * SCALE));
        if (DEBUGGING)
            System.out.println("Window size: " + BLOCK_SIZE * SCALE_X * SCALE + 'x' + BLOCK_SIZE * SCALE_Y * SCALE);
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
    }

    /**
     * The entry point of the program, this is executed when the program is run
     *
     * @param args The command line arguments (that aren't used)
     */
    public static void main(String[] args) {
        initialize();
        while (current_state != State.MAIN_MENU) panel.repaint();
        frame.remove(panel);
        if (DEBUGGING) {
            // Show the sprites of the test image
            SpriteSheet s = new SpriteSheet(imageFromFile("Game/test.png"));
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 3; j++) {
                    JOptionPane.showMessageDialog(frame, new JLabel(new ImageIcon(s.sprites[i][j])));
                }
            }
        }
        GameMenu menu = new GameMenu();
        frame.add(menu);
        menu.revalidate();
        menu.repaint();
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
     * Draws an image fading in and out
     *
     * @param i    The image to draw
     * @param time The amount of time in milliseconds to sleep between drawings
     */
    private static void fadeImage(Image i, long time) {
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, panel.getWidth(), panel.getHeight());
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha / 255.f));
        graphics.drawImage(i, (panel.getWidth() - i.getWidth(null)) / 2, (panel.getHeight() - i.getHeight(null)) / 2, null);
        sleep(time);
    }

    /**
     * Shifts the animation to the next state, used when spacebar is pressed or one stage of the animation is over
     */
    private static void nextState() {
        current_state = current_state.next();
        alpha = 0;
        animation_fading = false;
    }

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
}