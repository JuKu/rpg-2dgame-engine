package com.jukusoft.rpg.graphic.opengl.mesh;

import com.jukusoft.rpg.core.math.Vector3f;
import com.jukusoft.rpg.graphic.utils.MeshUtils;

/**
 * Created by Justin on 30.11.2016.
 */
public class DrawableObject {

    /**
    * position of mesh object
    */
    protected Vector3f position = new Vector3f(0, 0, 0);

    /**
    * scale factor for mesh
    */
    protected volatile float scale = 1;

    /**
    * rotation vector
    */
    protected Vector3f rotation = new Vector3f(0, 0, 0);

    protected Mesh mesh = null;

    /**
    * local unique meshID
    */
    protected final long meshID = MeshUtils.generateID();

    /**
    * default constructor
    */
    public DrawableObject(Mesh mesh1) {
        this.position = null;
        this.mesh = mesh1;
    }

    /**
     * default constructor
     */
    public DrawableObject() {
        //
    }

    public void setPosition (final float x, final float y, final float z) {
        this.position.set(x, y, z);
    }

    public void setScale (final float scale) {
        this.scale = scale;
    }

    public void setRotation (final float x, final float y, final float z) {
        this.rotation.set(x, y, z);
    }

    public Vector3f getPosition () {
        return this.position;
    }

    public float getScale () {
        return this.scale;
    }

    public Vector3f getRotation () {
        return this.rotation;
    }

    public long getMeshID () {
        return this.meshID;
    }

    public void cleanUp () {
        if (mesh != null) {
            this.mesh.cleanUp();
        }
    }

    @Override
    public final void finalize () {
        this.cleanUp();
    }

}
