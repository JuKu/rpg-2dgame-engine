package com.jukusoft.rpg.graphic.animation;

/**
 * Created by Justin on 11.12.2016.
 */
public class Frame {

    protected final float startX;
    protected final float startY;
    protected final float width;
    protected final float height;
    protected final long duration;

    public Frame (final float startX, final float startY, final float width, final float height, final long duration) {
        this.startX = startX;
        this.startY = startY;
        this.width = width;
        this.height = height;
        this.duration = duration;
    }

    public float getStartX () {
        return this.startX;
    }

    public float getStartY () {
        return this.startY;
    }

    public float getWidth () {
        return this.width;
    }

    public float getHeight () {
        return this.height;
    }

    public long getDuration () {
        return this.duration;
    }

}
