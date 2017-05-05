import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Map {
    Tile[][] map;

    public void load(File f) {
        int width, height;
        Scanner sc;
        try {
            sc = new Scanner(f);
        } catch (FileNotFoundException e) {
            System.err.println("The map " + f.getName() + " could not be found!");
            return;
        }
        width = sc.nextInt();
        height = sc.nextInt();
        map = new Tile[width][height];
        Set<BufferedImage> images = new HashSet<>();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {

            }
        }
    }
}
