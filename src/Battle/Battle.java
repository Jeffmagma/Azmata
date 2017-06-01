package Battle;

import Main.Azmata;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.*;

/**
    Battle mode in the game.
    <h2>Course Info</h2>
    <i>ICS4U0 with Mrs. Krasteva</i>
    @author Richard Yi
    @version 1.0
    @since 2017-05-10
*/
public class Battle extends JPanel{
    /**Contains tiles on the screen that contain a letter.*/
    private Set<Tile> tiles = new HashSet<>();
    /**The question for the current battle.*/
    private String question;
    /**The answer for the current battle.*/
    private String answer;
    /**How many characters the user has answered*/
    int answered = 0;
    /**Whether the Battle is still running.*/
    private volatile boolean running;
    /**The difficulty of the battle.*/
    private int difficulty;
    /**How many game ticks have elapsed in the battle.*/
    private long tickCount;
    /**A timer that repeatedly calls the game tick process.
     * @see Battle#tick
    */
    private Timer timer;
    /**The font to draw the file with*/
    private Font tileFont;
    /**Whether the question for the battle is overlayed*/
    private boolean showQuestion;
    /**The font used to draw the question. Decided at runtime.*/
    private Font questionFont;
    /** The X-coordinate such that the question will be drawn centered. */
    private int questionX;
    /** The Y-coordinate such that the question will be drawn centered. */
    private int questionY;
    /** Width of the question such that the question will be drawn centered. */
    private int questionWidth;
    /** Height of the question such that the question will be drawn centered. */
    private int questionHeight;
    /** Image for double-buffering. */
    BufferedImage buffer = new BufferedImage(Azmata.SCREEN_WIDTH, Azmata.SCREEN_HEIGHT, BufferedImage.TYPE_INT_ARGB);
    /** Graphics object to draw to for double-buffering. */
    Graphics2D g = buffer.createGraphics();
    //TODO: Integrate with player to create health system
    double health;
    /**The font used to draw the letters in the bottom bar*/
    private Font letterFont;// = new Font("Courier New", Font.PLAIN, 20);

    //TODO: Javadoc
    public static final int MAIN_TOP = 50;
    public static final int MAIN_BOTTOM = 516;
    public static final int MAIN_LEFT = 0;
    public static final int MAIN_RIGHT = 784;
    /**All the letters in the alphabet, including space.*/
    private static final String LETTERS = "QWERTYUIOPASDFGHJKLZXCVBNM ";

    /**Main game tick process.
     * Does processing and renders the frame.
     * @see Battle#paintComponent(Graphics)
     * */
    private ActionListener tick = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            ++tickCount;

            if (tickCount % Math.max(10, (100 - difficulty * 10)) == 0) {
                if (Math.random() < 1)
                    spawn(true);
                else
                    spawn(false);
            }

            for (Tile tile : tiles) {
                tile.tick();

                if(tile.isBlowing()) { //TODO: Make it blow better
                    tile.moveX(-2);
                    tile.moveY((int) ((tickCount + tile.hashCode()) % 11 - 5));
                }
                else {
                    tile.moveX((int) (tickCount / 50 + tile.hashCode()) % 5 - 2);
                    tile.moveY((int) (tickCount / 50 + tile.hashCode() - 1) % 5 - 2);
                }
            }

            tiles.removeIf((Tile tile) -> (tile.getAge() >= 5000 - difficulty * 250));

            repaint();
            revalidate();
        }
    };

    /**Main and only constructor.
     * Creates a new Battle based on the difficulty, question, and answer is.
     * Attaches mouse events.
     * @param difficulty The difficulty of the battle.
     * @param question The question for the battle.
     * @param answer The answer for the battle.
     */
    public Battle(int difficulty, String question, String answer) {
        this.question = question;
        this.answer = answer.toUpperCase();
        this.difficulty = difficulty;
        tickCount = 0;
        tileFont = new Font("Verdana", Font.PLAIN, (int) ((150 - 5 * difficulty) / 1.5));


        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent mouse) {
                click(mouse.getX(), mouse.getY());
            }
        });

        //Determine the font for drawing the question
        questionX = -1;
        questionY = -1;
        for(int questionFontSize = 50; questionX < 50; questionFontSize--){
            questionFont = new Font("Verdana", Font.PLAIN, questionFontSize);
            FontMetrics metrics = getFontMetrics(questionFont);
            questionX = Azmata.SCREEN_WIDTH / 2 - metrics.stringWidth(question) / 2;
            questionY = Azmata.SCREEN_HEIGHT / 2 + (metrics.getAscent() - metrics.getDescent()) / 2;
            questionWidth = metrics.stringWidth(question);
            questionHeight = metrics.getAscent() - metrics.getDescent();
        }

        //Determine the font for drawing the bottom bar letters
        letterFont = new Font("Courier New", Font.PLAIN, 40);
        for(int letterFontSize = 40; letterFontSize * answer.length() >= MAIN_RIGHT - 50; letterFontSize--){
            letterFont = new Font("Courier New", Font.PLAIN, letterFontSize);
            System.out.println(" " + answer.length() + ", " + letterFontSize);
        }

        //Listen for keys
        addKeyListener(new KeyListener(){
            @Override
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_CONTROL)
                    showQuestion = true;
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_CONTROL)
                    showQuestion = false;
            }

            @Override
            public void keyTyped(KeyEvent e){ }
        });

        setFocusable(true);
        requestFocusInWindow();
    }

    /**Main method. For testing only.*/
    public static void main(String[] args) throws InterruptedException{
        JFrame f = new JFrame();
        f.setSize(1024, 576);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Battle battle = new Battle(5, "According to all known laws of aviation, there is no way a bee should be able to fly. Does the bee fly anyway?", "really long phrase");
        f.add(battle);
        f.setVisible(true);
        battle.start();

        while (battle.running);

        //f.dispose();
        System.out.println("Ended.");
    }

    /** Starts the battle.
     * Initializes Timer and runs it.
     */
    public void start() {
        timer = new Timer(20, tick);
        timer.start();
        running = true;
    }

    /**
     * Renders the screen.
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
        for (int i = 0; i < answer.length(); i++) {
            if (i < answered)
                g.drawString("" + answer.charAt(i), i * letterFont.getSize() + 20, Azmata.SCREEN_HEIGHT - 30);
            else
                g.drawString("_", i * letterFont.getSize() + 20, Azmata.SCREEN_HEIGHT - 30);
        }

        //Render the enemy and enemy health bar
        g.setColor(Color.RED);
        g.fillRect(MAIN_RIGHT, 0, 250, Azmata.SCREEN_HEIGHT);

        g.setColor(Color.BLACK);
        //Draw the questions if the user is holding down CTRL
        if(showQuestion) {
            g.setFont(questionFont);
            g.setColor(Color.WHITE);
            g.fillRect(questionX - 10, questionY - questionHeight - 10, questionWidth + 20, questionHeight + 20);
            g.setColor(Color.BLACK);
            g.drawString(question, questionX, questionY);
        }

        //FIXME
        if(!running) {
            g.drawString("YOU WON!", 500, 200);
            timer.stop();
        }

        canvas.drawImage(buffer, 0, 0, null);
        //Draw buffer to screen
    }

    /**
     * Spawns a tile onto the screen.
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
     * @param x The x-coordinate of the mouse click
     * @param y The y-coordinate of the mouse click
     */
    private void click(int x, int y) {
        if(answered == answer.length()) return;
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

        if (clickedCorrect) {
            //Some graphical thing
            //Deal damage to the enemy
            ++answered;

            if (answered == answer.length()) { //Win Battle
                //timer.stop();
                running = false;
            }
        }
    }
}