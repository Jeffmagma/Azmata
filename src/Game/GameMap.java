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
    /** Dimensions of the map */
    public Dimension map;
    /** You can walk on tile (x, y) if {@code can_walk[x][y]} */
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
        map = new Dimension();
        map.width = sc.nextInt();
        map.height = sc.nextInt();
        can_walk = new boolean[map.width][map.height];
        sc.nextLine();
        for (int i = 0; i < map.height; i++) {
            String line = sc.nextLine();
            Azmata.debug(line);
            for (int j = 0; j < map.width; j++) {
                can_walk[j][i] = line.charAt(j) != 'X';
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
        Point d = Game.getRelativePosition(new Point(0, 0));
        Azmata.graphics.setColor(Game.state.world.background());
        Azmata.graphics.fillRect(0, 0, Azmata.SCREEN_WIDTH, Azmata.SCREEN_HEIGHT);
        Azmata.graphics.drawImage(image, d.x, d.y, null);
    }
}