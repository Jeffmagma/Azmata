package Game;

import Battle.Battle;
import Main.Azmata;

import java.awt.*;

/**
 * This class represents any non-playable character (NPC) that can exist in the game
 *
 * @author Jeffrey Gao
 */
public abstract class NPC extends Character {
    /** Where the NPC is on the map */
    Point position;

    /**
     * Creates an NPC with a specified pass distance
     *
     * @param position Which tile on the map the NPC is on
     * @param sprites  The sprites to use with the NPC
     */
    public NPC(Point position, SpriteSheet sprites) {
        direction = Direction.values()[(int) (Math.random() * 4)];
        this.sprites = sprites;
        this.position = position;
    }

    /**
     * What happens when you walk up to the NPC and press enter
     */
    public abstract void onTalk();

    /**
     * Start a battle with an NPC, saving before and after
     *
     * @return If you won the battle
     */
    public boolean battle() {
        Game.save();
        Azmata.frame.remove(Azmata.current_panel);
        Battle battle = new Battle(10, "LUL", "SeemsGood");
        Azmata.frame.add(battle);
        battle.start();
        Azmata.frame.remove(battle);
        Azmata.frame.add(Azmata.current_panel);
        Game.save();
        return battle.won();
    }

    /**
     * Draw the NPC based in its position
     */
    public void draw() {
        Point draw_point = Game.getRelativePosition(position);
        Azmata.graphics.drawImage(sprites.sprites()[direction.ordinal()][SpriteSheet.STANDING], draw_point.x, draw_point.y, null);
    }
}