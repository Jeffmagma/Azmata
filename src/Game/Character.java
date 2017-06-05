package Game;

import Main.Azmata;

import java.io.Serializable;

/**
 * This class represents any character in the game
 *
 * @author Jeffrey Gao
 */
public abstract class Character implements Serializable {
    /** The spritesheet that is used with the character */
    SpriteSheet sprites;
    /** The direction the character is currently facing */
    Direction direction;

    /**
     * Say something at the bottom
     *
     * @param messages The messages to say
     */
    void say(String... messages) {
        Azmata.graphics.fillRoundRect(0, 500, Azmata.SCREEN_WIDTH, 76, 20, 20);
        Azmata.graphics.drawImage(sprites.face(), 10, 500, null);
        for (int i = 0; i < messages.length; i++) {
            Azmata.graphics.drawString(messages[i], 10, 500 + i * 10);
        }
    }
}