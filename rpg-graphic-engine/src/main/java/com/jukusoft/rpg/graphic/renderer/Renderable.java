package com.jukusoft.rpg.graphic.renderer;

import com.jukusoft.rpg.core.math.Vector3f;
import com.jukusoft.rpg.graphic.opengl.mesh.Material;

/**
 * interface for objects, which can be rendered
 *
 * Created by Justin on 11.12.2016.
 */
public interface Renderable {

    public void render ();

    public Material getMaterial ();

    public Vector3f getPosition ();

    public Vector3f getRotation ();

    public float getScale ();

    public long getMeshID ();

    /**
    * flag, if object should also be redrawed (overlayed), if it has the same params
    */
    public boolean redrawWithSameParams ();

}
