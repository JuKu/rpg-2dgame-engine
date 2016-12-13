package com.jukusoft.rpg.graphic.lighting;

import com.jukusoft.rpg.core.math.Vector3f;

/**
 * Created by Justin on 13.12.2016.
 */
public class SpotLight {

    /**
    * from LWJGL examples
    */

    protected PointLight pointLight = null;

    protected Vector3f coneDirection = null;

    protected float cutOff = 0;

    public SpotLight(PointLight pointLight, Vector3f coneDirection, float cutOffAngle) {
        this.pointLight = pointLight;
        this.coneDirection = coneDirection;
        setCutOffAngle(cutOffAngle);
    }

    public SpotLight(SpotLight spotLight) {
        this(new PointLight(spotLight.getPointLight()),
                new Vector3f(spotLight.getConeDirection()),
                spotLight.getCutOff());
    }

    public PointLight getPointLight() {
        return pointLight;
    }

    public void setPointLight(PointLight pointLight) {
        this.pointLight = pointLight;
    }

    public Vector3f getConeDirection() {
        return coneDirection;
    }

    public void setConeDirection(Vector3f coneDirection) {
        this.coneDirection = coneDirection;
    }

    public float getCutOff() {
        return cutOff;
    }

    public void setCutOff(float cutOff) {
        this.cutOff = cutOff;
    }

    public final void setCutOffAngle(float cutOffAngle) {
        this.setCutOff((float)Math.cos(Math.toRadians(cutOffAngle)));
    }

}
