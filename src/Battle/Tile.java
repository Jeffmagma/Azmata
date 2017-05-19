//package Battle;

import java.awt.*;

public class Tile {
    private char letter;
    private int x;
    private int y;
    private int size;
	private int age;
    private Color color;
	private Color[] colors = {Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.BLUE, Color.MAGENTA};

    public Tile(char l, int initX, int initY, int initSize) {
        letter = l;
        x = initX;
        y = initY;
        size = initSize;
		color = colors[(int) (Math.random() * 6)];
    }

    public static void main(String[] args) {
        Tile t = new Tile('A', 100, 200, 50);
        Tile u = new Tile('X', 69, 420, 69);
        System.out.println(t.hashCode());
        System.out.println(u.hashCode());
    }

	public void tick(){
		++age;
	}

    public void moveX(int dx) {
		x += dx;
		if(x - size / 2 < 0) x = size / 2;
		if(x + size / 2 > 1024) x = 1024 - size / 2;
    }

    public void moveY(int dy) {
        y += dy;
		if(y - size / 2 < 0) y = size / 2;
		if(y + size / 2 > 576) y = 576 - size / 2;
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

	public int getAge(){
		return age;
	}
}
