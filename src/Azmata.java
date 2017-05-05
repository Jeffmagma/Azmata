import javax.swing.*;
import java.awt.*;

public class Azmata {
    private static final int BLOCK_SIZE = 32;
    private static final int SCALE_X = 16;
    private static final int SCALE_Y = 9;
    private static final int SCALE = 3;
    private static JFrame frame;
    private static JPanel panel;
    private static int tick = -1;

    public static void main(String[] args) {
        frame = new JFrame();
        panel = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                g.setColor(Color.RED);
                g.setFont(new Font("Comic Sans MS", Font.PLAIN, 300));
                g.drawString("D", 300, 300);
                g.setColor(Color.WHITE);
                g.fillArc(225, 60, 300, 300, 180, 360 - tick);
            }
        };
        panel.setPreferredSize(new Dimension(BLOCK_SIZE * SCALE_X * SCALE, BLOCK_SIZE * SCALE_Y * SCALE));
        frame.add(panel);
        frame.setBackground(Color.WHITE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
        while (++tick <= 360) {
            panel.repaint();
            if (tick < 300)
                sleep(10);
            else sleep(2);
        }
    }

    static void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}