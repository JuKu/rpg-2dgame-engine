package com.jukusoft.rpg.graphic.opengl.font;

import java.awt.*;

/**
 * Created by Justin on 29.11.2016.
 */
public class FontAttr {

    protected final Font font;
    protected final String charsetName;
    protected final Color color;

    public FontAttr (final Font font, final String charsetName, final Color color) {
        this.font = font;
        this.charsetName = charsetName;
        this.color = color;
    }

    public Font getFont () {
        return this.font;
    }

    public String getCharsetName () {
        return this.charsetName;
    }

    public Color getColor () {
        return this.color;
    }

}
