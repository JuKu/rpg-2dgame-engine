package com.jukusoft.rpg.graphic.opengl.mesh;

import com.jukusoft.rpg.core.math.Vector3f;
import com.jukusoft.rpg.graphic.renderer.BasicRenderable;
import com.jukusoft.rpg.graphic.renderer.Renderable;
import com.jukusoft.rpg.graphic.utils.MeshUtils;

/**
 * Created by Justin on 30.11.2016.
 */
public class DrawableObject extends BasicRenderable implements Renderable {

    /**
    * default constructor
    */
    public DrawableObject(Mesh mesh1) {
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

    public void setPosition (final float x, final float y) {
        this.position.set(x, y, 0);
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

    @Override
    public boolean redrawWithSameParams() {
        return false;
    }

    public Mesh getMesh () {
        return this.mesh;
    }

    public void setMesh (Mesh mesh) {
        this.mesh = mesh;
    }

    @Override
    public String toString () {
        return "Drawable Object class: " + this.getClass().getName() + ", position: " + this.position.toString() + ", rotation: " + this.rotation.toString() + ", scale: " + this.scale + ", meshID: " + this.meshID + ", mesh: " + this.mesh.toString();
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

    @Override
    public void render() {
        getMesh().render();
    }

    @Override
    public Material getMaterial() {
        return getMesh().getMaterial();
    }
}
