package com.jukusoft.rpg.graphic.opengl.mesh;

import com.jukusoft.rpg.core.math.Vector3f;
import com.jukusoft.rpg.core.math.Vector4f;
import com.jukusoft.rpg.graphic.opengl.texture.OpenGL2DTexture;

/**
 * Created by Justin on 30.11.2016.
 */
public class Material {

    private static final Vector4f DEFAULT_COLOUR = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);

    protected Vector4f color = DEFAULT_COLOUR;
    private float reflectance = 0;
    protected OpenGL2DTexture texture = null;

    public Material (OpenGL2DTexture texture) {
        this.texture = texture;
    }

    public Material () {
        this.color = DEFAULT_COLOUR;
        this.reflectance = 0;
    }

    public Vector4f getColor () {
        return this.color;
    }

    public void setColor (Vector4f color) {
        this.color = color;
    }

    public void setColor (Vector3f color) {
        this.color = new Vector4f(color.getX(), color.getY(), color.getZ(), 1f);
    }

    public float getReflectance() {
        return reflectance;
    }

    public void setReflectance(float reflectance) {
        this.reflectance = reflectance;
    }

    public OpenGL2DTexture getTexture () {
        return this.texture;
    }

    public void setTexture (OpenGL2DTexture texture) {
        this.texture = texture;
    }

    public boolean isTextured() {
        return this.texture != null;
    }

}
