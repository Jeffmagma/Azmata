package Battle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

/**
    Battle mode in the game
    <h2>Course Info</h2>
    <i>ICS4U0 with Mrs. Krasteva</i>
    @author Richard Yi
    @version 1.0
    @since 2017-05-10
*/
public class Battle extends JPanel {
    /**Contains tiles on the screen that contain a letter.*/
    private Set<Tile> tiles = new HashSet<>();
    /**A stack where the top keeps track of which is the next correct letter the user hasn't spelled.*/
    private Stack<Character> answerChars = new Stack<>();
    /**All characters that the user has correctly answered.*/
    private List<Character> answered = new ArrayList<>();
    /**The question for the current battle.*/
    private String question;
    /**The answer for the current battle.*/
    private String answer;
    /**Whether the Battle is still running.*/
    private volatile boolean running;
    /**The difficulty of the battle.*/
    private int difficulty;
    /**How many game ticks have elapsed in the battle.*/
    private long tickCount;
    /**A timer that repeatedly calls the game tick process.
     * @see Battle#tick
    */
    private javax.swing.Timer timer;
    /**The font to draw the file with*/
    private Font tileFont;

    /**All the letters in the alphabet, including space.*/
    private static final String LETTERS = "QWERTYUIOPASDFGHJKLZXCVBNM ";
    /**The font used to draw the letters in the bottom bar*/
    private static final Font LETTER_FONT = new Font("Courier New", Font.PLAIN, 40);

    /**Main game tick process.
     * Does processing and renders the frame.
     * @see Battle#paintComponent(Graphics)
     * */
    private ActionListener tick = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            ++tickCount;

            if (tickCount % Math.max(10, (100 - difficulty * 10)) == 0) {
                if (Math.random() < 0.5)
                    spawn(true);
                else
                    spawn(false);
            }

            for (Tile tile : tiles) {
                tile.tick();

                if(tile.isBlowing()) { //FIXME TODO XXX
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
        this.answer = answer;
        this.difficulty = difficulty;
        tickCount = 0;
        tileFont = new Font("Verdana", Font.PLAIN, (int) ((150 - 5 * difficulty) / 1.5));

        answer = answer.toUpperCase();
        for (int i = answer.length() - 1; i >= 0; i--)
            answerChars.push(answer.charAt(i));

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent mouse) {
                click(mouse.getX(), mouse.getY());
            }
        });
    }

    /**Main method. For testing only.*/
    public static void main(String[] args) throws InterruptedException{
        JFrame f = new JFrame();
        f.setSize(1024, 576);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Battle battle = new Battle(5, "What is a response to a question?", "dank memes");
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
        timer = new javax.swing.Timer(20, tick);
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
        Graphics2D g = (Graphics2D) graphics;
        int x, y, size;
        String letter;

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());

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
        g.setFont(LETTER_FONT);

        for (int i = 0; i < answer.length(); i++) {
            if (i < answered.size())
                g.drawString("" + answered.get(i), i * 50 + 20, getHeight() - 30);
            else
                g.drawString("_", i * 50 + 20, getHeight() - 30);
        }

        if(!running) {
            g.drawString("YOU WON!", 500, 200);
            timer.stop();
        }
    }

    /**
     * Spawns a tile onto the screen.
     * @param real Whether the tile to spawn is of the correct answer.
     */
    private void spawn(boolean real) {
        int spawnSize = 150 - (5 * difficulty) + (int) (Math.random() * 5);
        if (real && !answerChars.empty())
            tiles.add(new Tile(answerChars.peek(),
                    (int) (Math.random() * getWidth()),
                    (int) (Math.random() * getHeight() - 50),
                    spawnSize));
        else
            tiles.add(new Tile(LETTERS.charAt((int) (Math.random() * LETTERS.length())),
                    (int) (Math.random() * getWidth()),
                    (int) (Math.random() * getHeight() - 50),
                    spawnSize));
    }

    /**
     * Triggered when the mouse is clicked.
     * Checks if the user clicks on a tile and if it's correct.
     * @param x The x-coordinate of the mouse click
     * @param y The y-coordinate of the mouse click
     */
    private void click(int x, int y) {
        int x2, y2;
        boolean clickedTile = false, clickedCorrect = false;

        for (Tile tile : tiles) {
            if (Math.hypot(tile.getX() - x, tile.getY() - y) <= tile.getSize() / 2) {
                clickedTile = true;
                if (!answerChars.empty() && tile.getLetter() == answerChars.peek())
                    clickedCorrect = true;
            }
        }

        tiles.removeIf((Tile tile) -> (Math.hypot(tile.getX() - x, tile.getY() - y) <= tile.getSize() / 2));

        if (clickedCorrect) {
            //Some graphical thing
            //Deal damage to the enemy
            if (!answerChars.empty()) answered.add(answerChars.pop());

            if (answerChars.empty()) { //Win Battle
                //timer.stop();
                running = false;
            }
        }
    }
}