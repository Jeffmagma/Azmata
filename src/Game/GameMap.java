package Game;

import Main.Azmata;

import java.awt.*;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * A map in the game
 */
public class GameMap {
    /**
     * The tiles in the map
     */
    private Tile[][] map;

    /**
     * Constructs a game map from a file
     *
     * @param f the file to read the map from
     */
    public GameMap(File f) {
        /*
        Stored like
        -----------
        1 Filename
        2 Filename
        3 Filename
        Width(2)
        Height(3)
        1-2 false
        1-2 false
        1-3 false
        1 true
        1 true
        1-2 false
        */
        Scanner sc;
        try {
            sc = new Scanner(f);
        } catch (FileNotFoundException e) {
            System.err.println("The map " + f.getName() + " was not found!");
            if (Azmata.DEBUGGING) e.printStackTrace();
            return;
        }
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
                String[] images = sc.nextLine().split("-");
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
        for (int x = (int) Math.floor(camera.x); x <= Math.ceil(camera.x); x++) {
            for (int y = (int) Math.floor(camera.y); y <= Math.ceil(camera.y); y++) {
                map[x][y].draw();
            }
        }
    }
}
