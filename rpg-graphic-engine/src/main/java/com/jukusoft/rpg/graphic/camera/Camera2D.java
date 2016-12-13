package com.jukusoft.rpg.graphic.camera;

import com.jukusoft.rpg.core.math.Vector2f;

/**
 * Created by Justin on 06.12.2016.
 */
public interface Camera2D extends ReadonlyCamera {

    /**
    * set 2D position of camera
    */
    public void set2DPosition (final float x, final float y);

    public void move2DPosition (final float x, final float y);

    /**
    * get current camera position
    */
    public Vector2f getPosition ();

    /**
    * set zoom level
    */
    public void setZoom (float zoomLevel);

    /**
    * get zoom level
    */
    public float getZoom ();

    /**
    * check, if camera position, rotation or something else was changed
    */
    public boolean hasChanged ();

    /**
    * reset changed state flag
    */
    public void resetChangedState ();

}
