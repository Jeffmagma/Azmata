import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Azmata {
    private static final int BLOCK_SIZE = 32;
    private static final int SCALE_X = 16;
    private static final int SCALE_Y = 9;
    private static final int SCALE = 2;
    private static JFrame frame;
    private static JPanel panel;
    private static Graphics g;
    private static State current_state;

    private static void initialize() {
        // Construct the JFrame
        frame = new JFrame();
        // Construct a new JPanel
        panel = new JPanel();
        // Set the spacebar to move to the next splashscreen
        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("released SPACE"), "next_state");
        panel.getActionMap().put("next_state", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("space pressed");
                current_state = current_state.next();
            }
        });
        // Set the size of the JFrame to be a 16:9 screen, and make sure it can hold 32x32 grid blocks evenly
        frame.getContentPane().setPreferredSize(new Dimension(BLOCK_SIZE * SCALE_X * SCALE, BLOCK_SIZE * SCALE_Y * SCALE));
        // Add the panel which will contains the content
        frame.add(panel);
        // Make sure the frame is the right size
        frame.pack();
        // Center the frame
        frame.setLocationRelativeTo(null);
        // Set the JFrame to exit on close
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        // Stop users from resizing the JFrame (we need to preserve the scale)
        frame.setResizable(false);
        // Show the frame
        frame.setVisible(true);
        // Get the graphics that will be used to draw
        g = panel.getGraphics();
    }

    public static void main(String[] args) {
        initialize();
        current_state = State.NAME;
        drawLogo();
        drawName();
        //drawMainMenu();
    }

    /**
     * Sleep for a certain amount of time
     *
     * @param time The amount of time to sleep
     */
    private static void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Draws the name of the game (Azmata) on the screen
     */
    private static void drawName() {
        /*
        Font title_font = null;
        try {
            title_font = Font.createFont(Font.TRUETYPE_FONT, new File("name.ttf")).deriveFont(30);
        } catch (FontFormatException | IOException e) {
            System.err.println("There was an error retrieving the font file!");
            System.exit(0);
        }*/
        Font title_font = new Font("Comic Sans MS", Font.PLAIN, 30);
        g.setFont(title_font);
        for (int i = 1; i < 512 && current_state == State.NAME; i++) {
            int alpha = Math.abs(i - 256);
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, panel.getWidth(), panel.getHeight());
            g.setColor(new Color(255, 0, 0, alpha));
            g.drawString("Azmata", 300, 300);
            panel.revalidate();
            sleep(10);
        }
    }

    /**
     * Draws the DNP Logo on the screen;
     */
    private static void drawLogo() {
        if (current_state == State.LOGO) {
            g.setColor(Color.CYAN);
            g.drawString("lol", 400, 400);
        }
    }

    /**
     * Draws the main menu
     */
    private static void drawMainMenu() {
        panel.setLayout(null);
        JLabel start_image = new JLabel(new ImageIcon("start.png"));
        JLabel title_image = new JLabel(new ImageIcon("title.png"));
        panel.add(start_image);
        start_image.setLocation(300, 300);
    }

    enum State {
        LOGO,
        NAME,
        MAIN_MENU;

        public State next() {
            return values()[(this.ordinal() + 1) % values().length];
        }
    }
}
