import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
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
	int x, y;
	Timer t;

	ActionListener animate = new ActionListener(){
		public void actionPerformed(ActionEvent e){
			x++;

			if(x >= 1000)
				t.stop();

			repaint();
			revalidate();
		}
	};

	protected void paintComponent(Graphics graphics){
		Graphics2D g = (Graphics2D) graphics;
		int x, y, size;

		g.setColor(Color.white);
		g.fillRect(0, 0, 1024, 576);

		for(Tile tile : tiles){
			x = tile.getX();
			y = tile.getY();
			size = tile.getSize();

			g.setColor(tile.getColor());
			g.fillRect(x - size / 2, y - size / 2, size, size);
			
			g.setFont(new Font("Verdana", 0, size));
			g.setColor(Color.black);
			//g.drawString("" + tile.getLetter(), x - size / 2 + size / 8, y + size / 2 - size / 8);
			Utility.drawCenteredString(g, "" + tile.getLetter(), x, y);
		}
	}

	public Battle(){
		x = 0; y = 0;

		t = new Timer(1, animate);
		t.start();

		tiles.add(new Tile('A', 100, 100, 50, Color.red));
		tiles.add(new Tile('X', 150, 150, 50, Color.green));
	}

	public static void main(String[] args){
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				JFrame f = new JFrame();
				f.setSize(1024, 576);
				f.setDefaultCloseOperation(2);
				f.add(new Battle());
				f.setVisible(true);
			}
		});
	}
}
