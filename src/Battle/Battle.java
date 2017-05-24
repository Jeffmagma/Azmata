package Battle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class Battle extends JPanel {
    private Set<Tile> tiles = new HashSet<>();
    private Stack<Character> answerChars = new Stack<>();
    private String question;
    private int difficulty;
    private long tickCount;
    private Timer timer;
    private String letters = "QWERTYUIOPASDFGHJKLZXCVBNM";
    ActionListener tick = new ActionListener() {
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
    private boolean running;

    public Battle(int difficulty, String question, String answer) {
        this.question = question;
        this.difficulty = difficulty;
        tickCount = 0;

        answer = answer.toUpperCase();
        for (int i = answer.length() - 1; i >= 0; i--)
            answerChars.push(answer.charAt(i));

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent mouse) {
                click(mouse.getX(), mouse.getY());
            }
        });
    }

    public static void main(String[] args) {
        JFrame f = new JFrame();
        f.setSize(1024, 576);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Battle battle = new Battle(15, "What is a response to a question?", "answer");
        f.add(battle);
        f.setVisible(true);
        battle.start();
        battle.running = true;
        while (battle.running) ;
        System.out.println("Ended.");
    }

    public void start() {
        timer = new Timer(20, tick);
        timer.start();
    }

    protected void paintComponent(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics;
        int x, y, size;
        String letter;

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, 1024, 576);

        for (Tile tile : tiles) {
            x = tile.getX();
            y = tile.getY();
            size = tile.getSize();
            letter = "" + tile.getLetter();

            g.setColor(tile.getColor());
            g.fillOval(x - size / 2, y - size / 2, size, size);

            g.setFont(new Font("Verdana", Font.PLAIN, (int) (size / 1.5)));
            g.setColor(Color.BLACK);

            FontMetrics metrics = g.getFontMetrics(g.getFont());
            int cx = x - metrics.stringWidth(letter) / 2;
            int cy = y + (metrics.getAscent() - metrics.getDescent()) / 2;
            g.drawString(letter, cx, cy);
        }
    }

    private void spawn(boolean real) {
        int spawnSize = 150 - (5 * difficulty) + (int) (Math.random() * 20);
        if (real)
            tiles.add(new Tile(answerChars.peek(),
                    (int) (Math.random() * 1024),
                    (int) (Math.random() * 576),
                    spawnSize));
        else
            tiles.add(new Tile(letters.charAt((int) (Math.random() * 26)),
                    (int) (Math.random() * 1024),
                    (int) (Math.random() * 576),
                    spawnSize));
    }

    private void click(int x, int y) {
        int x2, y2;
        boolean clickedTile = false, clickedCorrect = false;

        for (Tile tile : tiles) {
            if (Math.hypot(tile.getX() - x, tile.getY() - y) <= tile.getSize() / 2) {
                clickedTile = true;
                if (tile.getLetter() == answerChars.peek())
                    clickedCorrect = true;
            }
        }

        tiles.removeIf((Tile tile) -> (Math.hypot(tile.getX() - x, tile.getY() - y) <= tile.getSize() / 2));

        if (clickedCorrect) {
            //Some graphical thing
            //Deal damage to the enemy
            System.out.println(answerChars.peek());
            if (!answerChars.empty()) answerChars.pop();
            if (answerChars.empty()) {
                //Win Battle
                timer.stop();
                running = false;
            }
        }
    }
}