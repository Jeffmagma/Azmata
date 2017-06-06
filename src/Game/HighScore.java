package Game;

import java.io.Serializable;

/**
 * Created by richard on 06/06/17.
 */
public class HighScore{
    public String name;
    public long score;

    public HighScore(String n, long s){
        name = n;
        score = s;
    }

    @Override
    public String toString(){
        return "" + name + ": " + score;
    }
}
