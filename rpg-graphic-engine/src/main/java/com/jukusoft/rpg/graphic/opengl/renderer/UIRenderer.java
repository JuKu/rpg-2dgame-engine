package com.jukusoft.rpg.graphic.opengl.renderer;

import com.jukusoft.rpg.core.logger.GameLogger;
import com.jukusoft.rpg.core.math.Matrix4f;
import com.jukusoft.rpg.core.math.RandomUtils;
import com.jukusoft.rpg.core.math.Vector3f;
import com.jukusoft.rpg.core.utils.FileUtils;
import com.jukusoft.rpg.graphic.animation.Animable;
import com.jukusoft.rpg.graphic.exception.OpenGLShaderException;
import com.jukusoft.rpg.graphic.lighting.Light2D;
import com.jukusoft.rpg.graphic.math.TransformationUtils;
import com.jukusoft.rpg.graphic.opengl.buffer.FrameBufferObject;
import com.jukusoft.rpg.graphic.opengl.image.OpenGL2DImage;
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
    protected AtomicBoolean lightingInitialized = new AtomicBoolean(true);

    protected AtomicBoolean lightingEnabled = new AtomicBoolean(true);

    /**
    * ambient light params
    */
    public volatile float ambientIntensity = 0.7f;
    public final Vector3f ambientColor = new Vector3f(0.3f, 0.3f, 0.7f);

    protected FrameBufferObject lightingFBO = null;
    protected OpenGLShaderProgram lightMapShaderPrgrogram = null;

    protected float zAngle = 0;

    public static final float PI2 = 3.1415926535897932384626433832795f * 2.0f;

    public static final List<Light2D> EMPTY_LIGHTS_LIST = new ArrayList<>();

    /**
    * http://www.alcove-games.com/opengl-es-2-tutorials/lightmap-shader-fire-effect-glsl/
    */

    /**
    * default constructor
    */
    public UIRenderer (final String vertexShaderPath, final String fragmentShaderPath, final String lightingVertexShader, final String lightingFragmentShader, final boolean lightingInitialized) throws IOException, OpenGLShaderException {
        this.lightingInitialized.set(lightingInitialized);

        try {
            this.init(vertexShaderPath, fragmentShaderPath, lightingVertexShader, lightingFragmentShader);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * default constructor
     */
    public UIRenderer (final String vertexShaderPath, final String fragmentShaderPath, final boolean lightingInitialized) throws IOException, OpenGLShaderException {
        this.lightingInitialized.set(lightingInitialized);

        try {
            this.init(vertexShaderPath, fragmentShaderPath, "./data/shader/lighting_fbo_vertex.vs", "./data/shader/lighting_fbo_fragment.fs");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * default constructor
     */
    public UIRenderer (final String vertexShaderPath, final String fragmentShaderPath) throws IOException, OpenGLShaderException {
        this(vertexShaderPath, fragmentShaderPath, false);
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
    public void init (final String vertexShaderPath, final String fragmentShaderPath, final String lightingVertexShader, final String lightingFragmentShader) throws Exception {
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

        //https://www.opengl.org/wiki/Texture

        if (lightingInitialized.get()) {
            //create uniforms for lighting
            this.uiShaderProgram.createUniform("ambientColor");

            //create uniform for resolution
            this.uiShaderProgram.createUniform("resolution");
        }

        /**
        * initlialize lighting map shader
        */
        this.lightMapShaderPrgrogram = new OpenGLShaderProgram();

        //add vertex shader
        this.lightMapShaderPrgrogram.setVertexShader(FileUtils.readFile(lightingVertexShader, StandardCharsets.UTF_8));//.setVertexShader(FileUtils.readFile(vertexShaderPath, StandardCharsets.UTF_8));

        //add fragment shader
        this.lightMapShaderPrgrogram.setFragmentShader(FileUtils.readFile(lightingFragmentShader, StandardCharsets.UTF_8));

        //compile shader program
        this.lightMapShaderPrgrogram.link();

        //create uniforms for ortographic model projection matrix and base color
        this.lightMapShaderPrgrogram.createUniform("projModelMatrix");
        this.lightMapShaderPrgrogram.createUniform("colour");
        this.lightMapShaderPrgrogram.createUniform("hasTexture");

        //create uniforms for lighting
        this.lightMapShaderPrgrogram.createUniform("ambientColor");

        this.isInitialized.set(true);
    }

    public void render (int windowWidth, int windowHeight, List<Renderable> drawableObjectsList) {
        render(windowWidth, windowHeight, drawableObjectsList, EMPTY_LIGHTS_LIST);
    }

    /**
    * render UI
    */
    public void render (int windowWidth, int windowHeight, List<Renderable> drawableObjectsList, List<Light2D> lights) {
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

        //check, if we can used cached ortho matrix
        if (wasReized(windowWidth, windowHeight)) {
            //invalidate old ortho matrix and generate an new one, use same destination matrix, so we dont need to create an new matrix instance
            this.cachedOrthoMatrix = TransformationUtils.getOrthoProjectionMatrix(0, windowWidth, windowHeight, 0, this.cachedOrthoMatrix);

            GameLogger.debug("UIRenderer", "invalidate cached ortho matrix and generate an new one, because window was resized.");
        }

        //get ortho matrix from cached ortho matrix
        Matrix4f ortho = this.cachedOrthoMatrix;

        if (this.lightingEnabled.get()) {
            this.renderLightsFBO(windowWidth, windowHeight, ortho, lights);
        }

        uiShaderProgram.bind();

        if (lightingInitialized.get()) {
            uiShaderProgram.setUniformf("resolution", windowWidth, windowHeight);
        }

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

            if (lightingInitialized.get()) {
                if (lightingEnabled.get()) {
                    uiShaderProgram.setUniformf("ambientColor", ambientColor.getX(), ambientColor.getY(), ambientColor.getZ(), ambientIntensity);
                } else {
                    uiShaderProgram.setUniformf("ambientColor", 1f, 1f, 1f, 1f);
                }
            } else {
                //GameLogger.debug("UIRenderer", "lighting isnt enabled.");
            }

            if (lightingEnabled.get()) {
                //render mesh with lightmap
                obj.render(this.lightingFBO);
            } else {
                //render mesh
                obj.render(null);
            }

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

    protected void renderLightsFBO(int width, int height, Matrix4f ortho, List<Light2D> lights) {
        //bind lightmap shader program
        this.lightMapShaderPrgrogram.bind();

        if (this.lightingFBO == null) {
            //initialize lighting framebuffer
            this.lightingFBO = new FrameBufferObject(width, height);
        }

        //bind framebuffer, so we draw to framebuffer
        this.lightingFBO.bind();

        float dt = 1000 / 60;
        float zSpeed = 15.0f;

        zAngle += dt * zSpeed;
        while(zAngle > PI2)
            zAngle -= PI2;

        //iterate through all lights
        for (Light2D light : lights) {
            float lightSize = light.isLightOscillateEnabled() ? (4.75f + 0.25f * (float) Math.sin(zAngle) + .2f * RandomUtils.random()) : 5.0f;

            //calculate projection model matrix
            final Matrix4f projModelMatrix = TransformationUtils.getOrtoProjModelMatrix(light, ortho, this.cachedModelMatrix, this.cachedProjModelMatrix);

            uiShaderProgram.setUniform("projModelMatrix", projModelMatrix);
            uiShaderProgram.setUniform("colour", light.getMaterial().getColor());
            uiShaderProgram.setUniform("hasTexture", light.getMaterial().isTextured() ? 1 : 0);

            if (lightingEnabled.get()) {
                uiShaderProgram.setUniformf("ambientColor", 1f, 1f, 1f, 1f);
            }

            //draw light texture
            light.renderLight(width, height);
        }

        //unbind framebuffer
        this.lightingFBO.unbind();

        //unbind lightmap shader program
        this.lightMapShaderPrgrogram.unbind();
    }

    public float getAmbientIntensity () {
        return this.ambientIntensity;
    }

    public void setAmbientIntensity (float ambientIntensity) {
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

    public void enableLighting () {
        if (!lightingInitialized.get()) {
            throw new IllegalStateException("You cannot enable lighting, because lighting wasnt initialized. Allow lighting in constructor.");
        }

        this.lightingEnabled.set(true);
    }

    public void disableLighting () {
        this.lightingEnabled.set(false);
    }

}
