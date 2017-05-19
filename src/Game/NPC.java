package Game;

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

    /**
     * What happens when you walk up to the NPC and press enter
     */
    public abstract void onTalk();

    public abstract void onPass();
}
