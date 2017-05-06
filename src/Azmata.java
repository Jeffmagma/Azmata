import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Azmata {
    private static final int BLOCK_SIZE = 32;
    private static final int SCALE_X = 16;
    private static final int SCALE_Y = 9;
    private static final int SCALE = 2;
    private static JFrame frame;
    private static JPanel panel;
    private static Graphics g;

    public static void main(String[] args) {
        frame = new JFrame();
        panel = new JPanel();
        frame.getContentPane().setSize(BLOCK_SIZE * SCALE_X * SCALE, BLOCK_SIZE * SCALE_Y * SCALE);
        frame.add(panel);
        frame.setBackground(Color.WHITE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
        drawLogo();
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                System.out.println("lol");
            }
        });
    }

    private static void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void drawLogo() {
        g = panel.getGraphics();
        for (int i = 0; i < 100; i++) {
            g.drawRect(100, 100, i, i);
            sleep(10);
        }
    }
}