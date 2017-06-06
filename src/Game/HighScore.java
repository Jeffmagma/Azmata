package Game;

/**
 * A class to store the high scores of a player
 * Created by richard on 06/06/17.
 *
 * @author Richard Yi
 */
public class HighScore {
    /** The name of the player */
    public String name;
    /** The score that they obtained */
    public long score;

    /**
     * constructs a high score
     *
     * @param n The name of the player
     * @param s The score of the player
     */
    public HighScore(String n, long s) {
        name = n;
        score = s;
    }

    @Override
    public String toString() {
        return "" + name + ": " + score;
    }
}
