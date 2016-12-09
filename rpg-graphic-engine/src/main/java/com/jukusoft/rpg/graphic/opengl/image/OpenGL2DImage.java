package com.jukusoft.rpg.graphic.opengl.image;

import com.jukusoft.rpg.core.asset.image.Image;
import com.jukusoft.rpg.core.math.Vector3f;
import com.jukusoft.rpg.graphic.opengl.mesh.DrawableObject;
import com.jukusoft.rpg.graphic.opengl.mesh.Mesh;
import com.jukusoft.rpg.graphic.opengl.texture.OpenGL2DTexture;

/**
 * Created by Justin on 09.12.2016.
 */
public class OpenGL2DImage extends DrawableObject {

    protected Image image = null;

    protected float width = 0;
    protected float height = 0;

    protected OpenGL2DTexture texture = null;

    /**
    * default constructor
     *
     * @param x x coordinate of image
     * @param y y coordinate of image
     * @param image instance of image
    */
    public OpenGL2DImage(final float x, final float y, final float width, final float height, Image image) {
        //create mesh
        Mesh mesh = buildMesh(x, y, width, height, image);

        //set default color #FFFFFF
        this.getMesh().getMaterial().setColor(new Vector3f(1f, 1f, 1f));

        //set position
        this.setPosition(x, y, 0);
    }

    /**
     * default constructor
     *
     * @param x x coordinate of image
     * @param y y coordinate of image
     * @param image instance of image
     */
    public OpenGL2DImage(final float x, final float y, Image image) {
        this(x, y, image.getWidth(), image.getHeight(), image);
    }

    protected Mesh buildMesh (final float x, final float y, final float width, final float height, Image image) {
        throw new UnsupportedOperationException("method isnt implemented yet.");
    }

    public void setImage (final float x, final float y, final float width, final float height, final Image image) {
        //create and upload new texture
        this.texture = OpenGL2DTexture.createAndUpload(image);

        //create new mesh
        Mesh mesh = buildMesh(x, y, width, height, image);

        //get old mesh
        Mesh oldMesh = getMesh();

        //set new mesh
        setMesh(mesh);

        //set default color #FFFFFF
        this.getMesh().getMaterial().setColor(new Vector3f(1f, 1f, 1f));

        //delete old mesh from gpu
        oldMesh.cleanUp();

        //remove reference, so Garbage Collector can cleanUp
        oldMesh = null;

        //get old texture
        OpenGL2DTexture oldTexture = this.texture;

        if (oldTexture != null) {
            //delete old texture from gpu
            oldTexture.delete();

            oldTexture = null;
        }
    }

    public void setImage (final Image image) {
        this.setImage(0, 0, image.getWidth(), image.getHeight(), image);
    }

    public float getX () {
        return this.getPosition().getX();
    }

    public float getY () {
        return this.getPosition().getY();
    }

}
