package com.jukusoft.rpg.graphic.opengl.font;

import org.apache.commons.lang3.builder.HashCodeBuilder;

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

    @Override
    public boolean equals (Object obj) {
        if (!(obj instanceof FontAttr)) {
            throw new IllegalStateException("only FontAttr objects can be compared for equals.");
        }

        return this.equals((FontAttr) obj);
    }

    public boolean equals (FontAttr fontAttr) {
        return fontAttr.hashCode() == this.hashCode();
    }

    public int hashCode () {
        //generate hashCode and return hashCode
        return new HashCodeBuilder(17, 31) //two randomly chosen prime numbers
                        .append(font.getFamily())
                        .append(font.getFontName())
                        .append(font.getName())
                        .append(font.getStyle())
                        .append(font.getSize())
                        .append(charsetName)
                        .append(color.getRed())
                        .append(color.getGreen())
                        .append(color.getBlue())
                        .append(color.getAlpha())
                        .toHashCode();
    }

    @Override
    public String toString () {
        return font.getFamily() + "-" + font.getFontName() + "-" + font.getName() + "-" + font.getStyle() + "-" + font.getSize() + "-" + charsetName + "-" + color.getRed() + "-" + color.getGreen() + "-" + color.getBlue() + "-" + color.getAlpha();
    }

}
