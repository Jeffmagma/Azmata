package Menu;

import Main.Azmata;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * A splashscreen that shows the name and logo for the game
 */
public class SplashScreen extends JPanel {
    /** The current alpha of the animation */
    private char animation_alpha;
    /** If the animation is in the fading stage */
    private boolean animation_fading;
    /** The current state of the animation */
    private AnimationState animation_state;
    /** The image used for the company logo */
    private Image dnp_image;
    /** The image used for the game name */
    private Image name_image;

    /**
     * Constructs a splashscreen, and sets default values
     */
    public SplashScreen() {
        animation_alpha = 0;
        animation_fading = false;
        animation_state = AnimationState.LOGO;
        // Gets the image used for the game name in the intro
        name_image = Azmata.imageFromFile("Main/azmata.png");
        // Gets the image used for the company name in the intro
        dnp_image = Azmata.imageFromFile("Main/dnpnew.png");

        // Set the spacebar to move to the next splashscreen
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("released SPACE"), "next_state");
        getActionMap().put("next_state", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nextState();
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void paintComponent(Graphics g) {
        Azmata.graphics = (Graphics2D) g;

        if (animation_fading) animation_alpha--;
        else animation_alpha++;

        if (animation_alpha == 255) animation_fading = true;
        if (animation_alpha == 0) nextState();

        switch (animation_state) {
            case LOGO:
                drawImage(dnp_image, animation_alpha);
                break;
            case NAME:
                drawImage(name_image, animation_alpha);
                break;
            case MAIN_MENU:
                break;
        }
    }

    /**
     * Draws an image at a specific alpha, in the middle of the screen, with a black background
     *
     * @param image The image to draw
     * @param alpha The alpha to draw it at
     */
    private void drawImage(Image image, char alpha) {
        Azmata.graphics.setColor(Color.BLACK);
        Azmata.graphics.fillRect(0, 0, getWidth(), getHeight());
        Azmata.graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha / 255.f));
        Azmata.graphics.drawImage(image, (getWidth() - image.getWidth(null)) / 2, (getHeight() - image.getHeight(null)) / 2, null);
    }

    /**
     * Plays the animation of the splashscreen
     */
    public void play() {
        revalidate();
        // Start the animation timer for the logo
        Timer animation_timer = new Timer(5, e -> repaint());
        animation_timer.start();
        while (animation_state == AnimationState.LOGO) ;
        animation_timer = new Timer(8, e -> repaint());
        animation_timer.start();
        while (animation_state == AnimationState.NAME) ;
    }

    /**
     * Shifts the animation to the next state, used when spacebar is pressed or one stage of the animation is over
     */
    private void nextState() {
        animation_state = animation_state.next();
        animation_alpha = 0;
        animation_fading = false;
    }

    /**
     * The current state of the animation
     */
    enum AnimationState {
        /** When displaying the company logo */
        LOGO,
        /** When displaying the name of the game */
        NAME,
        /** While in the main menu */
        MAIN_MENU;

        /**
         * Moves the state to the next one
         *
         * @return The next state
         */
        public AnimationState next() {
            return values()[ordinal() + 1 < values().length ? ordinal() + 1 : ordinal()];
        }
    }
}
