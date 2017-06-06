package Menu;

import Game.HighScore;
import Main.Azmata;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * A class of the options inside the options menu
 *
 * @author Jeffrey Gao
 */
public class HighScores extends JPanel {
    /** When the user wants to return back to the main menu */
    private volatile boolean quit;

    /**
     * Constructs a panel that will display high scores
     */
    public HighScores() {
        Azmata.debug("HighScores Constructed");
        quit = false;
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "quit");
        getActionMap().put("quit", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                quit = true;
            }
        });
    }

    @Override
    public void paintComponent(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, Azmata.SCREEN_WIDTH, Azmata.SCREEN_HEIGHT);
        // TODO draw background
        g.setFont(new Font("Comic Sans MS", Font.BOLD, 42));
        g.setColor(Color.BLACK);
        for (int i = 0; i < 10 && i < Azmata.high_scores.size(); i++) {
            HighScore score = Azmata.high_scores.get(i);
            g.drawString(score.name, 70, 100 + 30 * i);
            g.drawString(String.valueOf(score.score), 530, 100 + 30 * i);
        }
    }

    /**
     * Show the options, returning when the user decides to quit
     */
    public void show() {
        revalidate();
        repaint();
        while (!quit) ;
    }
}