package Menu;

import javax.swing.*;

public class OptionsMenu extends JPanel {
    /**
     * A enum to represent the current menu option
     */
    public enum MenuOption {
        /** The new game option */
        KEY_CONFIG, VOLUME, BACK;
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
