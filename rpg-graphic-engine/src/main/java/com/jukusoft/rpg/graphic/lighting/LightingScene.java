package com.jukusoft.rpg.graphic.lighting;

import com.jukusoft.rpg.core.math.Vector3f;

/**
 * Created by Justin on 13.12.2016.
 */
public class LightingScene {

    public static final int MAX_NUMBER_OF_POINT_LIGHTS = 15;

    public static final int MAX_NUMBER_OF_SPOT_LIGHTS = 15;

    /**
    * ambiente light
    */
    protected Vector3f ambientLight = null;

    public Vector3f getAmbientLight () {
        return this.ambientLight;
    }

    public void setAmbientLight (Vector3f ambientLight) {
        this.ambientLight = ambientLight;
    }

}
