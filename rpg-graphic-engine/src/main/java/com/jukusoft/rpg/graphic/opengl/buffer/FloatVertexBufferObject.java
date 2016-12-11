package com.jukusoft.rpg.graphic.opengl.buffer;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

/**
 * Created by Justin on 24.08.2016.
 */
public class FloatVertexBufferObject {

    /**
    * id of vertex buffer object on gpu
    */
    protected int vboID = 0;

    /**
    * flag, if VBO is bind
    */
    protected boolean isBind = false;

    protected final int target;

    /**
    * usage - see https://www.opengl.org/wiki/Buffer_Object#Buffer_Object_Usage
    */
    protected int usage = GL_STATIC_DRAW;

    public FloatVertexBufferObject(final int target) {
        this.target = target;

        this.create();
    }

    public FloatVertexBufferObject(final int target, final int usage) {
        this.target = target;
        this.usage = usage;

        this.create();
    }

    public FloatVertexBufferObject() {
        this.target = GL_ARRAY_BUFFER;
        this.create();
    }

    /**
    * create new vertex buffer object on gpu
    */
    protected void create () {
        //create new buffer
        this.vboID = glGenBuffers();
    }

    /**
    * bind vertex buffer object on gpu
    */
    public void bind () {
        //bind buffer
        glBindBuffer(this.target, this.vboID);

        //set bind flag to true
        this.isBind = true;
    }

    public void unbind () {
        //unbind the VBO
        glBindBuffer(this.target, 0);

        //set bind flag to false
        this.isBind = false;
    }

    public void putData (FloatBuffer buffer) {
        //put buffer
        glBufferData(this.target, buffer, this.usage);
    }

    public void putData (float[] vertices) {
        //create new float buffer
        FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(vertices.length);

        //put vertices to float buffer
        verticesBuffer.put(vertices).flip();

        //put buffer to vertex buffer object
        this.putData(verticesBuffer);
    }

    public void defineStructure (final int index, final int size, final boolean normalized, final int stride, final int offset) {
        int type = GL_FLOAT;//data type of values

        glVertexAttribPointer(index, size, type, false, stride, offset);
    }

    public void defineStructure () {
        int index = 0;
        int size = 3;//3 for 3D array, 4 for 4D array, values from 1 to 4 are possible
        int type = GL_FLOAT;//data type of values
        boolean normalized = false;//Specifies if the values should be normalized or not.
        int stride = 0;//Specifies the byte offset between consecutive generic vertex attributes.
        int offset = 0;//Specifies a offset of the first component of the first component in the array in the data store of the buffer.


        glVertexAttribPointer(index, size, type, false, stride, offset);
    }

    public void delete () {
        glBindBuffer(this.target, 0);
        glDeleteBuffers(this.vboID);
    }

    public static FloatVertexBufferObject createAndPutBuffer (float[] vertices) {
        FloatVertexBufferObject vbo = new FloatVertexBufferObject();

        //bind VBO
        vbo.bind();

        //put data
        vbo.putData(vertices);

        //define structure
        vbo.defineStructure();

        //unbind VBO
        vbo.unbind();

        //return instance of vertex buffer object
        return vbo;
    }

}
