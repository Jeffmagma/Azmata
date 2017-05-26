package Game;

import Main.Azmata;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * A map in the game
 */
public class GameMap {
    static Tile blank = null;
    /**
     * The tiles in the map
     */
    private Tile[][] map;

    /**
     * Constructs a game map from a file
     *
     * @param path The path to read the map from
     */
    public GameMap(String path) {
        if (blank == null) {
            blank = new Tile(false);
            blank.getImages().add(Azmata.imageFromFile("Maps/black.png"));
        }
        /*
        Stored like
        -----------
        1 Filename
        2 Filename
        3 Filename
        end
        Width(2)
        Height(3)
        1-2 false
        1-2 false
        1-3 false
        1 true
        1 true
        1-2 false
        */
        Scanner sc = new Scanner(Azmata.class.getClassLoader().getResourceAsStream(path));
        Map<Integer, Image> temp_images = new HashMap<>();
        String s;
        while (!(s = sc.nextLine()).equals("end")) {
            int split = s.indexOf(' ');
            temp_images.put(Integer.parseInt(s.substring(0, split)), Azmata.imageFromFile(s.substring(split + 1)));
        }
        int w = sc.nextInt(), h = sc.nextInt();
        map = new Tile[w][h];
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                String[] images = sc.next().split("-");
                map[i][j] = new Tile(sc.nextBoolean());
                for (String img : images) {
                    map[i][j].getImages().add(temp_images.get(Integer.parseInt(img)));
                }
            }
        }
    }
    // TODO: Find out how we are implementing the camera

    /**
     * Draws the map with the camera at a specific point
     *
     * @param camera Where the camera is on the map
     */
    public void draw(Point2D.Double camera) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                map[i][j].draw(new Point(i * 32, j * 32));
            }
        }
        Azmata.graphics.translate(camera.x, camera.y);
        /*for (int x = (int) Math.floor(camera.x); x <= Math.ceil(camera.x); x++) {
            for (int y = (int) Math.floor(camera.y); y <= Math.ceil(camera.y); y++) {
                Point2D.Double location = new Point2D.Double(x - (int) Math.floor(camera.x), y - (int) Math.floor(camera.y));
                if (x < 0 || x >= map.length || y < 0 || y >= map[x].length) blank.draw(location);
                else map[x][y].draw(location);
            }
        }*/
    }
}
