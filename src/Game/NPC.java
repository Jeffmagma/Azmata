package Game;

/**
 * This class represents any non-playable character (Game.NPC) that can exist in the game
 */
public abstract class NPC extends Character {
    /** The distance that you have to be from the character for some interaction to occur */
    private int pass_distance;

    public NPC(int pass_distance) {
        this.pass_distance = pass_distance;
    }

    public abstract void onTalk();

    public abstract void onPass();
}
