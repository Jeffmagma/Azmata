package Main;

import Game.Game;
import Game.GameState;
import Game.HighScore;
import Game.Questions;
import Menu.*;
import Menu.SplashScreen;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * The driver class of the program
 *
 * @author Jeffrey Gao
 */
public class Azmata {
    /** The size, in pixels, of a square in the grid of the game */
    public static final int BLOCK_SIZE = 32;
    /** A flag to show if we are debugging or not */
    public static final boolean DEBUGGING = false; // TODO: Set on false on release
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
    /** The current panel that is on the frame and should be removed when the main menu is next shown */
    public static JPanel current_panel;
    /** The JFrame that contains everything */
    public static JFrame frame;
    /** Name of the user. Temporary storage before it is transferred to GameState */
    public static String name;
    /** User highscores */
    public static List<HighScore> high_scores;
    /** Where the save file should be stored */
    private static File save_directory;

    /**
     * Gets the file where the saved file should be.
     * This is a different location depending on the OS the
     * program is run in
     *
     * @return A File representing the saved file
     */
    public static File saveDirectory() {
        if (save_directory != null) return save_directory;
        String OS = System.getProperty("os.name").toLowerCase();
        String dir;
        if (OS.startsWith("win")) {
            dir = System.getenv("APPDATA");
        } else {
            dir = System.getProperty("user.home");
        }
        return save_directory = new File(dir + "/Azmata/save.xd");
    }

    /**
     * Gets the file where the high scores file should be.
     * This is a different location depending on the OS the
     * program is run in
     *
     * @return A File representing the saved file
     */
    public static File highScoreDirectory() {
        String OS = System.getProperty("os.name").toLowerCase();
        String dir;
        if (OS.startsWith("win")) {
            dir = System.getenv("APPDATA");
        } else {
            dir = System.getProperty("user.home");
        }
        return new File(dir + "/Azmata/high.xd");
    }

    /**
     * Output something (usually a string) if our debugging flag is set
     *
     * @param o The object to output
     */
    public static void debug(Object o) {
        if (DEBUGGING) System.out.println(o);
    }

    /**
     * Retrieve an image from a relative file path
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
        frame.getContentPane().setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        debug("Window size: " + SCREEN_WIDTH + 'x' + SCREEN_HEIGHT);
        // Stop users from resizing the JFrame (we need to preserve the scale)
        frame.setResizable(false);
        // Make sure the frame is the right size
        frame.pack();
        // Center the frame
        frame.setLocationRelativeTo(null);
        // Set the JFrame to exit on close
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        // Show the frame
        frame.setVisible(true);
        // Try to get to the top
        frame.requestFocus();
    }

    /**
     * Read high scores from file
     */
    private static void readHighScores() {
        try {
            high_scores.clear();
            BufferedReader in = new BufferedReader(new FileReader(highScoreDirectory()));
            String name;
            int score, size = Integer.parseInt(in.readLine());
            for (int i = 0; i < size; i++) {
                name = in.readLine();
                score = Integer.parseInt(in.readLine());
                high_scores.add(new HighScore(name, score));
                Azmata.debug("Read: " + high_scores.get(i));
            }
            in.close();
        } catch (IOException | NullPointerException | NumberFormatException e) {
            highScoreDirectory().delete();
            if (Azmata.DEBUGGING) e.printStackTrace();
        }
    }

    /**
     * The entry point of the program, this is executed when the program is run
     *
     * @param args The command line arguments (that aren't used)
     */
    public static void main(String[] args) {
        high_scores = new ArrayList<>();
        debug(saveDirectory());
        debug(highScoreDirectory());
        readHighScores();
        Questions.init();
        SwingUtilities.invokeLater(Azmata::initializeJFrame);
        SplashScreen splash_screen = new SplashScreen();
        frame.add(splash_screen);
        splash_screen.play();
        current_panel = splash_screen;
        // while(true), while(the window is open)
        while (frame.isDisplayable()) {
            frame.remove(current_panel);
            MainMenu game_menu = new MainMenu();
            frame.add(game_menu);
            MainMenu.Option selected = game_menu.getSelected();
            debug(selected.name());
            frame.remove(game_menu);
            switch (selected) {
                case NEW_GAME:
                    name = JOptionPane.showInputDialog(frame, "Please enter your name");
                    high_scores.add(new HighScore(name, 0));
                    WorldMenu world_menu = new WorldMenu();
                    frame.add(world_menu);
                    Game new_game = new Game(world_menu.selected());
                    frame.remove(world_menu);
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
                case HIGH_SCORES:
                    HighScores high_scores = new HighScores();
                    frame.add(high_scores);
                    current_panel = high_scores;
                    high_scores.show();
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