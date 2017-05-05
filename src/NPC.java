/**
 * This class represents any non-playable character (NPC) that can exist in the game
 */
public abstract class NPC extends Character {
    private int pass_distance;

    public NPC(int pass_distance) {
        this.pass_distance = pass_distance;
    }

    public abstract void onTalk();

    public abstract void onPass();
}
