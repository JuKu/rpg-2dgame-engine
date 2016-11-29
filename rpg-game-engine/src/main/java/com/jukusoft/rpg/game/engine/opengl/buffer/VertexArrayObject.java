package com.jukusoft.rpg.game.engine.opengl.buffer;

import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.*;

/**
 * Created by Justin on 24.08.2016.
 */
public class VertexArrayObject {

    /**
    * if of vertex array object on gpu
    */
    protected int vaoID = 0;

    /**
    * default constructor
    */
    public VertexArrayObject () {
        //create VAO on gpu
        this.create();
    }

    protected void create () {
        //create new vertex array object on gpu
        this.vaoID = glGenVertexArrays();
    }

    public void bind () {
        //bind VAO
        glBindVertexArray(this.vaoID);
    }

    public void unbind () {
        //unbind the VAO
        glBindVertexArray(0);
    }

    public void disable () {
        glDisableVertexAttribArray(0);
    }

    public void enable () {
        glEnableVertexAttribArray(0);
    }

    public void delete () {
        glBindVertexArray(0);
        glDeleteVertexArrays(this.vaoID);
    }

    public void cleanUp () {
        this.delete();
    }

}
