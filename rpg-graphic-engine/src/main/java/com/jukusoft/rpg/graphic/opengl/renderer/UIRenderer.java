package com.jukusoft.rpg.graphic.opengl.renderer;

import com.jukusoft.rpg.core.logger.GameLogger;
import com.jukusoft.rpg.core.math.Matrix4f;
import com.jukusoft.rpg.core.math.Vector3f;
import com.jukusoft.rpg.core.utils.FileUtils;
import com.jukusoft.rpg.graphic.added.*;
import com.jukusoft.rpg.graphic.exception.OpenGLShaderException;
import com.jukusoft.rpg.graphic.math.TransformationUtils;
import com.jukusoft.rpg.graphic.opengl.font.FontTexture;
import com.jukusoft.rpg.graphic.opengl.mesh.DrawableObject;
import com.jukusoft.rpg.graphic.opengl.mesh.Mesh;
import com.jukusoft.rpg.graphic.opengl.shader.OpenGLShaderProgram;
import com.jukusoft.rpg.graphic.opengl.text.OpenGLText;

import java.awt.*;
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

    private static final Font FONT = new Font("Arial", Font.PLAIN, 20);

    private static final String CHARSET = "ISO-8859-1";

    private TextItem statusTextItem;
    private OpenGLText text;
    private Transformation transformation;


    /**
    * default constructor
    */
    public UIRenderer (final String vertexShaderPath, final String fragmentShaderPath) throws IOException, OpenGLShaderException {
        try {
            this.init(vertexShaderPath, fragmentShaderPath);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        this.transformation = new Transformation();

        FontTexture fontTexture = new FontTexture(FONT, CHARSET, Color.BLUE);

        try {
            this.statusTextItem = new TextItem("DEMO", fontTexture);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        this.statusTextItem.getMesh().getMaterial().setColor(new Vector3f(1, 1, 1));
        this.text = new OpenGLText(0, 0, "TEXT", fontTexture);
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
    public void init (final String vertexShaderPath, final String fragmentShaderPath) throws Exception {
        if (isInitialized.get()) {
            throw new IllegalStateException("UIRenderer was already initialized.");
        }

        //create new UI shader program
        this.uiShaderProgram = new OpenGLShaderProgram();

        System.out.println("HUD vertex shader:\n" + FileUtils.readFile(vertexShaderPath, StandardCharsets.UTF_8));
        System.out.println("HUD fragment shader:\n" + FileUtils.readFile(fragmentShaderPath, StandardCharsets.UTF_8));

        //add vertex shader
        this.uiShaderProgram.setVertexShader(FileUtils.readFile(vertexShaderPath, StandardCharsets.UTF_8));//.setVertexShader(FileUtils.readFile(vertexShaderPath, StandardCharsets.UTF_8));

        //add fragment shader
        this.uiShaderProgram.setFragmentShader(FileUtils.readFile(fragmentShaderPath, StandardCharsets.UTF_8));
        //.setFragmentShader(FileUtils.readFile(fragmentShaderPath, StandardCharsets.UTF_8));

        //compile shader program
        this.uiShaderProgram.link();

        //create uniforms for ortographic model projection matrix and base color
        this.uiShaderProgram.createUniform("projModelMatrix");
        this.uiShaderProgram.createUniform("colour");
        this.uiShaderProgram.createUniform("hasTexture");

        this.isInitialized.set(true);
    }

    /**
    * render UI
    */
    public void render (int windowWidth, int windowHeight, List<DrawableObject> drawableObjects) {
        if (GameLogger.isRendererDebugMode()) {
            GameLogger.debug("UIRenderer", "render UI.");
        }

        /*if (GameLogger.isRendererDebugMode()) {
            GameLogger.debug("UIRenderer", "render UI");
        }

        //check, if UI renderer was initialized
        if (!this.isInitialized.get()) {
            throw new IllegalStateException("UIRenderer wasnt initialized yet, call init() method first.");
        }

        //bind shader program
        this.uiShaderProgram.bind();

        //generate projection matrix / view matrix
        final Matrix4f projMatrix = getProjMatrix(windowWidth, windowHeight);

        //iterate through all drawable objects
        for (DrawableObject obj : drawableObjects) {
            //first get mesh
            Mesh mesh = obj.getMesh();

            //get rotation
            Vector3f rotation = obj.getRotation();

            Matrix4f projModelMatrix = TransformationUtils.getOrtoProjModelMatrix(obj, projMatrix);

            //get color
            Vector3f color = obj.getMesh().getMaterial().getColor();

            if (color == null) {
                throw new RuntimeException("color cannot be null.");
            }

            System.out.println("position: " + obj.getPosition().toString());
            System.out.println("rotation: " + obj.getRotation().toString());
            System.out.println("scale: " + obj.getScale());
            System.out.println("color: " + obj.getMesh().getMaterial().getColor());

            System.out.println(projModelMatrix.toString(true));

            //set shader program parameter
            this.uiShaderProgram.setUniform("projModelMatrix", projModelMatrix);
            this.uiShaderProgram.setUniform("colour", color);
            this.uiShaderProgram.setUniform("hasTexture", obj.getMesh().getMaterial().isTextured() ? 1 : 0);

            //render mesh
            mesh.render();
        }

        //unbind shader program
        this.uiShaderProgram.unbind();*/

        uiShaderProgram.bind();

        Matrix4f ortho = TransformationUtils.getOrthoProjectionMatrix(0, windowWidth, windowHeight, 0);

        Mesh mesh = statusTextItem.getMesh();
        // Set ortohtaphic and model matrix for this HUD item
        Matrix4f projModelMatrix = transformation.getOrtoProjModelMatrix(statusTextItem, ortho);

        uiShaderProgram.setUniform("projModelMatrix", projModelMatrix);
        uiShaderProgram.setUniform("colour", statusTextItem.getMesh().getMaterial().getColor());
        uiShaderProgram.setUniform("hasTexture", statusTextItem.getMesh().getMaterial().isTextured() ? 1 : 0);

        // Render the mesh for this HUD item
        mesh.render();

        /**
        * render text
        */

        /*Mesh mesh1 = text.getMesh();
        Matrix4f projModelMatrix1 = transformation.getOrtoProjModelMatrix(text, ortho);

        uiShaderProgram.setUniform("projModelMatrix", projModelMatrix1);
        uiShaderProgram.setUniform("colour", text.getMesh().getMaterial().getColor());
        uiShaderProgram.setUniform("hasTexture", text.getMesh().getMaterial().isTextured() ? 1 : 0);

        // Render the mesh for this HUD item
        mesh1.render();*/

        uiShaderProgram.unbind();
    }

    protected Matrix4f getProjMatrix (final int windowWidth, final int windowHeight) {
        return TransformationUtils.getOrthoProjectionMatrix(0, windowWidth, windowHeight, 0, new Matrix4f());
    }

}
