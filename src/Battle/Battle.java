//package Battle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;
import java.util.HashSet;
import java.util.Stack;

public class Battle extends JPanel {
    private Set<Tile> tiles = new HashSet<>();
	private Stack<Character> answerChars = new Stack<>();
	private String question;
    private int difficulty;
    private long tickCount;
    private Timer t;
	private String letters = "QWERTYUIOPASDFGHJKLZXCVBNM";

    ActionListener tick = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            ++tickCount;

			if(tickCount % (200 - difficulty * 10) == 0){
				if(Math.random() < 1.0 / difficulty * 2)
					spawn(true);
				else
					spawn(false);
			}

            for (Tile tile : tiles) {
				tile.tick();
				tile.moveX((int) (tickCount / 50 + tile.hashCode()) % 5 - 2);
				tile.moveY((int) (tickCount / 50 + tile.hashCode() - 1) % 5 - 2);
            }

			tiles.removeIf((Tile tile) -> {
				return (tile.getAge() >= 2000 - difficulty * 100);
			});

			repaint();
			revalidate();
        }
    };

    public Battle(int difficulty, String question, String answer) {
		//setMinimumSize(new Dimension(1024, 576));
		this.question = question;
        this.difficulty = difficulty;
        tickCount = 0;

		answer = answer.toUpperCase();
		for(int i = answer.length() - 1; i >= 0; i--)
			answerChars.push(answer.charAt(i));

        t = new Timer(20, tick);
        t.start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame();
            f.setSize(1024, 576);
            f.setDefaultCloseOperation(2);
            f.add(new Battle(19, "What is a respose to a question?", "answer"));
            f.setVisible(true);
        });
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

	private void spawn(boolean real){
		int spawnSize = 150 - (5 * difficulty) + (int) (Math.random() * 20);
		if(real)
			tiles.add(new Tile(answerChars.peek(),
						(int) (Math.random() * 1024),
						(int) (Math.random() * 576),
						spawnSize));
		else
			tiles.add(new Tile(/*letters.charAt((int) (Math.random() * 26*/'W',
						(int) (Math.random() * 1024),
						(int) (Math.random() * 576),
						spawnSize));
	}
}
