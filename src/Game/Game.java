package Game;

import Main.Azmata;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.geom.Point2D;

public class Game extends JPanel {
    GameState state;
    boolean player_moving;
    /**
     * Since the player is a grid, the player position
     */
    private Point2D.Double camera_location;

    public Game(Point player_pos) {
        state = new GameState(player_pos);
        getInputMap().put(KeyStroke.getKeyStroke("UP"), "move_up");
        getInputMap().put(KeyStroke.getKeyStroke("LEFT"), "move_left");
        getInputMap().put(KeyStroke.getKeyStroke("DOWN"), "move_down");
        getInputMap().put(KeyStroke.getKeyStroke("RIGHT"), "move_right");
        getActionMap().put("move_up", move(Direction.UP));
        getActionMap().put("move_left", move(Direction.LEFT));
        getActionMap().put("move_down", move(Direction.DOWN));
        getActionMap().put("move_right", move(Direction.RIGHT));
    }

    @Override
    public void paintComponent(Graphics g) {
        Azmata.graphics = (Graphics2D) g;
        state.current_map.draw(camera_location);
    }

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
                        repaint();
                    }
                    camera_location.x = Math.round(camera_location.x);
                    camera_location.y = Math.round(camera_location.y);
                    repaint();
                }
            }
        };
    }
}
