package com.jukusoft.rpg.graphic.camera;

import com.jukusoft.rpg.core.math.Vector2f;
import com.jukusoft.rpg.core.math.Vector3f;

/**
 * Created by Justin on 06.12.2016.
 */
public class DefaultCamera implements Camera2D, Camera3D {

    /**
    * camera position
    */
    protected Vector3f position = new Vector3f(0, 0, 0);

    /**
    * camera rotation
    */
    protected Vector3f rotation = new Vector3f(0, 0, 0);

    @Override
    public void set2DPosition(float x, float y) {
        this.position.setX(x);
        this.position.setY(y);
    }

    @Override
    public Vector2f getPosition() {
        return new Vector2f(this.position.getDirectByteBuffer());
    }

    @Override
    public void setZoom(float zoomLevel) {
        this.position.setZ(zoomLevel);
    }

    @Override
    public float getZoom() {
        return this.position.getZ();
    }

    @Override
    public void setPosition(Vector3f vector) {
        this.position = vector;
    }

    @Override
    public void setPosition(float x, float y, float z) {
        this.position.set(x, y, z);
    }

    @Override
    public void setRotation(Vector3f rotation) {
        this.rotation = rotation;
    }
}
