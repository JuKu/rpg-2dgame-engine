package com.jukusoft.rpg.graphic.lighting;

import com.jukusoft.rpg.core.math.Vector3f;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Justin on 13.12.2016.
 */
public class LightingScene {

    public static final int MAX_NUMBER_OF_POINT_LIGHTS = 15;

    public static final int MAX_NUMBER_OF_SPOT_LIGHTS = 15;

    /**
    * ambiente light
    */
    protected Vector3f ambientLight = new Vector3f(0.3f, 0.3f, 0.3f);

    /**
    * list with all point lights
    */
    protected List<PointLight> pointLightList = new ArrayList<>();

    /**
    * point light array cache
    */
    protected PointLight[] pointLightCache = new PointLight[0];

    /**
    * list with all spot lights
    */
    protected List<SpotLight> spotLightList = new ArrayList<>();

    protected SpotLight[] spotLightsCache = new SpotLight[0];

    private DirectionalLight directionalLight = null;

    public Vector3f getAmbientLight () {
        return this.ambientLight;
    }

    public void setAmbientLight (Vector3f ambientLight) {
        this.ambientLight = ambientLight;
    }

    public void addPointLight (PointLight pointLight) {
        this.pointLightList.add(pointLight);
        this.pointLightCache = (PointLight[]) this.pointLightList.toArray();
    }

    public void removePointLight (PointLight pointLight) {
        this.pointLightList.remove(pointLight);
        this.pointLightCache = (PointLight[]) this.pointLightList.toArray();
    }

    public void clearPointLights () {
        this.pointLightList.clear();
        this.pointLightCache = new PointLight[0];
    }

    public PointLight[] getPointLights () {
        return this.pointLightCache;
    }

    public void addSpotLight (SpotLight spotLight) {
        this.spotLightList.add(spotLight);
        this.spotLightsCache = (SpotLight[]) this.spotLightList.toArray();
    }

    public void removeSpotLight (SpotLight spotLight) {
        this.spotLightList.remove(spotLight);
        this.spotLightsCache = (SpotLight[]) this.spotLightList.toArray();
    }

    public void clearSpotLights () {
        this.spotLightList.clear();
        this.spotLightsCache = new SpotLight[0];
    }

    public SpotLight[] getSpotLights () {
        return this.spotLightsCache;
    }

    public DirectionalLight getDirectionalLight() {
        return this.directionalLight;
    }

    public void setDirectionalLight(DirectionalLight directionalLight) {
        this.directionalLight = directionalLight;
    }

}
