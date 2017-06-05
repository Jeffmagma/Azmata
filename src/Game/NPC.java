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
    private boolean battling;
    private Battle battle;

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
    public void battle() {
        battle = new Battle(10, "LUL", "SeemsGood");
        Azmata.frame.add(battle);
        battle.start();

        while(battle.isRunning());

        stopBattling();
        Azmata.frame.remove(battle);
        Azmata.frame.add(Azmata.current_panel);
    }

    /**
     * Draw the NPC based in its position
     */
    public void draw() {
        Point draw_point = Game.getRelativePosition(position);
        Azmata.graphics.drawImage(sprites.sprites()[direction.ordinal()][SpriteSheet.STANDING], draw_point.x, draw_point.y, null);
    }

    public void startBattling(){
        Game.save();
        Azmata.frame.remove(Azmata.current_panel);
        battling = true;
    }

    public void stopBattling(){
        battling = false;
        Game.save();
    }

    public boolean isBattling(){
        return battling;
    }
}