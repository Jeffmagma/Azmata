import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Battle extends JPanel{
	boolean start;
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

		g.setColor(Color.white);
		g.fillRect(0, 0, 1024, 576);
		g.drawRect(x - 1, y, 100, 100);
		g.setColor(Color.black);
		g.drawRect(x, y, 100, 100);

		start = false;
	}

	public Battle(){
		x = 0; y = 0;
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				start = true;

				t = new Timer(1, animate);
				t.start();
			}
		});
	}

	public static void main(String[] args){
		JFrame f = new JFrame();
		f.setSize(1024, 576);
		f.setDefaultCloseOperation(2);
		f.add(new Battle());
		f.setVisible(true);
	}
}
