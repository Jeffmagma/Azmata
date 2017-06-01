package Game;

import Main.Azmata;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * A map in the game
 */
public class GameMap {
    private static Tile blank = null;
    /** The tiles in the map */
    public Tile[][] map;
    /** The map to temporarily draw the map to before drawing */
    private BufferedImage map_image;

    /**
     * Constructs a game map from a file
     *
     * @param path The path to read the map from
     */
    public GameMap(String path) {
        map_image = new BufferedImage(Azmata.SCREEN_WIDTH, Azmata.SCREEN_HEIGHT, BufferedImage.TYPE_INT_RGB);
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

    /**
     * Draws the map with the camera at a specific point
     */
    public void draw() {
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
        Azmata.graphics.drawImage(map_image, 0, 0, null);
    }
}