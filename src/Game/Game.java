package Game;

import Main.Azmata;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * A game panel that is used when the user is in game
 */
public class Game extends JPanel {
    Point movement_offset;
    private Player player;
    private volatile boolean quit = false;
    private List<NPC> npc_list = new ArrayList<>();
    private int animation_state = 0;
    /** The current state of the game */
    private GameState state;
    /** If the player is moving (don't accept user input during this time) */
    private boolean player_moving;

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
        npc_list.add(new NPC(6) {
            @Override
            public void onTalk() {
                NPC.say("lol", "hi");
            }

            @Override
            public void onPass() {

            }
        });
        movement_offset = new Point(0, 0);
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
        player = new Player(state);
    }

    @Override
    public void paintComponent(Graphics g) {
        Azmata.graphics = (Graphics2D) g;
        state.current_map.draw(state.player_pos, movement_offset);
        player.draw(animation_state % 3);
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
                if (player_moving) return;
                player.direction = dir;
                repaint();
                if (!player.canMove(dir)) return;
                player_moving = true;
                Timer move_timer = new Timer(10, new ActionListener() {
                    int moves = 0;

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (++moves > 32) {
                            player_moving = false;
                            animation_state = SpriteSheet.STANDING;
                            if (dir == Direction.DOWN) state.player_pos.y++;
                            if (dir == Direction.LEFT) state.player_pos.x--;
                            if (dir == Direction.RIGHT) state.player_pos.x++;
                            if (dir == Direction.UP) state.player_pos.y--;
                            movement_offset = new Point(0, 0);
                            repaint();
                            ((Timer) e.getSource()).stop();
                        }
                        animation_state = moves / 6;
                        switch (dir) {
                            case DOWN: movement_offset.y++;
                                break;
                            case LEFT: movement_offset.x--;
                                break;
                            case RIGHT: movement_offset.x++;
                                break;
                            case UP: movement_offset.y--;
                                break;
                        }
                        repaint();
                    }
                });
                move_timer.start();
            }
        };
    }

    /**
     * Display the game until the user wants to go back to the main menu
     */
    public void run() {
        revalidate();
        repaint();
        while (!quit) ;
        System.out.println("quitt");
    }
}