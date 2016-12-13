package com.jukusoft.rpg.graphic.lighting;

import com.jukusoft.rpg.core.math.Vector3f;

/**
 * directional light, like in lwjgl exampels
 *
 * Created by Justin on 13.12.2016.
 */
public class DirectionalLight {

    /**
    * from lwjgl examples
    */

    protected Vector3f color = new Vector3f(0, 0, 0);

    private Vector3f direction = new Vector3f(0, 0, 0);

    private volatile float intensity;

    public DirectionalLight(final Vector3f color, final Vector3f direction, final float intensity) {
        this.color = color;
        this.direction = direction;
        this.intensity = intensity;
    }

    public DirectionalLight(DirectionalLight light) {
        this(new Vector3f(light.getColor()), new Vector3f(light.getDirection()), light.getIntensity());
    }

    /**
    * get color of directional light
    */
    public Vector3f getColor() {
        return color;
    }

    /**
    * set color of directional light
    */
    public void setColor(Vector3f color) {
        this.color = color;
    }

    /**
    * get direction of directional light
    */
    public Vector3f getDirection() {
        return direction;
    }

    /**
    * set direction of directictional light
    */
    public void setDirection(Vector3f direction) {
        this.direction = direction;
    }

    public float getIntensity() {
        return intensity;
    }

    public void setIntensity(float intensity) {
        this.intensity = intensity;
    }

}
