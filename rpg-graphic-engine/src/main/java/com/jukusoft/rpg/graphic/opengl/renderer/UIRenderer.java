package com.jukusoft.rpg.graphic.opengl.renderer;

import com.jukusoft.rpg.core.logger.GameLogger;
import com.jukusoft.rpg.core.math.Matrix4f;
import com.jukusoft.rpg.core.math.Vector3f;
import com.jukusoft.rpg.core.utils.FileUtils;
import com.jukusoft.rpg.graphic.animation.Animable;
import com.jukusoft.rpg.graphic.exception.OpenGLShaderException;
import com.jukusoft.rpg.graphic.math.TransformationUtils;
import com.jukusoft.rpg.graphic.opengl.mesh.DrawableObject;
import com.jukusoft.rpg.graphic.opengl.shader.OpenGLShaderProgram;
import com.jukusoft.rpg.graphic.renderer.Renderable;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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
    * cached drawable objects list
    */
    protected List<DrawableObject> drawableObjectsCache = new ArrayList<>();

    /**
    * cached window size, so we can detect, if ortho matrix has to be calculated again or we can used cached matrix
    */
    protected int lastWindowWidth = 0;
    protected int lastWindowHeight = 0;

    /**
    * cached ortho matrix
    */
    protected Matrix4f cachedOrthoMatrix = new Matrix4f();

    /**
    * cached matrix instance, so we dont need to create an new matrix on each renderer loop
    */
    protected Matrix4f cachedModelMatrix = new Matrix4f();

    /**
    * cached projection model matrix, so we donnt need to create an new matrix on each renderer loop
    */
    protected Matrix4f cachedProjModelMatrix = new Matrix4f();

    protected boolean redrawSameMeshOptimization = true;
    protected long lastMeshID = -1;
    protected Vector3f lastPosition = null;
    protected Vector3f lastRotation = null;
    protected float lastScale = 0;

    /**
    * flag, if lighting is enabled
    */
    protected AtomicBoolean lightingEnabled = new AtomicBoolean(true);

    /**
    * ambient light params
    */
    public float ambientIntensity = .7f;
    public final Vector3f ambientColor = new Vector3f(0.3f, 0.3f, 0.7f);

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
    public void render (int windowWidth, int windowHeight, List<Renderable> drawableObjectsList) {
        //clear old cache
        //this.drawableObjectsCache.clear();

        //add all entries to reverse list later
        //this.drawableObjectsCache.addAll(drawableObjectsList);

        //reverse list
        //Collections.reverse(this.drawableObjectsCache);

        //create new list and add all entries to reverse list
        List<Renderable> drawableObjects = drawableObjectsList;

        if (drawableObjects == null) {
            throw new IllegalArgumentException("drawableObjects list cannot be null.");
        }

        if (GameLogger.isRendererDebugMode()) {
            GameLogger.debug("UIRenderer", "render UI.");
        }

        if (!this.isInitialized.get()) {
            throw new IllegalStateException("UIRenderer wasnt initialized yet, call init() method first.");
        }

        uiShaderProgram.bind();

        //check, if we can used cached ortho matrix
        if (wasReized(windowWidth, windowHeight)) {
            //invalidate old ortho matrix and generate an new one, use same destination matrix, so we dont need to create an new matrix instance
            this.cachedOrthoMatrix = TransformationUtils.getOrthoProjectionMatrix(0, windowWidth, windowHeight, 0, this.cachedOrthoMatrix);

            GameLogger.debug("UIRenderer", "invalidate cached ortho matrix and generate an new one, because window was resized.");
        }

        //get ortho matrix from cached ortho matrix
        Matrix4f ortho = this.cachedOrthoMatrix;

        final long currentTime = System.currentTimeMillis();

        //iterate through all drawable objects
        for (Renderable obj : drawableObjects) {
            //check, if object is an animation
            if (obj instanceof Animable) {
                Animable animation = (Animable) obj;

                //update frame if neccessary
                animation.updateFrame(currentTime);
            }

            if (this.redrawSameMeshOptimization && !obj.redrawWithSameParams() && this.lastMeshID == obj.getMeshID() &&
                    this.lastPosition != null && this.lastPosition.equals(obj.getPosition()) &&
                    this.lastRotation != null && this.lastRotation.equals(obj.getRotation()) &&
                    this.lastScale == obj.getScale()) {
                //we dont need to redraw the same texture with the same params, because the underlaying texture isnt visible
                continue;
            }

            //get mesh
            //final Mesh mesh = obj.getMesh();

            //calculate projection model matrix
            final Matrix4f projModelMatrix = TransformationUtils.getOrtoProjModelMatrix(obj, ortho, this.cachedModelMatrix, this.cachedProjModelMatrix);

            uiShaderProgram.setUniform("projModelMatrix", projModelMatrix);
            uiShaderProgram.setUniform("colour", obj.getMaterial().getColor());
            uiShaderProgram.setUniform("hasTexture", obj.getMaterial().isTextured() ? 1 : 0);

            //render mesh
            obj.render();

            //set last params
            this.lastMeshID = obj.getMeshID();
            this.lastPosition = obj.getPosition();
            this.lastRotation = obj.getRotation();
            this.lastScale = obj.getScale();
        }

        //reset params
        this.lastMeshID = -1;
        this.lastPosition = null;
        this.lastRotation = null;
        this.lastScale = Integer.MAX_VALUE;

        uiShaderProgram.unbind();
    }

    public void setAmbientIntensity (final float ambientIntensity) {
        this.ambientIntensity = ambientIntensity;
    }

    public void setAmbientColor (final float x, final float y, final float z) {
        this.ambientColor.set(x, y, z);
    }

    protected Matrix4f getProjMatrix (final int windowWidth, final int windowHeight) {
        return TransformationUtils.getOrthoProjectionMatrix(0, windowWidth, windowHeight, 0, new Matrix4f());
    }

    protected boolean wasReized (final int windowWidth, final int windowHeight) {
        //check, if window was resized
        final boolean wasResized = this.lastWindowWidth != windowWidth || this.lastWindowHeight != windowHeight;

        //cache last window width & height
        this.lastWindowWidth = windowWidth;
        this.lastWindowHeight = windowHeight;

        return wasResized;
    }

    public void disableRedrawSameMeshOptimization () {
        this.redrawSameMeshOptimization = false;
    }

    public void enableRedrawSameMeshOptimization () {
        this.redrawSameMeshOptimization = true;
    }

}
