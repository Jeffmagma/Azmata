package Game;

import Main.Azmata;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;

/**
 * A game panel that is used when the user is in game
 *
 * @author Jeffrey Gao
 */
public class Game extends JPanel {
    /** The current state of the game */
    public static GameState state;
    /** How far in a direction the player has moved */
    private static Point movement_offset;
    /** Where the game will be saved */
    private static ObjectOutputStream save_game;
    /** The player that is playing the game */
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
        Azmata.debug("Game Constructed");
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
                        Azmata.debug("interacted");
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
        if (!Arrays.asList(state.beaten_worlds).contains(true)) {
            // TODO: the firs time the map is loaded
        }
        // TODO: every time a new map is loaded
        state.question = 0;
    }

    /**
     * Constructs a game with a previously saved game state
     *
     * @param game_state The game state to load
     */
    public Game(GameState game_state) {
        this();
        if (!game_state.in_game) quit = true;
        state = game_state;

        for (NPC npc : state.npc_list) {
            state.getMap().can_walk[npc.position.x][npc.position.y] = false;
        }
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
                if (Azmata.DEBUGGING) e.printStackTrace();
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        Azmata.graphics = (Graphics2D) g;
        state.getMap().draw();
        player.draw(animation_state %= 3);
        try{
            for (NPC npc : state.npc_list) {
                npc.draw();
            }
        }
        catch(ConcurrentModificationException e){
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
                            animation_state = SpriteSheet.STANDING;
                            // TODO: switch statement?
                            if (dir == Direction.DOWN) state.player_pos.y++;
                            if (dir == Direction.LEFT) state.player_pos.x--;
                            if (dir == Direction.RIGHT) state.player_pos.x++;
                            if (dir == Direction.UP) state.player_pos.y--;
                            movement_offset = new Point(0, 0);
                            repaint();
                            // TODO RIP passing code
                            /*
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
                            case DOWN:
                                movement_offset.y++;
                                break;
                            case LEFT:
                                movement_offset.x--;
                                break;
                            case RIGHT:
                                movement_offset.x++;
                                break;
                            case UP:
                                movement_offset.y--;
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
        Iterator<NPC> iterator;
        NPC npc;

        while (!quit && state.npc_list.size() > 0) {
            iterator = state.npc_list.iterator();
            //  Was throwing a ConcurrentModificationException because it was being removed
            //  inside the for-each loop
            while (iterator.hasNext()) {
                npc = iterator.next();
                if (npc.battling) {
                    npc.battle();
                    revalidate();
                    repaint();
                    iterator.remove();
                    save();
                }
            }
        }
        if (state.npc_list.isEmpty() && state.world != World.AIRLOO) state.beaten_worlds[state.world.value] = true;
        Azmata.debug("quit game");
        state.in_game = false;
    }

    /**
     * A world that can be played in the game
     */
    public enum World {
        EARTHLOO, FIRELOO, WATERLOO, AIRLOO;

        /** The previous world that was selected before Airloo */
        public static World prev_top;
        /** The index of the current world */
        public int value = ordinal();

        /**
         * Gets the spawn point of the map corresponding to this world
         *
         * @return The starting point on the map on this world
         */
        public Point getStartingPoint() {
            switch (this) {
                case EARTHLOO: return new Point(23, 42);
                case WATERLOO: return new Point(32, 43);
                case FIRELOO: return new Point(43, 22);
                case AIRLOO: return new Point(23, 45);
                default: throw new IllegalStateException("How did you add a new world?");
            }
        }

        public Color background() {
            switch (this) {
                case EARTHLOO: return Color.GREEN;
                case WATERLOO: return Color.WHITE;
                case FIRELOO: return Color.BLACK;
                case AIRLOO: return Color.WHITE;
                default: throw new IllegalStateException("How did you add a new world?");
            }
        }

        /**
         * Gets the corresponding map name to the world
         *
         * @return The corresponding map name to the world
         */
        public String getMapName() {
            switch (this) {
                case EARTHLOO: return "Earthloo.map";
                case WATERLOO: return "Waterloo.map";
                case FIRELOO: return "Fireloo.map";
                case AIRLOO: return "Airloo.map";
                default: throw new IllegalStateException("How did you add a new world?");
            }
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

        public ArrayList<Point> npcPoints() {
            switch (this) {
                case EARTHLOO: return new ArrayList<>(Arrays.asList(new Point(28, 33), new Point(27, 16), new Point(9, 11), new Point(7, 28), new Point(40, 10)));
                case FIRELOO: return new ArrayList<>(Arrays.asList(new Point(29, 20), new Point(34, 31), new Point(20, 26), new Point(6, 29), new Point(6, 9)));
                case WATERLOO: return new ArrayList<>(Arrays.asList(new Point(37, 34), new Point(28, 26), new Point(18, 33), new Point(6, 27), new Point(12, 17)));
                case AIRLOO: return new ArrayList<>(Arrays.asList(new Point(22, 35), new Point(11, 33), new Point(33, 24), new Point(10, 23), new Point(23, 14)));
                default: throw new IllegalStateException("How did you add a new world?");
            }
        }
    }
}