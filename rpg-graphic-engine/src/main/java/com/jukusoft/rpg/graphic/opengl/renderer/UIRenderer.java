package com.jukusoft.rpg.graphic.opengl.renderer;

import com.jukusoft.rpg.core.math.Matrix4f;
import com.jukusoft.rpg.core.math.Vector3f;
import com.jukusoft.rpg.core.utils.FileUtils;
import com.jukusoft.rpg.graphic.exception.OpenGLShaderException;
import com.jukusoft.rpg.graphic.math.TransformationUtils;
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
    protected OpenGLShaderProgram uiShaderProgram = null;

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

    /**
    * default constructor
    */
    public UIRenderer (final String vertexShaderPath, final String fragmentShaderPath) throws IOException, OpenGLShaderException {
        this.init(vertexShaderPath, fragmentShaderPath);
    }

    /**
    * constructor without initialization
    */
    protected UIRenderer () {
        //
    }

    /**
    * initialize UI renderer
    */
    public void init (final String vertexShaderPath, final String fragmentShaderPath) throws IOException, OpenGLShaderException {
        if (isInitialized.get()) {
            throw new IllegalStateException("UIRenderer was already initialized.");
        }

        //create new UI shader program
        this.uiShaderProgram = new OpenGLShaderProgram();

        System.out.println("HUD vertex shader:\n" + FileUtils.readFile(vertexShaderPath, StandardCharsets.UTF_8));
        System.out.println("HUD fragment shader:\n" + FileUtils.readFile(fragmentShaderPath, StandardCharsets.UTF_8));

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

        this.isInitialized.set(true);
    }

    /**
    * render UI
    */
    public void render (int windowWidth, int windowHeight, List<DrawableObject> drawableObjects) {
        //check, if UI renderer was initialized
        if (!this.isInitialized.get()) {
            throw new IllegalStateException("UIRenderer wasnt initialized yet, call init() method first.");
        }

        //bind shader program
        this.uiShaderProgram.bind();

        //generate projection matrix / view matrix
        final Matrix4f projMatrix = getProjMatrix(windowWidth, windowHeight);

        //System.out.println("ortho matrix with width: " + windowWidth + " and height: " + windowHeight + " on renderer:\n" + this.projMatrix.toString(true));

        //iterate through all drawable objects
        for (DrawableObject obj : drawableObjects) {
            //first get mesh
            Mesh mesh = obj.getMesh();

            //get rotation
            Vector3f rotation = obj.getRotation();

            Matrix4f projModelMatrix = TransformationUtils.getOrtoProjModelMatrix(obj, projMatrix);

            //set identity matrix
            /*this.modelViewMatrix.setIdentityMatrix();

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
            this.viewCurrent.mul(this.modelViewMatrix);*/

            //System.out.println(obj.toString());

            //System.out.println(this.viewCurrent.toString(true));

            //System.out.println("projModelMatrix:\n" + projModelMatrix.toString(true) + "\n\n");

            //get color
            Vector3f color = obj.getMesh().getMaterial().getColor();

            if (color == null) {
                throw new RuntimeException("color cannot be null.");
            }

            //set shader program parameter
            this.uiShaderProgram.setUniform("projModelMatrix", projMatrix);
            this.uiShaderProgram.setUniform("baseColor", color);
            this.uiShaderProgram.setUniform("hasTexture", obj.getMesh().getMaterial().isTextured() ? 1 : 0);

            //render mesh
            mesh.render();
        }

        //unbind shader program
        this.uiShaderProgram.unbind();
    }

    protected Matrix4f getProjMatrix (final int windowWidth, final int windowHeight) {
        return TransformationUtils.getOrthoProjMatrix(0, windowWidth, windowHeight, 0, new Matrix4f());
    }

}
