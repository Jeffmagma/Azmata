package util;
import java.awt.Graphics;
import java.awt.FontMetrics;

public class Text{
	public static void drawCenteredString(Graphics g, String text, int x, int y, int maxx, int maxy){
		FontMetrics metrics = g.getFontMetrics(g.getFont());
		int cx = x + (maxx - metrics.stringWidth(text)) / 2;
		int cy = y + ((maxy - metrics.getHeight()) / 2) + metrics.getAscent();
		g.drawString(text, x, y);
	}
}
