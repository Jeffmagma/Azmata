package Game;

import Main.Azmata;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
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
                for (String img : images) { //TODO actually do something here
                    map[i][j] = new Tile(Azmata.imageFromFile(img), sc.nextBoolean());
                }
            }
        }
    }

    // TODO: Find out how we are implementing the camera
    public void draw(Point camera) {
        for (int i = 0; i < Azmata.SCALE_X * Azmata.SCALE; i++) {
            for (int j = 0; j < Azmata.SCALE_Y * Azmata.SCALE; j++) {
                map[i][j].draw();
            }
        }
    }
}
