package Game;

import Main.Azmata;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * A game panel that is used when the user is in game
 */
public class Game extends JPanel {
    /** The current state of the game */
    public static GameState state;
    static ObjectOutputStream save_game;
    /** How far in a direction the player has moved */
    private static Point movement_offset;
    private Player player;
    /** If the player wants to quit the game */
    private volatile boolean quit = false;
    /** The current state of the player walking */
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
        getActionMap().put("interact", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (NPC npc : state.npc_list) {
                    if (npc.position.equals(player.direction.to(state.player_pos))) {
                        npc.direction = player.direction.opposite();
                        System.out.println("interacted");
                        npc.onTalk();
                        break;
                    }
                }
            }
        });
        movement_offset = new Point(0, 0);
    }

    /**
     * Constructs a game with the player at a certain position
     *
     * @param game_world Which world the player spawns at
     */
    public Game(World game_world) {
        this(new GameState(game_world));
        // show intro
    }

    /**
     * Constructs a game with a previously saved game state
     *
     * @param game_state The game state to load
     */
    public Game(GameState game_state) {
        this();
        state = game_state;
        state.npc_list.add(new NPC(new Point(3, 3), new SpriteSheet("Sprites/Characters/eric.png", "Sprites/Faces/eric.png")) {
            @Override
            public void onTalk() {
                if (battle()) state.npc_list.remove(this);
                //say("lol", "hi");
            }
        });
        player = new Player();
    }

    /**
     * Calculates the pixel coordinates that the object should be drawn at if it is at specific coordinates
     *
     * @param position The tile coordinate the object is
     * @return The pixel that it should be drawn at
     */
    static Point getRelativePosition(Point position) {
        return new Point(Azmata.SCREEN_WIDTH / 2 + (position.x - state.player_pos.x) * Azmata.BLOCK_SIZE - movement_offset.x, Azmata.SCREEN_HEIGHT / 2 + (position.y - state.player_pos.y) * Azmata.BLOCK_SIZE - movement_offset.y);
    }

    /**
     * Save the game to the save file, retrying until it works
     */
    static void save() {
        while (true) {
            try {
                Azmata.saveDirectory().getParentFile().mkdirs();
                Azmata.saveDirectory().createNewFile();
                save_game = new ObjectOutputStream(new FileOutputStream(Azmata.saveDirectory()));
                save_game.writeObject(state);
                break;
            } catch (IOException | NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        Azmata.graphics = (Graphics2D) g;
        state.getMap().draw();
        player.draw(animation_state %= 3);
        for (NPC npc : state.npc_list) {
            npc.draw();
            state.getMap().map[npc.position.x][npc.position.y].can_walk = false;
            if (false) npc.onTalk();
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

    public enum World {
        EARTHLOO, WATERLOO, FIRELOO, AIRLOO;

        public static World prev_top;
        int value = ordinal();

        public Point getStartingPoint() {
            switch (this) {
                case EARTHLOO: return new Point(6, 9);
                case WATERLOO: return new Point(6, 9);
                case FIRELOO: return new Point(6, 9);
                case AIRLOO: return new Point(6, 9);
                default: throw new IllegalArgumentException("How did you add a new word?");
            }
        }

        public String getMapName() {
            switch (this) {
                case EARTHLOO: return "Earthloo.map";
                case WATERLOO: return "Waterloo.map";
                case FIRELOO: return "Fireloo.map";
                case AIRLOO: return "Airloo.map";
                default: throw new IllegalArgumentException("How did you add a new word?");
            }
        }


        public World top() {
            return prev_top;
        }

        public World bottom() {
            return AIRLOO;
        }

        /**
         * Returns the next world horizontally, but the current one if it's the last one
         *
         * @return The next world horizontally, but the current one if it's the last one
         */
        public World next() {
            if (this == AIRLOO) return AIRLOO;
            return values()[value + 1 < AIRLOO.value ? value + 1 : value];
        }

        /**
         * Returns the previous world horizontally, but the current one if it's the first one
         *
         * @return The previous world horizontally, but the current one if it's the first one
         */
        public World prev() {
            if (this == AIRLOO) return AIRLOO;
            return values()[value > 0 ? value - 1 : value];
        }
    }
}