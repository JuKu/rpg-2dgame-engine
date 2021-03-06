package com.jukusoft.rpg.graphic.camera;

import com.jukusoft.rpg.core.math.Vector2f;
import com.jukusoft.rpg.core.math.Vector3f;

/**
 * Created by Justin on 13.12.2016.
 */
public interface ReadonlyCamera {

    public Vector3f getPosition();

    public Vector2f get2DPosition();

    public Vector3f getRotation ();

    public boolean wasChanged ();

    public void resetChangedState ();

}
