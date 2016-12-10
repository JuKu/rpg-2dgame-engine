package com.jukusoft.rpg.graphic.opengl.image;

import com.jukusoft.rpg.core.math.Vector3f;
import com.jukusoft.rpg.core.utils.ArrayUtils;
import com.jukusoft.rpg.graphic.opengl.mesh.DrawableObject;
import com.jukusoft.rpg.graphic.opengl.mesh.Material;
import com.jukusoft.rpg.graphic.opengl.mesh.Mesh;
import com.jukusoft.rpg.graphic.opengl.texture.OpenGL2DTexture;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Justin on 10.12.2016.
 */
public class OpenGL2DTextureRegion extends DrawableObject {

    /**
     * some constants
     */
    private static final float ZPOS = 0.0f;
    private static final int VERTICES_PER_QUAD = 4;

    protected float width = 0;
    protected float height = 0;

    protected OpenGL2DTexture texture = null;

    /**
    * some interesting links:
     *  - http://www.meandmark.com/tilingpart1.html
     *  - http://gamedev.stackexchange.com/questions/30362/drawing-lots-of-tiles-with-opengl-the-modern-way
     *  - https://www.opengl.org/discussion_boards/showthread.php/172751-Approaches-for-2D-tile-rendering
    */

    /**
    * viewport of image
    */
    protected ImageRegionInfo imageRegionInfo = null;

    /**
    * default constructor
    */
    public OpenGL2DTextureRegion (final float x, final float y, final float startX, final float startY, final float width, final float height, OpenGL2DTexture texture) {
        //set image and build mesh
        this.setTexture(x, y, startX, startY, width, height, texture);

        //set default color #FFFFFF
        this.getMesh().getMaterial().setColor(new Vector3f(1f, 1f, 1f));

        //set position
        this.setPosition(x, y, 0);
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

    public void setTexture (final float x, final float y, final float startX, final float startY, final float width, final float height, OpenGL2DTexture texture) {
        //set region (viewport of image)
    }

    public void setRegion (final float startX, final float startY, final float width, final float height) {
        if (this.imageRegionInfo == null) {
            this.imageRegionInfo = new ImageRegionInfo(startX, startY, width, height);
        } else {
            this.imageRegionInfo.set(startX, startY, width, height);
        }
    }

}
