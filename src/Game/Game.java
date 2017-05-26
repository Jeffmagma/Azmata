package Game;

import Main.Azmata;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.geom.Point2D;

/**
 * A game panel that is used when the user is in game
 */
public class Game extends JPanel {
    Player player;
    volatile boolean quit = false;
    /** The current state of the game */
    private GameState state;
    /** If the player is moving (don't accept user input during this time) */
    private boolean player_moving;
    /** Where the camera currently is */
    private Point2D.Double camera_location;

    /**
     * Constructs a game with the default move bindings
     */
    public Game() {
        if (Azmata.DEBUGGING) System.out.println("Game Constructed");
        InputMap input_map = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        input_map.put(KeyStroke.getKeyStroke("UP"), "move_up");
        input_map.put(KeyStroke.getKeyStroke("LEFT"), "move_left");
        input_map.put(KeyStroke.getKeyStroke("DOWN"), "move_down");
        input_map.put(KeyStroke.getKeyStroke("RIGHT"), "move_right");
        input_map.put(KeyStroke.getKeyStroke("ESCAPE"), "quit");
        getActionMap().put("move_up", move(Direction.UP));
        getActionMap().put("move_left", move(Direction.LEFT));
        getActionMap().put("move_down", move(Direction.DOWN));
        getActionMap().put("move_right", move(Direction.RIGHT));
        getActionMap().put("quit", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                quit = true;
            }
        });
    }

    /**
     * Constructs a game with the player at a certain position
     *
     * @param player_pos Where the player is
     */
    public Game(Point player_pos) {
        this(new GameState(player_pos));
    }

    /**
     * Constructs a game with a previously saved game state
     *
     * @param game_state The game state to load
     */
    public Game(GameState game_state) {
        this();
        state = game_state;
        state.current_map = new GameMap("Maps/Map.map");
        camera_location = new Point2D.Double(state.player_pos.x - Azmata.SCALE_X * Azmata.SCALE / 2, state.player_pos.y - Azmata.SCALE_Y * Azmata.SCALE / 2);
    }

    @Override
    public void paintComponent(Graphics g) {
        Azmata.graphics = (Graphics2D) g;
        state.current_map.draw(camera_location);
    }

    /**
     * Constructs an AbstractAction that moves the player based on a direction
     *
     * @param dir The direction to move in
     * @return An AbstractAction that moves the player based on a direction
     */
    private AbstractAction move(Direction dir) {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!player_moving) {
                    player_moving = true;
                    for (int i = 0; i < 1000; i++) {
                        switch (dir) {
                            case DOWN:
                                camera_location.y += .001;
                                break;
                            case LEFT:
                                camera_location.x -= .001;
                                break;
                            case RIGHT:
                                camera_location.x += .001;
                                break;
                            case UP:
                                camera_location.y -= .001;
                                break;
                        }
                        revalidate();
                        repaint();
                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                    }
                    camera_location.x = Math.round(camera_location.x);
                    camera_location.y = Math.round(camera_location.y);
                    switch (dir) {
                        case DOWN:
                            state.player_pos.y++;
                            break;
                        case LEFT:
                            state.player_pos.x--;
                            break;
                        case RIGHT:
                            state.player_pos.x++;
                            break;
                        case UP:
                            state.player_pos.y--;
                            break;
                    }
                    repaint();
                }
                player_moving = false;
            }
        };
    }

    public void run() {
        revalidate();
        repaint();
        while (!quit) ;
        System.out.println("quitt");
    }
}
