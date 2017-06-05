package Game;

import Main.Azmata;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Scanner;

/**
 * A map in the game
 *
 * @author Jeffrey Gao
 */
public class GameMap {
    boolean[][] can_walk;
    /** The map to temporarily draw the map to before drawing */
    private BufferedImage image;

    /**
     * Constructs a game map from a file
     *
     * @param path The path to read the map from
     */
    public GameMap(String path) {
        Scanner sc = new Scanner(Azmata.class.getClassLoader().getResourceAsStream(path));
        String map_image = sc.nextLine();
        image = Azmata.imageFromFile(map_image);
        Dimension d = new Dimension();
        d.width = sc.nextInt();
        d.height = sc.nextInt();
        can_walk = new boolean[d.width][d.height];
        sc.nextLine();
        for (int i = 0; i < d.width; i++) {
            String line = sc.nextLine();
            for (int j = 0; j < d.height; j++) {
                can_walk[i][j] = line.charAt(i) == 'X';
            }
        }
    }

    /**
     * Draws the map with the camera at a specific point
     */
    public void draw() {
        /*
        Graphics2D image_graphics = map_image.createGraphics();
        image_graphics.setColor(Color.WHITE);
        image_graphics.fillRect(0, 0, Azmata.SCREEN_WIDTH, Azmata.SCREEN_HEIGHT);
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                Point draw_point = Game.getRelativePosition(new Point(i, j));
                if (draw_point.x + Azmata.BLOCK_SIZE >= 0 && draw_point.x < Azmata.SCREEN_WIDTH
                        && draw_point.y + Azmata.BLOCK_SIZE >= 0 && draw_point.y < Azmata.SCREEN_HEIGHT)
                    map[i][j].draw(draw_point, image_graphics);
            }
        }
        Azmata.graphics.drawImage(map_image, 0, 0, null);*/
        Point pos = new Point(Game.state.player_pos.x - Game.movement_offset.x, Game.state.player_pos.y - Game.movement_offset.y);
        Azmata.graphics.drawImage(image.getSubimage(pos.x, pos.y, Azmata.SCREEN_WIDTH, Azmata.SCREEN_HEIGHT), 0, 0, null);
    }
}