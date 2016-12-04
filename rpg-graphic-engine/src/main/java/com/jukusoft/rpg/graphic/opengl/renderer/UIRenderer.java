package com.jukusoft.rpg.graphic.opengl.renderer;

import com.jukusoft.rpg.core.math.Matrix4f;
import com.jukusoft.rpg.core.math.Vector3f;
import com.jukusoft.rpg.core.utils.FileUtils;
import com.jukusoft.rpg.graphic.exception.OpenGLShaderException;
import com.jukusoft.rpg.graphic.opengl.mesh.DrawableObject;
import com.jukusoft.rpg.graphic.opengl.mesh.Mesh;
import com.jukusoft.rpg.graphic.opengl.shader.OpenGLShaderProgram;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Justin on 01.12.2016.
 */
public class UIRenderer {

    /**
    * flag, if renderer was initialized
    */
    protected AtomicBoolean isInitialized = new AtomicBoolean(false);

    /**
    * shader program for UI
    */
    protected OpenGLShaderProgram uiShaderProgram = new OpenGLShaderProgram();

    /**
    * orthogonal projection matrix
    */
    protected Matrix4f projMatrix = new Matrix4f();

    /**
    * model matrix
    */
    protected Matrix4f modelViewMatrix = new Matrix4f();

    /**
    * temporary view matrix for each game object
    */
    protected Matrix4f viewCurrent = new Matrix4f();

    public UIRenderer (final String vertexShaderPath, final String fragmentShaderPath) throws IOException, OpenGLShaderException {
        this.init(vertexShaderPath, fragmentShaderPath);
    }

    /**
    * initialize UI renderer
    */
    public void init (final String vertexShaderPath, final String fragmentShaderPath) throws IOException, OpenGLShaderException {
        if (isInitialized.get()) {
            throw new IllegalStateException("UIRenderer was already initialized.");
        }

        //add vertex shader
        this.uiShaderProgram.setVertexShader(FileUtils.readFile(vertexShaderPath, StandardCharsets.UTF_8));

        //add fragment shader
        this.uiShaderProgram.setFragmentShader(FileUtils.readFile(fragmentShaderPath, StandardCharsets.UTF_8));

        //compile shader program
        this.uiShaderProgram.link();

        //create uniforms for ortographic model projection matrix and base color
        this.uiShaderProgram.createUniform("projModelMatrix");
        this.uiShaderProgram.createUniform("baseColor");
        this.uiShaderProgram.createUniform("hasTexture");
    }

    /**
    * render UI
    */
    public void render (int windowWidth, int windowHeight, List<DrawableObject> drawableObjects) {
        //bind shader program
        this.uiShaderProgram.bind();

        //generate projection matrix / view matrix
        this.projMatrix.setIdentityMatrix();
        this.projMatrix.setOrtho2D(0, windowWidth, windowHeight, 0);

        //iterate through all drawable objects
        for (DrawableObject obj : drawableObjects) {
            //first get mesh
            Mesh mesh = obj.getMesh();

            //get rotation
            Vector3f rotation = obj.getRotation();

            //set identity matrix
            this.modelViewMatrix.setIdentityMatrix();

            //translate matrix with position of game object
            this.modelViewMatrix.translate(obj.getPosition());

            //rotate matrix
            this.modelViewMatrix.rotateX((float) Math.toRadians(-rotation.getX()));
            this.modelViewMatrix.rotateY((float) Math.toRadians(-rotation.getY()));
            this.modelViewMatrix.rotateZ((float) Math.toRadians(-rotation.getZ()));

            //scale game object
            this.modelViewMatrix.scale(obj.getScale());

            this.viewCurrent.reset();
            this.viewCurrent.set(projMatrix);
            this.viewCurrent.mul(this.modelViewMatrix);

            //set shader program parameter
            this.uiShaderProgram.setUniform("projModelMatrix", this.viewCurrent);
            this.uiShaderProgram.setUniform("colour", obj.getMesh().getMaterial().getColor());
            this.uiShaderProgram.setUniform("hasTexture", obj.getMesh().getMaterial().isTextured() ? 1 : 0);

            //render mesh
            mesh.render();
        }

        //unbind shader program
        this.uiShaderProgram.unbind();
    }

}
