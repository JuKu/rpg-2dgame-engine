package com.jukusoft.rpg.core.color;

/**
 * an thread safe class for color representation
 *
 * Created by Justin on 23.08.2016.
 */
public class Color implements Cloneable {

    /**
    * RGBA color values red, green, blue, alpha
    */
    protected volatile float r = 0;
    protected volatile float g = 0;
    protected volatile float b = 0;
    protected volatile float a = 0;

    /**
     * default constructor
     *
     * @param r red
     * @param g green
     * @param b blue
     * @param a alpha
     */
    public Color (float r, float g, float b, float a) {
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
    public Color (float r, float g, float b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    /**
    * default constructor
    */
    public Color () {
        //
    }

    public float getRed () {
        return r;
    }

    public float getGreen () {
        return g;
    }

    public float getBlue () {
        return b;
    }

    public float getAalpha () {
        return a;
    }

    public void setRed (float r) {
        this.r = r;
    }

    public void setGreen (float g) {
        this.g = g;
    }

    public void setBlue (float b) {
        this.b = b;
    }

    public void setAlpha (float a) {
        this.a = a;
    }

    @Override
    public Color clone () {
        //return new instance of color with same values
        return new Color(this.r, this.g, this.b, this.a);
    }

}
