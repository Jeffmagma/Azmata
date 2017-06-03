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
import java.io.File;
import java.io.IOException;
import java.net.URL;

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
    /** The amount of blocks in the width */
    private static final int X_BLOCKS = SCALE_X * SCALE;
    /** The width of the screen */
    public static final int SCREEN_WIDTH = BLOCK_SIZE * X_BLOCKS;
    /** The amount of blocks in the height */
    private static final int Y_BLOCKS = SCALE_Y * SCALE;
    /** The height of the screen */
    public static final int SCREEN_HEIGHT = BLOCK_SIZE * Y_BLOCKS;
    /** The graphics that are drawn to */
    public static Graphics2D graphics;
    public static JPanel current_panel;
    private static File save_directory = null;
    /** The JFrame that contains everything */
    public static JFrame frame;

    public static File saveDirectory() {
        if (save_directory != null) return save_directory;
        String OS = System.getProperty("os.name").toUpperCase();
        String dir;
        if (OS.startsWith("WIN")) {
            dir = System.getenv("APPDATA");
        } else {
            dir = System.getProperty("user.home");
        }
        return save_directory = new File(dir + "/Azmata/save.xd");
    }

    /**
     * Retreive an image from a relative file path
     *
     * @param path The path to get the image from
     * @return an Image that was read from the file, null if there was an error
     */
    public static BufferedImage imageFromFile(String path) {
        try {
            URL resource = Azmata.class.getClassLoader().getResource(path);
            if (resource == null) throw new NullPointerException();
            return ImageIO.read(resource);
        } catch (IOException | NullPointerException e) {
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
        frame.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        if (DEBUGGING) System.out.println("Window size: " + SCREEN_WIDTH + 'x' + SCREEN_HEIGHT);
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
        if (DEBUGGING) System.out.println(saveDirectory());
        //SwingUtilities.invokeLater(Azmata::initializeJFrame);
        initializeJFrame();
        SplashScreen splash_screen = new SplashScreen();
        frame.add(splash_screen);
        splash_screen.play();
        current_panel = splash_screen;
        while (frame.isDisplayable()) {
            frame.remove(current_panel);
            MainMenu game_menu = new MainMenu();
            frame.add(game_menu);
            MainMenu.Option selected = game_menu.getSelected();
            if (DEBUGGING) System.out.println(selected.name());
            frame.remove(game_menu);
            switch (selected) {
                case NEW_GAME:
                    Game new_game = new Game(Game.World.EARTHLOO);
                    frame.add(new_game);
                    current_panel = new_game;
                    new_game.run();
                    break;
                case CONTINUE_GAME:
                    Game game;
                    try {
                        GameState saved_state = (GameState) game_menu.saved_game.readObject();
                        game = new Game(saved_state);
                    } catch (IOException | ClassNotFoundException e) {
                        System.err.println("There was an error retrieving the saved game!"); // This should never happen...
                        if (DEBUGGING) e.printStackTrace();
                        game = new Game(Game.World.EARTHLOO);
                    }
                    frame.add(game);
                    current_panel = game;
                    game.run();
                    break;
                case OPTIONS:
                    OptionsMenu options_menu = new OptionsMenu();
                    frame.add(options_menu);
                    current_panel = options_menu;
                    options_menu.show();
                    break;
                case INSTRUCTIONS:
                    Instructions instructions = new Instructions();
                    frame.add(instructions);
                    current_panel = instructions;
                    instructions.show();
                    break;
            }
        }
    }
}