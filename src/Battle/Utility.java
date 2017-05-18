import java.awt.Graphics;
import java.awt.FontMetrics;

public class Utility{
	public static void drawCenteredString(Graphics g, String text, int x, int y){
		FontMetrics metrics = g.getFontMetrics(g.getFont());
		//int cx = x + (maxx - metrics.stringWidth(text)) / 2;
		int cx = x - metrics.stringWidth(text) / 2;
		//int cy = y + ((maxy - metrics.getHeight()) / 2) + metrics.getAscent();
		int cy = y + (metrics.getAscent(text) + metrics.getDescent(text)) / 2;
		g.drawString(text, cx, cy);
	}
}
