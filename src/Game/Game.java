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
    /** The current state of the game */
    public static GameState state;
    /** How far in a direction the player has moved */
    private static Point movement_offset;
    private Player player;
    private volatile boolean quit = false;
    private List<NPC> npc_list = new ArrayList<>();
    private int animation_state = 0;
    /** If the player is moving (don't accept user input during this time) */
    private boolean player_moving;

    /**
     * Constructs a game with the default move bindings
     */
    private Game() {
        if (Azmata.DEBUGGING) System.out.println("Game Constructed");
        InputMap input_map = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        input_map.put(KeyStroke.getKeyStroke("UP"), "move_up");
        input_map.put(KeyStroke.getKeyStroke("LEFT"), "move_left");
        input_map.put(KeyStroke.getKeyStroke("DOWN"), "move_down");
        input_map.put(KeyStroke.getKeyStroke("RIGHT"), "move_right");
        input_map.put(KeyStroke.getKeyStroke("ESCAPE"), "quit");
        input_map.put(KeyStroke.getKeyStroke("ENTER"), "interact");
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
        npc_list.add(new NPC(new Point(3, 3), new SpriteSheet(Azmata.imageFromFile("Sprites/Characters/eric.png"), Azmata.imageFromFile("Sprites/Faces/eric.png"))) {
            @Override
            public void onTalk() {
                say("lol", "hi");
            }

            @Override
            public void onPass() {

            }
        });
        getActionMap().put("interact", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (NPC npc : npc_list) {
                    if (npc.position.equals(player.direction.to(state.player_pos))) {
                        System.out.println("interacted");
                        repaint();
                        npc.onTalk();
                        repaint();
                        break;
                    } else System.out.println(npc.position + " " + player.direction.to(state.player_pos));
                }
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
        this(new GameState(player_pos, 100.0));
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
        player = new Player();
    }

    /**
     * Calculates the pixel coordinates that the object should be drawn at if it is at specific coordinates
     *
     * @param position The tile coordinate the object is
     * @return The pixel that it should be drawn at
     */
    static Point getRelativePosition(Point position) {
        return new Point(Azmata.SCREEN_WIDTH / 2 + (position.x - state.player_pos.x) * 32 - movement_offset.x, Azmata.SCREEN_HEIGHT / 2 + (position.y - state.player_pos.y) * 32 - movement_offset.y);
    }

    @Override
    public void paintComponent(Graphics g) {
        Azmata.graphics = (Graphics2D) g;
        state.current_map.draw();
        player.draw(animation_state %= 3);
        for (NPC npc : npc_list) {
            npc.draw();
        }
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
                        // The end of the move
                        if (++moves > 32) {
                            player_moving = false;
                            animation_state = 2;
                            if (dir == Direction.DOWN) state.player_pos.y++;
                            if (dir == Direction.LEFT) state.player_pos.x--;
                            if (dir == Direction.RIGHT) state.player_pos.x++;
                            if (dir == Direction.UP) state.player_pos.y--;
                            movement_offset = new Point(0, 0);
                            repaint();/*
                            for (NPC npc : npc_list) {
                                switch (npc.direction) {
                                    case DOWN:
                                        if (state.player_pos.x == npc.position.x && state.player_pos.y > npc.position.y && state.player_pos.y <= npc.position.y + npc.pass_distance) {
                                            npc.onPass();
                                        }
                                        break;
                                    case LEFT:
                                        if (state.player_pos.y == npc.position.y && state.player_pos.x < npc.position.x && state.player_pos.x >= npc.position.x - npc.pass_distance) {
                                            npc.onPass();
                                        }
                                        break;
                                    case RIGHT:
                                        if (state.player_pos.y == npc.position.y && state.player_pos.x > npc.position.x && state.player_pos.x <= npc.position.x + npc.pass_distance) {
                                            npc.onPass();
                                        }
                                        break;
                                    case UP:
                                        if (state.player_pos.x == npc.position.x && state.player_pos.y < npc.position.y && state.player_pos.y >= npc.position.y - npc.pass_distance) {
                                            npc.onPass();
                                        }
                                        break;
                                }
                            }*/
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