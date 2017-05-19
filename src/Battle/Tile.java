import java.awt.Color;

public class Tile{
	private char letter;
	private int x;
	private int y;
	private int size;
	private Color color;
	
	public Tile(char l, int initX, int initY, int initSize, Color c){
		letter = l;
		x = initX;
		y = initY;
		size = initSize;
		color = c;
	}

	public void moveX(int dx){
		x = (x + dx) % 1024;
	}

	public void moveY(int dy){
		y = (y + dy) % 576;
	}

	public void changeSize(int ds){
		size += ds;
	}

	public void setX(int x){
		this.x = x;
	}

	public void setY(int y){
		this.y = y;
	}

	public void setSize(int s){
		size = s;
	}

	public char getLetter(){
		return letter;
	}

	public int getX(){
		return x;
	}

	public int getY(){
		return y;
	}

	public int getSize(){
		return size;
	}

	public Color getColor(){
		return color;
	}

	public static void main(String[] args){
		Tile t = new Tile('A', 100, 200, 50, Color.blue);
		Tile u = new Tile('X', 69, 420, 69, Color.red);
		System.out.println(t.hashCode());
		System.out.println(u.hashCode());
	}
}
