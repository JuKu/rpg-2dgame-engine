package com.jukusoft.rpg.graphic.lighting.impl;

import com.jukusoft.rpg.core.math.Vector3f;
import com.jukusoft.rpg.graphic.lighting.Light2D;
import com.jukusoft.rpg.graphic.opengl.image.OpenGL2DImage;
import com.jukusoft.rpg.graphic.opengl.mesh.Material;

/**
 * Created by Justin on 17.12.2016.
 */
public class CircleLight2D implements Light2D {

    protected OpenGL2DImage image = null;

    protected volatile float x = 0;
    protected volatile float y = 0;

    public CircleLight2D (final float x, final float y, OpenGL2DImage image) {
        this.x = x;
        this.y = y;
        this.image = image;
    }

    @Override
    public float getX() {
        return this.x;
    }

    @Override
    public float getY() {
        return this.y;
    }

    @Override
    public OpenGL2DImage getLightTexture () {
        return this.image;
    }

    @Override
    public boolean isLightOscillateEnabled () {
        return false;
    }

    @Override
    public Vector3f getPosition() {
        return this.image.getPosition();
    }

    @Override
    public Vector3f getRotation() {
        return this.image.getRotation();
    }

    @Override
    public float getScale() {
        return this.image.getScale();
    }

    @Override
    public Material getMaterial() {
        return this.image.getMaterial();
    }

    @Override
    public void renderLight (float windowWidth, float windowHeight) {
        //set position of light
        this.image.setPosition(this.x, this.y);

        this.image.render();
    }

}
