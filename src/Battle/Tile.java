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
		x += dx;
	}

	public void moveY(int dy){
		y += dy;
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

	public int hashCode(){
		return ((((int) letter - 65) * 65 + x) * 1024 + y) * 200 % 1000000007 + (color.getRGB() % 1000000007);
	}
}
