package Battle;

import java.awt.*;

public class Tile {
    private char letter;
    private int x;
    private int y;
    private int size;
    private Color color;

    public Tile(char l, int initX, int initY, int initSize, Color c) {
        letter = l;
        x = initX;
        y = initY;
        size = initSize;
        color = c;
    }

    public static void main(String[] args) {
        Tile t = new Tile('A', 100, 200, 50, Color.blue);
        Tile u = new Tile('X', 69, 420, 69, Color.red);
        System.out.println(t.hashCode());
        System.out.println(u.hashCode());
    }

    public void moveX(int dx) {
        x = (x + dx) % 1024;
    }

    public void moveY(int dy) {
        y = (y + dy) % 576;
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
}