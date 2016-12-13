package com.jukusoft.rpg.graphic.opengl.image;

import com.jukusoft.rpg.core.asset.image.Image2D;
import com.jukusoft.rpg.core.math.Vector3f;
import com.jukusoft.rpg.core.utils.ArrayUtils;
import com.jukusoft.rpg.graphic.opengl.mesh.DrawableObject;
import com.jukusoft.rpg.graphic.opengl.mesh.Material;
import com.jukusoft.rpg.graphic.opengl.mesh.Mesh;
import com.jukusoft.rpg.graphic.opengl.texture.OpenGL2DTexture;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Justin on 09.12.2016.
 */
public class OpenGL2DImage extends DrawableObject {

    /**
    * some constants
    */
    private static final float ZPOS = 0.0f;
    private static final int VERTICES_PER_QUAD = 4;

    protected float width = 0;
    protected float height = 0;

    protected OpenGL2DTexture texture = null;

    /**
     * default constructor
     *
     * @param x x coordinate of image
     * @param y y coordinate of image
     * @param texture instance of texture
     */
    public OpenGL2DImage(final float x, final float y, final float width, final float height, OpenGL2DTexture texture) {
        //create mesh
        //Mesh mesh = buildMesh(x, y, width, height, image);

        //set image and build mesh
        this.setTexture(x, y, width, height, texture);

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
    @Deprecated
    public OpenGL2DImage(final float x, final float y, final float width, final float height, Image2D image) {
        //create mesh
        //Mesh mesh = buildMesh(x, y, width, height, image);

        //set image and build mesh
        this.setImage(x, y, width, height, image);

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
     * @param texture instance of texture
     */
    public OpenGL2DImage(final float x, final float y, OpenGL2DTexture texture) {
        this(x, y, texture.getWidth(), texture.getHeight(), texture);
    }

    /**
     * default constructor
     *
     * @param x x coordinate of image
     * @param y y coordinate of image
     * @param image instance of image
     */
    @Deprecated
    public OpenGL2DImage(final float x, final float y, Image2D image) {
        this(x, y, image.getWidth(), image.getHeight(), image);
    }

    protected Mesh buildMesh (final float x, final float y, final float width, final float height, OpenGL2DTexture texture) {
        List<Float> positions = new ArrayList();
        List<Float> textCoords = new ArrayList();

        //we dont need normals yet
        float[] normals   = new float[0];

        //indices
        List<Integer> indices   = new ArrayList();

        int i = 0;

        //left top vertex
        positions.add(x); //x
        positions.add(y); //y
        positions.add(ZPOS); //z
        textCoords.add(/*(float) charInfo.getStartX() / (float)fontTexture.getWidth()*/x / width);
        textCoords.add(0.0f);
        indices.add(i * VERTICES_PER_QUAD);

        //left bottom vertex
        positions.add(x); // x
        positions.add(height); //y
        positions.add(ZPOS); //z
        textCoords.add(/*(float)charInfo.getStartX() / (float)fontTexture.getWidth()*/x / width);
        textCoords.add(1.0f);
        indices.add(i * VERTICES_PER_QUAD + 1);

        //right bottom vertex
        positions.add(x + width); // x
        positions.add(height); //y
        positions.add(ZPOS); //z
        textCoords.add((float)(x + width)/ width);
        textCoords.add(1.0f);
        indices.add(i * VERTICES_PER_QUAD + 2);

        //right top vertex
        positions.add(x + width); // x
        positions.add(0.0f); //y
        positions.add(ZPOS); //z
        textCoords.add((x + width) / width);
        textCoords.add(0.0f);
        indices.add(i * VERTICES_PER_QUAD + 3);

        //add indices for left top and bottom right vertices
        indices.add(i * VERTICES_PER_QUAD);
        indices.add(i * VERTICES_PER_QUAD + 2);

        float[] posArr = ArrayUtils.convertFloatListToArray(positions);
        float[] textCoordsArr = ArrayUtils.convertFloatListToArray(textCoords);

        int[] indicesArr = indices.stream().mapToInt((j) -> {
            return j;
        }).toArray();

        //create new mesh with material
        Mesh mesh = new Mesh(posArr, textCoordsArr, normals, indicesArr);
        mesh.setMaterial(new Material(texture));

        return mesh;
    }

    public void setTexture (final float x, final float y, final float width, final float height, final OpenGL2DTexture texture) {
        this.width = width;
        this.height = height;

        //get old texture
        OpenGL2DTexture oldTexture = this.texture;

        //create and upload new texture
        this.texture = texture;

        //create new mesh
        Mesh mesh = buildMesh(0, 0, width, height, texture);

        //get old mesh
        Mesh oldMesh = getMesh();

        //set new mesh
        setMesh(mesh);

        //set default color #FFFFFF
        this.getMesh().getMaterial().setColor(new Vector3f(1f, 1f, 1f));

        if (oldMesh != null) {
            //delete old mesh from gpu
            oldMesh.cleanUp();
        }

        /*if (oldTexture != null) {
            //delete old texture from gpu
            oldTexture.delete();
        }*/
    }

    @Deprecated
    public void setImage (final float x, final float y, final float width, final float height, final Image2D image) {
        this.width = width;
        this.height = height;

        //get old texture
        OpenGL2DTexture oldTexture = this.texture;

        //create and upload new texture
        this.texture = OpenGL2DTexture.createAndUpload(image);

        //create new mesh
        Mesh mesh = buildMesh(0, 0, width, height, texture);

        //get old mesh
        Mesh oldMesh = getMesh();

        //set new mesh
        setMesh(mesh);

        //set default color #FFFFFF
        this.getMesh().getMaterial().setColor(new Vector3f(1f, 1f, 1f));

        if (oldMesh != null) {
            //delete old mesh from gpu
            oldMesh.cleanUp();
        }

        if (oldTexture != null) {
            //delete old texture from gpu
            oldTexture.delete();
        }
    }

    public void setTexture (final float x, final float y, final OpenGL2DTexture texture) {
        this.setTexture(x, y, texture.getWidth(), texture.getHeight(), texture);
    }

    @Deprecated
    public void setImage (final float x, final float y, final Image2D image) {
        this.setImage(x, y, image.getWidth(), image.getHeight(), image);
    }

    public void setTexture (final OpenGL2DTexture texture) {
        this.setTexture(0, 0, texture.getWidth(), texture.getHeight(), texture);
    }

    @Deprecated
    public void setImage (final Image2D image) {
        this.setImage(0, 0, image.getWidth(), image.getHeight(), image);
    }

    public float getX () {
        return this.getPosition().getX();
    }

    public float getY () {
        return this.getPosition().getY();
    }

    public float getWidth () {
        return this.width;
    }

    public float getHeight () {
        return this.height;
    }

}
