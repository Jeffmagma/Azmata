package Battle;

import java.awt.*;

public class Tile {
    /**The letter that the tile displays.*/
    private char letter;
    /**The current X-coordinate of the tile.*/
    private int x;
    /**the current y-coordinate of the tile.*/
    private int y;
    /**The size of the tile.*/
    private int size;
    /**How many ticks the tile has been alive for.*/
    private int age;
    /**The color of the tile.*/
    private Color color;
    /**Whether or not the tile is being blown.*/
    private boolean blowing;

    private static final Color[] COLORS = {Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.BLUE, Color.MAGENTA};
    private static final int TOP = 100;
    private static final int BOTTOM = 500;
    private static final int LEFT = 0;
    private static final int RIGHT = 1024;

    public Tile(char l, int initX, int initY, int initSize) {
        letter = l;
        x = initX;
        y = initY;
        size = initSize;
        blowing = false;
        color = COLORS[(int) (Math.random() * 6)];
    }

    public static void main(String[] args) {
        Tile t = new Tile('A', 100, 200, 50);
        Tile u = new Tile('X', 69, 420, 69);
        System.out.println(t.hashCode());
        System.out.println(u.hashCode());
    }

    public void tick() {
        ++age;
    }

    public void moveX(int dx) {
        x += dx;
        if (x - size / 2 < LEFT) x = LEFT + size / 2;
        if (x + size / 2 > RIGHT) x = RIGHT - size / 2;
    }

    public void moveY(int dy) {
        y += dy;
        if (y - size / 2 < TOP) y = TOP + size / 2;
        if (y + size / 2 > BOTTOM) y = BOTTOM - size / 2;
    }

    public void changeSize(int ds) {
        size += ds;
    }

    public char getLetter() {
        return letter;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int s) {
        size = s;
    }

    public Color getColor() {
        return color;
    }

    public int getAge() {
        return age;
    }

    public void startBlowing(){
        blowing = true;
    }

    public void stopBlowing(){
        blowing = false;
    }

    public boolean isBlowing() { return blowing; }
}
