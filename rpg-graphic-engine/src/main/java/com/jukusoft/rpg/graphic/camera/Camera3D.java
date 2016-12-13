package com.jukusoft.rpg.graphic.camera;

import com.jukusoft.rpg.core.math.Vector3f;

/**
 * Created by Justin on 05.12.2016.
 */
public interface Camera3D extends ReadonlyCamera {

    public Vector3f getPosition ();

    /**
    * set camera position
     *
     * @param vector camera position
    */
    public void setPosition (Vector3f vector);

    /**
     * set camera position
     *
     * @param x x position of camera
     * @param y y position of camera
     * @param z z position of camera
     */
    public void setPosition (final float x, final float y, final float z);

    public void movePosition (final float x, final float y, final float z);

    /**
    * set camera rotation
     *
     * @param rotation camera rotation
    */
    public void setRotation (Vector3f rotation);

    /**
     * set camera rotation
     */
    public void setRotation (final float x, final float y, final float z);

    public void moveRotation (final float offsetX, final float offsetY, final float offsetZ);

    /**
     * check, if camera position, rotation or something else was changed
     */
    public boolean hasChanged ();

    /**
     * reset changed state flag
     */
    public void resetChangedState ();

}
