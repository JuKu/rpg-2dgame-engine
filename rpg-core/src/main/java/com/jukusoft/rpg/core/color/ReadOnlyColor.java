package com.jukusoft.rpg.core.color;

/**
 * Created by Justin on 23.08.2016.
 */
public class ReadOnlyColor extends Color {

    /**
     * default constructor
     *
     * @param r red
     * @param g green
     * @param b blue
     * @param a alpha
     */
    public ReadOnlyColor (float r, float g, float b, float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    /**
     * default constructor
     *
     * @param r red
     * @param g green
     * @param b blue
     */
    public ReadOnlyColor (float r, float g, float b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    @Override
    public void setRed (float r) {
        throw new UnsupportedOperationException("Cannot set red, instance is readonly.");
    }

    @Override
    public void setGreen (float g) {
        throw new UnsupportedOperationException("Cannot set red, instance is readonly.");
    }

    @Override
    public void setBlue (float b) {
        throw new UnsupportedOperationException("Cannot set red, instance is readonly.");
    }

    @Override
    public void setAlpha (float a) {
        throw new UnsupportedOperationException("Cannot set red, instance is readonly.");
    }

}
