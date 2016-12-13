package com.jukusoft.rpg.game.engine.spritebatcher;

import com.jukusoft.rpg.graphic.animation.BasicAnimation;
import com.jukusoft.rpg.graphic.opengl.image.OpenGL2DImage;

import java.awt.*;

/**
 * Created by Justin on 13.12.2016.
 */
public interface SpriteBatcher {

    public void begin ();

    public void end ();

    /**
    * draw text
    */
    public void drawText (String text, final float x, final float y, Font font, Color color);

    /**
     * draw text
     */
    public void drawText (String text, final float x, final float y, Color color);

    /**
     * draw text
     */
    public void drawText (String text, final float x, final float y);

    public void drawAnimation (BasicAnimation animation, final float x, final float y);

    public void drawImage (OpenGL2DImage image, final float x, final float y);

}
