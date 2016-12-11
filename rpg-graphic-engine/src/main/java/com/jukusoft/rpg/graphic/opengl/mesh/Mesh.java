package com.jukusoft.rpg.graphic.opengl.mesh;

import com.jukusoft.rpg.core.logger.GameLogger;
import com.jukusoft.rpg.graphic.opengl.buffer.FloatVertexBufferObject;
import com.jukusoft.rpg.graphic.opengl.buffer.IntegerVertexBufferObject;
import com.jukusoft.rpg.graphic.opengl.buffer.VertexArrayObject;
import com.jukusoft.rpg.graphic.opengl.texture.OpenGL2DTexture;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

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

    public void setPositionVBO (float[] positions) {
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

    public void setTextureCoordinatesVBO (float[] textCoords) {
        if (this.textureCoordinatesVBO == null) {
            this.textureCoordinatesVBO = new FloatVertexBufferObject(GL_ARRAY_BUFFER, GL_DYNAMIC_DRAW);
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

    public void setNormalesVBO (float[] normals) {
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

    public void setIndexVBO (int[] indices) {
        if (this.indexVBO == null) {
            this.indexVBO = new IntegerVertexBufferObject(GL_ELEMENT_ARRAY_BUFFER);
        }

        //bind VBO
        this.indexVBO.bind();

        //put data
        this.indexVBO.putData(indices);

        //define structure in 3D
        //this.indexVBO.defineStructure(2, 3, false, 0, 0);

        //unbind VBO
        this.indexVBO.unbind();
    }

    public int getVertexCount () {
        return this.vertexCount;
    }

    public int getVaoID () {
        return this.vao.getVaoID();
    }

    public Material getMaterial () {
        return this.material;
    }

    public void setMaterial (Material material) {
        this.material = material;
    }

    public void render() {
        OpenGL2DTexture texture = material.getTexture();

        if (texture != null) {
            if (GameLogger.isRendererDebugMode()) {
                GameLogger.debug("Mesh", "set active texture");
            }

            //activate firs texture bank
            glActiveTexture(GL_TEXTURE0);

            //bind the texture
            glBindTexture(GL_TEXTURE_2D, texture.getTextureID());
        } else {
            if (GameLogger.isRendererDebugMode()) {
                GameLogger.debug("Mesh", "Mesh with vaoID " + getVaoID() + "doesnt have an texture.");
            }
        }

        //TODO: replace OpenGL commands with FloatVertexBufferObject methods

        this.beforeRender();

        //draw the mesh
        glBindVertexArray(this.getVaoID());
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);

        if (GameLogger.isRendererDebugMode()) {
            GameLogger.debug("Mesh", "draw mesh with VaoID: " + getVaoID() + ", vertex count: " + getVertexCount() + ", textureID: " + texture.getTextureID());
        }

        glDrawElements(GL_TRIANGLES, getVertexCount(), GL_UNSIGNED_INT, 0);

        // Restore state
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        glBindVertexArray(0);
        glBindTexture(GL_TEXTURE_2D, 0);

        this.afterRender();
    }

    protected void beforeRender () {
        //
    }

    public void afterRender () {
        //
    }

    public void deleteBuffers() {
        glDisableVertexAttribArray(0);

        // Delete the VBOs
        /*glBindBuffer(GL_ARRAY_BUFFER, 0);
        for (int vboId : vboIdList) {
            glDeleteBuffers(vboId);
        }*/

        this.positionVBO.delete();
        this.textureCoordinatesVBO.delete();
        this.vertexNormalsVBO.delete();
        this.indexVBO.delete();

        // Delete the VAO
        this.vao.unbind();
        this.vao.delete();
        //glBindVertexArray(0);
        //glDeleteVertexArrays(vaoId);
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

        this.vao.delete();
    }

}
