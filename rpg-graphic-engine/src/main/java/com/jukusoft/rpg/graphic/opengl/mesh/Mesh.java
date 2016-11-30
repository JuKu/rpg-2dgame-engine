package com.jukusoft.rpg.graphic.opengl.mesh;

import com.jukusoft.rpg.graphic.opengl.buffer.FloatVertexBufferObject;
import com.jukusoft.rpg.graphic.opengl.buffer.IntegerVertexBufferObject;
import com.jukusoft.rpg.graphic.opengl.buffer.VertexArrayObject;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;

/**
 * Created by Justin on 30.11.2016.
 */
public class Mesh {

    /**
     * vertex buffer objects
     */
    protected FloatVertexBufferObject positionVBO = null;
    protected FloatVertexBufferObject textureCoordinatesVBO = null;
    protected FloatVertexBufferObject vertexNormalsVBO = null;
    protected IntegerVertexBufferObject indexVBO = null;

    protected Material material = null;

    /**
     * vertex array object
     */
    protected VertexArrayObject vao = new VertexArrayObject();

    protected int vertexCount = 0;

    public Mesh(float[] positions, float[] textCoords, float[] normals, int[] indices) {
        //get vertex count
        vertexCount = indices.length;

        //bind vertex array object
        vao.bind();

        //add position VBO
        this.setPositionVBO(positions);

        //add texture coordinates VBO
        this.setTextureCoordinatesVBO(textCoords);

        //add normals VBO
        this.setNormalesVBO(normals);

        //add indix VBO
        this.setIndexVBO(indices);

        //unbind vertex array object
        vao.unbind();
    }

    protected void setPositionVBO (float[] positions) {
        if (this.positionVBO == null) {
            this.positionVBO = new FloatVertexBufferObject(GL_ARRAY_BUFFER);
        }

        //bind VBO
        this.positionVBO.bind();

        //put data
        this.positionVBO.putData(positions);

        //define structure for 3d object
        this.positionVBO.defineStructure(0, 3, false, 0, 0);

        //unbind VBO
        this.positionVBO.unbind();
    }

    protected void setTextureCoordinatesVBO (float[] textCoords) {
        if (this.textureCoordinatesVBO == null) {
            this.textureCoordinatesVBO = new FloatVertexBufferObject(GL_ARRAY_BUFFER);
        }

        //bind VBO
        this.textureCoordinatesVBO.bind();

        //put data
        this.textureCoordinatesVBO.putData(textCoords);

        //define structure, its only an 2D object
        this.textureCoordinatesVBO.defineStructure(1, 2, false, 0, 0);

        //unbind VBO
        this.textureCoordinatesVBO.unbind();
    }

    protected void setNormalesVBO (float[] normals) {
        if (this.vertexNormalsVBO == null) {
            this.vertexNormalsVBO = new FloatVertexBufferObject(GL_ARRAY_BUFFER);
        }

        //bind VBO
        this.vertexNormalsVBO.bind();

        //put data
        this.vertexNormalsVBO.putData(normals);

        //define structure in 3D
        this.vertexNormalsVBO.defineStructure(2, 3, false, 0, 0);

        //unbind VBO
        this.vertexNormalsVBO.unbind();
    }

    protected void setIndexVBO (int[] indices) {
        if (this.indexVBO == null) {
            this.indexVBO = new IntegerVertexBufferObject(GL_ELEMENT_ARRAY_BUFFER);
        }

        //bind VBO
        this.indexVBO.bind();

        //put data
        this.indexVBO.putData(indices);

        //define structure in 3D
        this.indexVBO.defineStructure(2, 3, false, 0, 0);

        //unbind VBO
        this.indexVBO.unbind();
    }

    public int getVertexCount () {
        return this.vertexCount;
    }

    public Material getMaterial () {
        return this.material;
    }

    public void setMaterial (Material material) {
        this.material = material;
    }

    public void cleanUp () {
        if (this.positionVBO != null) {
            this.positionVBO.delete();
        }

        if (this.textureCoordinatesVBO != null) {
            this.textureCoordinatesVBO.delete();
        }

        if (this.vertexNormalsVBO != null) {
            this.vertexNormalsVBO.delete();
        }

        if (this.indexVBO != null) {
            this.indexVBO.delete();
        }
    }

}
