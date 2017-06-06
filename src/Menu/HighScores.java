package Menu;

import Main.Azmata;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;

/**
 * A class of the options inside the options menu
 *
 * @author Jeffrey Gao
 */
public class HighScores extends JPanel {
    /** The scores of all the players */
    private Map<String, Integer> scores;
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
        scores = new HashMap<>();
        // TODO read from save
        scores.put("lol", 1);
        scores.put("kek", 2);
    }

    @Override
    public void paintComponent(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, Azmata.SCREEN_WIDTH, Azmata.SCREEN_HEIGHT);
        // TODO draw background
        int index = 0;
        g.setColor(Color.BLACK);
        for (Map.Entry score : scores.entrySet()) {
            String player = (String) score.getKey();
            int sc = (Integer) score.getValue();
            // TODO draw the scores
            g.drawString(player, 20, 100 + index * 30);
            g.drawString(String.valueOf(sc), 500, 100 + index * 30);
            index++;
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