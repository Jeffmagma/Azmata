import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.FontMetrics;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.Timer;
import javax.swing.SwingUtilities;
import java.util.HashSet;
//import util.Text;
//How do you import stuff
//Improves compile speed, will be compressed later TODO
public class Battle extends JPanel{
	HashSet<Tile> tiles = new HashSet<Tile>();
	Timer t;
	long tickCount;
	int speed;

	ActionListener tick = new ActionListener(){
		public void actionPerformed(ActionEvent e){
			++tickCount;

			for(Tile tile : tiles){
				if(tickCount % speed == 0){
					tile.moveX((int) (tickCount / 50 + (int) tile.hashCode()) % 3 - 2);
					tile.moveY((int) (tickCount / 50 + (int) tile.hashCode() - 1) % 3 - 2);
				}
			}

			repaint();
			revalidate();
		}
	};

	protected void paintComponent(Graphics graphics){
		Graphics2D g = (Graphics2D) graphics;
		int x, y, size;
		String letter;

		g.setColor(Color.white);
		g.fillRect(0, 0, 1024, 576);

		for(Tile tile : tiles){
			x = tile.getX();
			y = tile.getY();
			size = tile.getSize();
			letter = "" + tile.getLetter();

			g.setColor(tile.getColor());
			g.fillOval(x, y, size, size);
			
			g.setFont(new Font("Arial", 0, size));
			g.setColor(Color.black);

			FontMetrics metrics = g.getFontMetrics(g.getFont());
			int cx = x - metrics.stringWidth(letter) / 2 + size / 2;
			int cy = y + (metrics.getAscent() - metrics.getDescent()) / 2 + size / 2;
			g.drawString(letter, cx, cy);
		}
	}

	public Battle(int speed){
		this.speed = speed;
		tickCount = 0;

		t = new Timer(20, tick);
		t.start();

		tiles.add(new Tile('A', 100, 100, 50, Color.red));
		tiles.add(new Tile('X', 150, 150, 50, Color.green));
		tiles.add(new Tile('R', 300, 300, 100, Color.blue));

		setMinimumSize(new Dimension(1024, 576));
	}

	public static void main(String[] args){
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				JFrame f = new JFrame();
				f.setSize(1024, 576);
				f.setDefaultCloseOperation(2);
				f.add(new Battle(10));
				f.setVisible(true);
			}
		});
	}
}
