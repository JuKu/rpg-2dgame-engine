package com.jukusoft.rpg.game.engine.spritebatcher;

import java.awt.*;

/**
 * Created by Justin on 13.12.2016.
 */
public interface SpriteBatcher {

    /**
    * draw text
    */
    public void drawText (String text, final float x, final float y, Font font, Color color);

    public void drawText (String text, final float x, final float y, Color color);

    public void drawText (String text, final float x, final float y);

}
