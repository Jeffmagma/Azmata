package Game;

import Main.Azmata;

/**
 * This class represents any non-playable character (Game.NPC) that can exist in the game
 */
public abstract class NPC extends Character {
    /** The distance that you have to be from the character for some interaction to occur */
    private int pass_distance;

    /**
     * Creates an NPC with a specified pass distance
     *
     * @param pass_distance How far you have to be away from the face of an NPC for an interaction to occur
     */
    public NPC(int pass_distance) {
        this.pass_distance = pass_distance;
    }

    static void say(String... messages) {
        Azmata.graphics.fillRoundRect(0, 500, Azmata.SCREEN_WIDTH, 76, 20, 20);
        for (int i = 0; i < messages.length; i++) {
            Azmata.graphics.drawString(messages[i], 10, 500 + i * 10);
        }
    }

    /**
     * What happens when you walk up to the NPC and press enter
     */
    public abstract void onTalk();

    public abstract void onPass();
}
