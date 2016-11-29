package com.jukusoft.rpg.graphic.opengl.font;

/**
 * Information for each character, see LWJGL examples
 *
 * Created by Justin on 29.11.2016.
 */
public class CharacterInfo {

    protected final int startX;
    protected final int width;

    public CharacterInfo (final int startX, final int width) {
        this.startX = startX;
        this.width = width;
    }

    public int getStartX () {
        return this.startX;
    }

    public int getWidth () {
        return this.width;
    }

}
