package Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * A map in the game
 *
 * @author Jeffrey Gao
 * @author Richard Yi
 */
public class GameMap {
    /**
     * The tiles in the map
     */
    private Tile[][] map;

    /**
     * Loads a map from a file. The map is formatted like
     *
     * Tiles
     * 1 filename
     * 2 filename2
     * 3 filename3
     *
     * [width]
     * [height]
     *
     * 1 1 1 1
     * 2 1 2 2
     * 3 3 3 3
     *
     * First there are numbers with filenames, then the width and height, then numbers that are the actual tiles and correspond tot he tile images
     *
     * @param f The file to load the map from
     */
    public void load(File f) {
        int width, height;
        Scanner sc;
        try {
            sc = new Scanner(f);
        } catch (FileNotFoundException e) {
            System.err.println("The map " + f.getName() + " could not be found!");
            return;
        }
        String s;
        Map<Integer, Tile> tiles = new HashMap<>();
        int tile = 1;
        while (!(s = sc.nextLine().trim()).equals("Tiles")) {
            String image_name = sc.next();
            File image_file = new File(image_name);
            Image image;
            try {
                image = ImageIO.read(image_file);
            } catch (IOException e) {
                System.err.println("The tile image " + image_file.getName() + " could not be found!");
                return;
            }
            tiles.put(tile++, new Tile(image, sc.nextBoolean()));
        }
        width = sc.nextInt();
        height = sc.nextInt();
        map = new Tile[width][height];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                map[i][j] = tiles.get(sc.nextInt());
            }
        }
    }

    public void draw() {
        for (Tile[] a : map) {
            for (Tile t : a) {
                t.draw();
            }
        }
    }
}
