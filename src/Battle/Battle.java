package Battle;

import Main.Azmata;
import Game.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
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
    private static final char[] LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ ".toCharArray();
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
    /** The font to draw the file with */
    private Font tileFont;
    /** Whether the question for the battle is overlayed */
    private boolean showQuestion;
    /** Whether the user has pressed space after the battle */
    private boolean spacePressed;
    /** The font used to draw the question. Decided at runtime. */
    private Font questionFont;
    /** The X-coordinate such that the question will be drawn centered. */
    private int questionX;
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
    /** Whether the user has lost */
    private boolean lost = false;
    /** Information for the user to learn */
    private String[] learn;
    /**
     * A timer that repeatedly calls the game tick process.
     * @see Battle#tick
     */
    private Timer timer;

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

            if (tickCount > 100 && tickCount % Math.max(10, (100 - difficulty * 5)) == 0)
                spawn(Math.random() < 0.6);

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
            paintImmediately(0, 0, Azmata.SCREEN_WIDTH, Azmata.SCREEN_HEIGHT);
            if (getGraphics() != null) paintComponent(getGraphics());
        }
    };

    /**
     * Main and only constructor.
     * Creates a new Battle based on the difficulty, question, and answer is.
     * Attaches mouse events.
     */
    public Battle() {
        if(Game.state.map_name.equals("Earthloo.map")){
            question = Questions.questions[0][Game.state.question];
            answer = Questions.answers[0][Game.state.question];
            learn = Questions.material[0][Game.state.question].split("\n");
            difficulty = 5;
        }
        //TODO: Add all map cases
        tickCount = 0;
        tileFont = new Font("Verdana", Font.PLAIN, (int) ((150 - 5 * difficulty) / 1.5));

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent mouse) {
                click(mouse.getX(), mouse.getY());
            }
        });

        InputMap input_map = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        input_map.put(KeyStroke.getKeyStroke("control CONTROL"), "showq");
        input_map.put(KeyStroke.getKeyStroke("released CONTROL"), "noq");
        input_map.put(KeyStroke.getKeyStroke("SPACE"), "space");
        getActionMap().put("showq", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showQuestion = true;
            }
        });
        getActionMap().put("noq", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showQuestion = false;
            }
        });
        getActionMap().put("space", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Azmata.debug("space");
                if(stopTick > 0)
                    spacePressed = true;
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
        }
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
        while (running);
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
        int segment = (int) (Game.state.health * (MAIN_RIGHT - 10) / 100);
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
        if ((!lost && tickCount < 100) || (lost && tickCount > 100 && tickCount < 200) || showQuestion) {
            g.setFont(questionFont);
            g.setColor(Color.WHITE);
            g.fillRect(questionX - 10, questionY - questionHeight - 10, questionWidth + 20, questionHeight + 20);
            g.setColor(Color.YELLOW);
            g.drawRect(questionX - 10, questionY - questionHeight - 10, questionWidth + 20, questionHeight + 20);
            g.setColor(Color.BLACK);
            g.drawString(question, questionX, questionY);
        }

        if(lost && tickCount < 100){
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, Azmata.SCREEN_WIDTH, Azmata.SCREEN_HEIGHT);
            g.setFont((new Font("Verdana", Font.PLAIN, 100)));
            g.setColor(Color.BLACK);
            g.drawString("GAME OVER", 230, 328);
        }

        if (stopTick > 0) {
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, Azmata.SCREEN_WIDTH, Azmata.SCREEN_HEIGHT);
            g.setFont((new Font("Verdana", Font.PLAIN, 50)));
            g.setColor(Color.BLACK);
            for(int i = 0; i < learn.length; i++)
                g.drawString(learn[i], 242, i * 50); //some centered x
            if (spacePressed) {
                timer.stop();
                running = false;
                ++Game.state.question;
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
            tiles.add(new Tile(LETTERS[(int) (Math.random() * LETTERS.length)],
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
        } else if (clickedTile) { //Case 2: No correct tiles were clicked but a tile was clicked
            Game.state.health -= 5.0 + (difficulty / 20.0); //TODO: Armor?
        } else { //Case 3: No tiles were clicked at all
            Game.state.health -= 3.0 + (difficulty / 50.0);
        }

        if (answered == answer.length()) //User has won battle
            stopTick = tickCount;

        if(Game.state.health <= 0.0){
            tiles.removeIf((Tile tile) -> (true));
            Game.state.health = 100.0;
            answered = 0;
            tickCount = 0;
            lost = true;
        }
    }
}