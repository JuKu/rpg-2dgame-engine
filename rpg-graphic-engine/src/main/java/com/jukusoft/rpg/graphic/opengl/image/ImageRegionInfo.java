package com.jukusoft.rpg.graphic.opengl.image;

/**
 * Created by Justin on 10.12.2016.
 */
public class ImageRegionInfo {

    protected volatile float startX = 0f;
    protected volatile float startY = 0f;
    protected volatile float width = 0f;
    protected volatile float height = 0f;

    public ImageRegionInfo(final float startX, final float startY, final float width, final float height) {
        this.startX = startX;
        this.startY = startY;
        this.width = width;
        this.height = height;
    }

    public void set (final float startX, final float startY, final float width, final float height) {
        this.startX = startX;
        this.startY = startY;
        this.width = width;
        this.height = height;
    }

    public float getStartX () {
        return this.startX;
    }

    public void setStartX (final float startX) {
        this.startX = startX;
    }

    public float getStartY () {
        return this.startY;
    }

    public void setStartY (final float startY) {
        this.startY = startY;
    }

    public float getEndX () {
        return this.startX + this.width;
    }

    public float getEndY () {
        return this.startY = this.height;
    }

    public float getWidth () {
        return this.width;
    }

    public void setWidth (final float width) {
        this.width = width;
    }

    public float getHeight () {
        return this.height;
    }

    public void setHeight (final float height) {
        this.height = height;
    }

}
