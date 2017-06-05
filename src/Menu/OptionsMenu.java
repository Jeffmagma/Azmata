package Menu;

import Main.Azmata;

import javax.swing.*;

/**
 * A class of the options inside the options menu
 *
 * @author Jeffrey Gao
 */
public class OptionsMenu extends JPanel {
    private volatile boolean quit;

    /**
     * TODO: actual stuff
     */
    public OptionsMenu() {
        Azmata.debug("OptionsMenu Constructed");
        quit = false;
    }

    /**
     * Show the options, returning when the user decides to quit
     */
    public void show() {
        revalidate();
        repaint();
        while (!quit) ;
    }

    /**
     * A enum to represent the current menu option
     */
    public enum MenuOption {
        KEY_CONFIG, VOLUME, BACK;
        /** The index of the option */
        int value = ordinal();

        /**
         * Returns the next menu option, but the current one if it's the last one
         *
         * @return The next menu option, but the current one if it's the last one
         */
        public MenuOption next() {
            return values()[value + 1 < values().length ? value + 1 : value];
        }

        /**
         * Returns the previous menu option, but the current one if it's the first one
         *
         * @return The previous menu option, but the current one if it's the first one
         */
        public MenuOption prev() {
            return values()[value > 0 ? value - 1 : value];
        }
    }
}