package Main;

import Game.Game;
import Game.GameState;
import Menu.Instructions;
import Menu.MainMenu;
import Menu.OptionsMenu;
import Menu.SplashScreen;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * The driver class of the program
 */
public class Azmata {
    /** A flag to show if we are debugging or not */
    public static final boolean DEBUGGING = true; // To be set to false on release


    /** The size, in pixels, of a square in the grid of the game */
    public static final int BLOCK_SIZE = 32;
    /** The scale we want for the width of the screen (16 because we want 16:9) */
    private static final int SCALE_X = 16;
    /** The scale we want for the height of the screen (9 because we want 16:9) */
    private static final int SCALE_Y = 9;
    /** An arbitrary number that would make the window fit on most screens */
    private static final int SCALE = 2;
    /** The graphics that are drawn to */
    public static Graphics2D graphics;
    /** The JFrame that contains everything */
    private static JFrame frame;

    /**
     * Retreive an image from a relative file path
     *
     * @param path The path to get the image from
     * @return an Image that was read from the file, null if there was an error
     */
    public static BufferedImage imageFromFile(String path) {
        try {
            return ImageIO.read(Azmata.class.getClassLoader().getResource(path));
        } catch (IOException e) {
            System.err.println("There was an error retrieving " + path);
            if (DEBUGGING) e.printStackTrace();
        }
        return null;
    }

    /**
     * Initialize the JFrame to be able to use in the rest of the program
     */
    private static void initializeJFrame() {
        // Construct the JFrame
        frame = new JFrame();
        // Set the size of the JFrame to be a 16:9 screen, and make sure it can hold 32x32 grid blocks evenly
        frame.setPreferredSize(new Dimension(BLOCK_SIZE * SCALE_X * SCALE, BLOCK_SIZE * SCALE_Y * SCALE));
        if (DEBUGGING)
            System.out.println("Window size: " + BLOCK_SIZE * SCALE_X * SCALE + 'x' + BLOCK_SIZE * SCALE_Y * SCALE);
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
        SwingUtilities.invokeLater(Azmata::initializeJFrame);
        SplashScreen splash_screen = new SplashScreen();
        frame.add(splash_screen);
        splash_screen.play();
        while (true) {
            frame.remove(splash_screen);
            MainMenu game_menu = new MainMenu();
            frame.add(game_menu);
            MainMenu.MenuOption selected = game_menu.getSelected();
            if (DEBUGGING) System.out.println(selected.name());
            frame.remove(game_menu);
            switch (selected) {
                case NEW_GAME:
                    Game game = new Game(new Point(6, 9));
                    frame.add(game);
                    break;
                case CONTINUE_GAME:
                    Game g;
                    try {
                        GameState saved_state = (GameState) game_menu.saved_game.readObject();
                        g = new Game(saved_state);
                    } catch (IOException | ClassNotFoundException e) {
                        System.err.println("There was an error retrieving the saved game");
                        if (DEBUGGING) e.printStackTrace();
                        g = new Game(new Point(6, 9));
                    }
                    frame.add(g);
                    break;
                case OPTIONS:
                    OptionsMenu options_menu = new OptionsMenu();
                    frame.add(options_menu);
                    break;
                case INSTRUCTIONS:
                    Instructions instructions = new Instructions();
                    frame.add(instructions);
                    instructions.show();
                    break;
            }
        }
    }
}