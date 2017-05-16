package Game;

import Main.Azmata;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
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
        Scanner sc;
        try {
            sc = new Scanner(f);
        } catch (FileNotFoundException e) {
            System.err.println("The map " + f.getName() + " was not found!");
            if (Azmata.DEBUGGING) e.printStackTrace();
        }
    }

    public void draw(Point camera) {
        for (int i = 0; i < Azmata.SCALE_X * Azmata.SCALE; i++) {
            for (int j = 0; j < Azmata.SCALE_Y * Azmata.SCALE; j++) {
                map[i][j].draw();
            }
        }
    }
}
