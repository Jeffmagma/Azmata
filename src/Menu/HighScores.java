package Menu;

import Game.HighScore;
import Main.Azmata;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

/**
 * A class of the options inside the options menu
 *
 * @author Jeffrey Gao
 */
public class HighScores extends JPanel {
    /** The background of the thing */
    BufferedImage back;
    /** When the user wants to return back to the main menu */
    private volatile boolean quit;

    /**
     * Constructs a panel that will display high scores
     */
    public HighScores() {
        back = Azmata.imageFromFile("Menu/highscores.png");
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
        g.drawImage(back, 0, 0, null);
        // TODO draw background
        g.setFont(new Font("Comic Sans MS", Font.BOLD, 30));
        for (int i = 0; i < 10 && i < Azmata.high_scores.size(); i++) {
            HighScore score = Azmata.high_scores.get(i);
            if (i < 5) {
                g.drawString(score.name, 100, 300 + 35 * i);
                g.drawString(String.valueOf(score.score), 330, 300 + 35 * i);
            } else {
                g.drawString(score.name, 600, 300 + 35 * (i - 5));
                g.drawString(String.valueOf(score.score), 825, 300 + 35 * (i - 5));
            }
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