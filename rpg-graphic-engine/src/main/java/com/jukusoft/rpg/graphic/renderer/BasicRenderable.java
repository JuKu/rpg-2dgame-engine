package com.jukusoft.rpg.graphic.renderer;

import com.jukusoft.rpg.core.math.Vector3f;
import com.jukusoft.rpg.graphic.opengl.mesh.Material;
import com.jukusoft.rpg.graphic.opengl.mesh.Mesh;
import com.jukusoft.rpg.graphic.utils.MeshUtils;

/**
 * Created by Justin on 13.12.2016.
 */
public abstract class BasicRenderable implements Renderable {

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

    @Override
    public Material getMaterial() {
        if (mesh == null) {
            return null;
        } else {
            return mesh.getMaterial();
        }
    }

    @Override
    public Vector3f getPosition() {
        return this.position;
    }

    @Override
    public Vector3f getRotation() {
        return this.rotation;
    }

    @Override
    public float getScale() {
        return this.scale;
    }

    @Override
    public long getMeshID() {
        return this.meshID;
    }

    @Override
    public boolean redrawWithSameParams() {
        return true;
    }

}
