package Battle;

import Main.Azmata;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;

/**
 * Battle mode in the game.
 * <h2>Course Info</h2>
 * <i>ICS4U0 with Mrs. Krasteva</i>
 *
 * @author Richard Yi
 * @version 1.0
 * @since 2017-05-10
 */
public class Battle extends JPanel {
    /** The top boundary of the main game area. (the places where the tiles spawn) */
    public static final int MAIN_TOP = 50;
    /** The bottom boundary of the main game area. (the places where the tiles spawn) */
    public static final int MAIN_BOTTOM = 516;
    /** The left boundary of the main game area. (the places where the tiles spawn) */
    public static final int MAIN_LEFT = 0;
    /** The right boundary of the main game area. (the places where the tiles spawn) */
    public static final int MAIN_RIGHT = 784;
    /** All the letters in the alphabet, including space. */
    private static final String LETTERS = "QWERTYUIOPASDFGHJKLZXCVBNM ";
    /** How many characters the user has answered */
    int answered = 0;
    /** Image for double-buffering. */
    BufferedImage buffer = new BufferedImage(Azmata.SCREEN_WIDTH, Azmata.SCREEN_HEIGHT, BufferedImage.TYPE_INT_ARGB);
    /** Graphics object to draw to for double-buffering. */
    Graphics2D g = buffer.createGraphics();
    /** Temporary health variable to keep track of how much health the player has. */
    double health = 100.0;
    /** Contains tiles on the screen that contain a letter. */
    private Set<Tile> tiles = new HashSet<>();
    /** The question for the current battle. */
    private String question;
    /** The answer for the current battle. */
    private String answer;
    /** Whether the Battle is still running. */
    private volatile boolean running;
    /** The difficulty of the battle. */
    private int difficulty;
    /** How many game ticks have elapsed in the battle. */
    private long tickCount;
    /**
     * A timer that repeatedly calls the game tick process.
     *
     * @see Battle#tick
     */
    private Timer timer;
    /** The font to draw the file with */
    private Font tileFont;
    //TODO: Integrate with player to create health system
    /** Whether the question for the battle is overlayed */
    private boolean showQuestion;
    /** The font used to draw the question. Decided at runtime. */
    private Font questionFont;
    /** The X-coordinate such that the question will be drawn centered. */
    private int questionX;

    //TODO: Javadoc
    /** The Y-coordinate such that the question will be drawn centered. */
    private int questionY;
    /** Width of the question such that the question will be drawn centered. */
    private int questionWidth;
    /** Height of the question such that the question will be drawn centered. */
    private int questionHeight;
    /** The font used to draw the letters in the bottom bar. */
    private Font letterFont;
    /** The tick at which the user won the game. */
    private long stopTick;
    /**
     * Main game tick process.
     * Does processing and renders the frame.
     *
     * @see Battle#paintComponent(Graphics)
     */
    private ActionListener tick = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            ++tickCount;

            if (tickCount % Math.max(10, (100 - difficulty * 5)) == 0) {
                spawn(Math.random() < 0.5);
            }

            for (Tile tile : tiles) {
                tile.tick();

                if (tile.isBlowing()) { //TODO: Make it blow better
                    tile.moveX(-2);
                    tile.moveY((int) ((tickCount + tile.hashCode()) % 11 - 5));
                } else {
                    tile.moveX((int) (tickCount / 50 + tile.hashCode()) % 5 - 2);
                    tile.moveY((int) (tickCount / 50 + tile.hashCode() - 1) % 5 - 2);
                }
            }

            tiles.removeIf((Tile tile) -> (tile.getAge() >= 5000 - difficulty * 250));

            revalidate();
            repaint();

            if(tickCount % 100 == 0)
                System.out.println(tickCount);
        }
    };

    /**
     * Main and only constructor.
     * Creates a new Battle based on the difficulty, question, and answer is.
     * Attaches mouse events.
     *
     * @param difficulty The difficulty of the battle.
     * @param question   The question for the battle.
     * @param answer     The answer for the battle.
     */
    public Battle(int difficulty, String question, String answer) {
        this.question = question;
        this.answer = answer.toUpperCase();
        this.difficulty = difficulty;
        tickCount = 0;
        tileFont = new Font("Verdana", Font.PLAIN, (int) ((150 - 5 * difficulty) / 1.5));

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent mouse) {
                System.out.println("clicked");
                click(mouse.getX(), mouse.getY());
            }
        });

        //Determine the font for drawing the question
        questionX = -1;
        questionY = -1;
        for (int questionFontSize = 50; questionX < 50; questionFontSize--) {
            questionFont = new Font("Verdana", Font.BOLD, questionFontSize);
            FontMetrics metrics = getFontMetrics(questionFont);
            questionX = Azmata.SCREEN_WIDTH / 2 - metrics.stringWidth(question) / 2;
            questionY = Azmata.SCREEN_HEIGHT / 2 + (metrics.getAscent() - metrics.getDescent()) / 2;
            questionWidth = metrics.stringWidth(question);
            questionHeight = metrics.getAscent() - metrics.getDescent();
        }

        //Determine the font for drawing the bottom bar letters
        letterFont = new Font("Courier New", Font.PLAIN, 40);
        for (int letterFontSize = 40; letterFontSize * answer.length() >= MAIN_RIGHT - 50; letterFontSize--) {
            letterFont = new Font("Courier New", Font.PLAIN, letterFontSize);
            System.out.println(" " + answer.length() + ", " + letterFontSize);
        }
    }

    /**
     * Main method. For testing only
     *
     * @param args Arguments passed in the command-line
     */
    public static void main(String[] args) throws InterruptedException {
        JFrame f = new JFrame();
        f.setSize(1024, 576);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Battle battle = new Battle(15, "According to all known laws of aviation, there is no way a bee should be able to fly. Does the bee fly anyway?", "memes");
        f.add(battle);
        f.setVisible(true);
        battle.start();

        while (battle.isRunning()) ;

        f.dispose();
        System.out.println("Ended.");
    }

    /**
     * Starts the battle.
     * Initializes Timer and runs it.
     */
    public void start() {
        revalidate();
        repaint();
        timer = new Timer(20, tick);
        timer.start();
        running = true;
    }

    /**
     * Renders the screen.
     *
     * @param graphics The graphics object for the JComponent
     * @see JComponent#paintComponent
     */
    @Override
    protected void paintComponent(Graphics graphics) {
        Graphics2D canvas = (Graphics2D) graphics;
        int x, y, size;
        String letter;

        //Erase the screen
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, Azmata.SCREEN_WIDTH, Azmata.SCREEN_HEIGHT);

        //Draw all Tiles
        for (Tile tile : tiles) {
            x = tile.getX();
            y = tile.getY();
            size = tile.getSize();
            letter = "" + tile.getLetter();

            g.setColor(tile.getColor());
            g.fillOval(x - size / 2, y - size / 2, size, size);

            g.setColor(Color.BLACK);
            g.setFont(tileFont);

            FontMetrics metrics = g.getFontMetrics(tileFont);
            int cx = x - metrics.stringWidth(letter) / 2;
            int cy = y + (metrics.getAscent() - metrics.getDescent()) / 2;
            g.drawString(letter, cx, cy);
        }

        g.setColor(Color.BLACK);
        g.setFont(letterFont);

        //Show which characters have been answered
        for (int i = 0; i < answered; i++) {
            g.drawString(String.valueOf(answer.charAt(i)), i * letterFont.getSize() + 20, Azmata.SCREEN_HEIGHT - 50);
        }
        for (int i = answered; i < answer.length(); i++) {
            g.drawString("_", i * letterFont.getSize() + 20, Azmata.SCREEN_HEIGHT - 50);
        }

        //Render the player's health bar
        g.setColor(Color.GREEN);
        int segment = (int) (health * (MAIN_RIGHT - 10) / 100);
        g.fillRect(5, 5, segment, 40);
        g.setColor(Color.RED);
        g.fillRect(segment + 5, 5, MAIN_RIGHT - segment - 10, 40);

        //Render background
        g.setColor(Color.CYAN);
        g.fillRect(MAIN_RIGHT, 0, 250, 250);
        g.setColor(new Color(0, 169, 0));
        g.fillRect(MAIN_RIGHT, 250, 250, MAIN_BOTTOM - 250);

        //Render the enemy and enemy health bar
        segment = 230 - (int) (230.0 * answered / answer.length());
        g.setColor(Color.GREEN);
        g.fillRect(MAIN_RIGHT + 5, 150, segment, 30);
        g.setColor(Color.RED);
        g.fillRect(MAIN_RIGHT + segment + 5, 150, 240 - segment - 10, 30);
        //TODO: Add enemy rendering

        g.setColor(Color.BLACK);
        //Draw the questions if the user is holding down CTRL
        if (showQuestion) {
            g.setFont(questionFont);
            g.setColor(Color.WHITE);
            g.fillRect(questionX - 10, questionY - questionHeight - 10, questionWidth + 20, questionHeight + 20);
            g.setColor(Color.YELLOW);
            g.drawRect(questionX - 10, questionY - questionHeight - 10, questionWidth + 20, questionHeight + 20);
            g.setColor(Color.BLACK);
            g.drawString(question, questionX, questionY);
        }

        if (stopTick > 0) {
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, Azmata.SCREEN_WIDTH, Azmata.SCREEN_HEIGHT);
            g.setFont((new Font("Verdana", Font.PLAIN, 100)));
            g.setColor(Color.BLACK);
            g.drawString("YOU WON!", 242, 328);
            if (tickCount - stopTick >= 50) {
                timer.stop();
                running = false;
            }
        }

        canvas.drawImage(buffer, 0, 0, null);
        //Draw buffer to screen
    }

    /**
     * Spawns a tile onto the screen.
     *
     * @param real Whether the tile to spawn is of the correct answer.
     */
    private void spawn(boolean real) {
        int spawnSize = 150 - (5 * difficulty) + (int) (Math.random() * 5);
        if (real && answered < answer.length())
            tiles.add(new Tile(answer.charAt(answered),
                    (int) (Math.random() * MAIN_RIGHT),
                    (int) (Math.random() * (MAIN_BOTTOM - MAIN_TOP) + MAIN_TOP),
                    spawnSize));
        else
            tiles.add(new Tile(LETTERS.charAt((int) (Math.random() * LETTERS.length())),
                    (int) (Math.random() * Azmata.SCREEN_WIDTH),
                    (int) (Math.random() * Azmata.SCREEN_HEIGHT - 50),
                    spawnSize));
    }

    /**
     * Triggered when the mouse is clicked.
     * Checks if the user clicks on a tile and if it's correct.
     *
     * @param x The x-coordinate of the mouse click
     * @param y The y-coordinate of the mouse click
     */
    private void click(int x, int y) {
        if (answered == answer.length()) return;
        System.out.println("" + x + " " + y);
        char nextChar = answer.charAt(answered);
        int x2, y2;
        boolean clickedTile = false, clickedCorrect = false;

        for (Tile tile : tiles) {
            if (Math.hypot(tile.getX() - x, tile.getY() - y) <= tile.getSize() / 2) {
                clickedTile = true;
                if (tile.getLetter() == nextChar)
                    clickedCorrect = true;
            }
        }

        tiles.removeIf((Tile tile) -> (Math.hypot(tile.getX() - x, tile.getY() - y) <= tile.getSize() / 2));

        if (clickedCorrect) { //Case 1: Clicked a correct tile
            ++answered;

            if (answered == answer.length()) //User has won battle
                stopTick = tickCount;
        } else if (clickedTile) { //Case 2: No correct tiles were clicked but a tile was clicked
            health -= 1.5 + (difficulty / 50.0); //TODO: Fix when integrating
            //Game.state.health -= 5.0; //TODO: Armor?
        } else { //Case 3: No tiles were clicked at all
            health -= 0.75 + (difficulty / 100.0);
            //Game.state.health -= 2.5;
        }
    }

    /**
     * Returns whether the Battle is still going on
     *
     * @return Whether the Battle is still going on
     */
    public boolean isRunning() {
        return running;
    }
}