package Battle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class Battle extends JPanel {
    private Set<Tile> tiles = new HashSet<>();
    private Stack<Character> answerChars = new Stack<>();
    private List<Character> answered = new ArrayList<>();
    private String question;
    private String answer;
    private boolean running;
    private int difficulty;
    private long tickCount;
    private javax.swing.Timer timer;
    private Font tileFont;

    private static final String LETTERS = "QWERTYUIOPASDFGHJKLZXCVBNM ";
    private static final Font LETTER_FONT = new Font("Courier New", Font.PLAIN, 40);

    private ActionListener tick = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            ++tickCount;

            if (tickCount % (100 - difficulty * 5) == 0) {
                if (Math.random() < 1.0 / difficulty * 2)
                    spawn(true);
                else
                    spawn(false);
            }

            for (Tile tile : tiles) {
                tile.tick();
                tile.moveX((int) (tickCount / 50 + tile.hashCode()) % 5 - 2);
                tile.moveY((int) (tickCount / 50 + tile.hashCode() - 1) % 5 - 2);
            }

            tiles.removeIf((Tile tile) -> (tile.getAge() >= 5000 - difficulty * 250));

            repaint();
            revalidate();
        }
    };

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

    public static void main(String[] args) throws InterruptedException{
        JFrame f = new JFrame();
        f.setSize(1024, 576);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Battle battle = new Battle(5, "What is a response to a question?", "dank memes");
        f.add(battle);
        f.setVisible(true);
        battle.start();

        while (battle.running){
            Thread.sleep(0);
        }

        //f.dispose();
        System.out.println("Ended.");
    }

    public void start() {
        timer = new javax.swing.Timer(20, tick);
        timer.start();
        running = true;
    }

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

        g.setFont(LETTER_FONT);
        for (int i = 0; i < answer.length(); i++) {
            if (i < answered.size())
                g.drawString("" + answered.get(i), i * 50 + 20, getHeight() - 30);
            else
                g.drawString("_", i * 50 + 20, getHeight() - 30);
        }

        if(!running)
            g.drawString("YOU WON!", 500, 200);
    }

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
        } }
}