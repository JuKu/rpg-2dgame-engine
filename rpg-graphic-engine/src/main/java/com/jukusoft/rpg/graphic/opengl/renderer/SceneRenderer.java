package com.jukusoft.rpg.graphic.opengl.renderer;

import com.jukusoft.rpg.core.logger.GameLogger;
import com.jukusoft.rpg.core.math.Matrix4f;
import com.jukusoft.rpg.core.utils.FileUtils;
import com.jukusoft.rpg.graphic.animation.Animable;
import com.jukusoft.rpg.graphic.camera.ReadonlyCamera;
import com.jukusoft.rpg.graphic.lighting.LightingScene;
import com.jukusoft.rpg.graphic.math.TransformationUtils;
import com.jukusoft.rpg.graphic.opengl.shader.OpenGLShaderProgram;
import com.jukusoft.rpg.graphic.renderer.Renderable;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.jukusoft.rpg.graphic.lighting.LightingScene.MAX_NUMBER_OF_POINT_LIGHTS;
import static com.jukusoft.rpg.graphic.lighting.LightingScene.MAX_NUMBER_OF_SPOT_LIGHTS;
import static org.lwjgl.opengl.GL11.GL_LIGHTING;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glEnable;

/**
 * Created by Justin on 13.12.2016.
 */
public class SceneRenderer {

    /**
     * field of view in radians
     */
    private static final float FOV = (float) Math.toRadians(60.0f);

    private static final float Z_NEAR = 0.01f;

    private static final float Z_FAR = 1000.f;

    private final float specularPower = 10f;

    /**
     * flag, if renderer was initialized
     */
    protected AtomicBoolean isInitialized = new AtomicBoolean(false);

    /**
     * shader program for UI
     */
    protected OpenGLShaderProgram sceneShaderProgram = null;

    /**
     * cached window size, so we can detect, if ortho matrix has to be calculated again or we can used cached matrix
     */
    protected int lastWindowWidth = 0;
    protected int lastWindowHeight = 0;

    /**
     * cached projection matrix, so we donnt need to create an new matrix on each renderer loop
     */
    protected Matrix4f cachedProjMatrix = new Matrix4f();

    /**
    * cached view matrix
    */
    protected Matrix4f cachedViewMatrix = new Matrix4f();

    /**
    * cached projection model matrix
    */
    protected Matrix4f cachedModelViewMatrix = new Matrix4f();

    /**
    * temporary matrix
    */
    protected Matrix4f tmpMatrix = new Matrix4f();

    public SceneRenderer (final String vertexShaderPath, final String fragmentShaderPath) {
        try {
            this.init(vertexShaderPath, fragmentShaderPath);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void init (final String vertexShaderPath, final String fragmentShaderPath) throws Exception {
        if (isInitialized.get()) {
            throw new IllegalStateException("UIRenderer was already initialized.");
        }

        //create new shader program
        this.sceneShaderProgram = new OpenGLShaderProgram();

        //add vertex shader
        this.sceneShaderProgram.setVertexShader(FileUtils.readFile(vertexShaderPath, StandardCharsets.UTF_8));

        //add fragment shader
        this.sceneShaderProgram.setFragmentShader(FileUtils.readFile(fragmentShaderPath, StandardCharsets.UTF_8));

        //compile shader program
        this.sceneShaderProgram.link();

        //create all neccessary uniforms for modelView, projectionMatrix and texture
        sceneShaderProgram.createUniform("projectionMatrix");
        sceneShaderProgram.createUniform("modelViewMatrix");
        sceneShaderProgram.createUniform("texture_sampler");

        //create uniform for mesh material
        sceneShaderProgram.createMaterialUniform("material");

        //create uniforms for lighting
        sceneShaderProgram.createUniform("specularPower");
        sceneShaderProgram.createUniform("ambientLight");
        sceneShaderProgram.createPointLightUniforms("pointLights", MAX_NUMBER_OF_POINT_LIGHTS);
        sceneShaderProgram.createSpotLightUniforms("spotLights", MAX_NUMBER_OF_SPOT_LIGHTS);
        sceneShaderProgram.createDirectionalLightUniform("directionalLight");

        this.isInitialized.set(true);
    }

    public void renderScene (int windowWidth, int windowHeight, ReadonlyCamera camera, LightingScene lightsScene, List<Renderable> drawableObjectsList) {
        if (!this.isInitialized.get()) {
            throw new IllegalStateException("Cannot render scene, because SceneRenderer wasnt initialized yet, initialize renderer with init() first.");
        }

        //bind shader program
        this.sceneShaderProgram.bind();

        //check, if camera was changed, or if we can use cached view matrix
        if (camera.wasChanged()) {
            //invalide cached view matrix and generate an new one
            this.cachedViewMatrix = TransformationUtils.getCameraViewMatrix(camera, this.cachedViewMatrix);

            GameLogger.debug("SceneRenderer", "invalidate cached view matrix and generate an new one, because camera was changed.");

            //reset changed state
            camera.resetChangedState();
        }

        //check, if window was resized, than we have to generate an new projection matrix
        if (this.wasReized(windowWidth, windowHeight)) {
            //invalidate projection matrix and generates an new ones, because window was resized
            this.cachedProjMatrix = TransformationUtils.getProjectionMatrix(FOV, windowWidth, windowHeight, Z_NEAR, Z_FAR, this.cachedProjMatrix);

            GameLogger.debug("SceneRenderer", "invalidate cached projection matrix and generate an new one, because window was resized.");
        }

        //set projection matrix
        sceneShaderProgram.setUniform("projectionMatrix", this.cachedProjMatrix);

        //set light uniforms
        this.setLights(this.cachedViewMatrix, lightsScene);

        sceneShaderProgram.setUniform("texture_sampler", 0);

        //get current timestamp
        final long currentTime = System.currentTimeMillis();

        //iterate through all drawable objects
        for (Renderable obj : drawableObjectsList) {
            //check, if object is an animation
            if (obj instanceof Animable) {
                Animable animation = (Animable) obj;

                //update frame if neccessary
                animation.updateFrame(currentTime);
            }

            //calculate model view matrix for this item
            this.cachedModelViewMatrix = TransformationUtils.getModelViewMatrix(obj, this.tmpMatrix, this.cachedViewMatrix, this.cachedModelViewMatrix);

            //set model view matrix uniform
            sceneShaderProgram.setUniform("modelViewMatrix", this.cachedModelViewMatrix);

            //set uniforms for material
            sceneShaderProgram.setUniform("material", obj.getMaterial());

            //render mesh
            obj.render();
        }

        //unbind shader program
        this.sceneShaderProgram.unbind();
    }

    protected void setLights (Matrix4f viewMatrix, LightingScene lightScene) {
        //set uniform for ambient light
        sceneShaderProgram.setUniform("ambientLight", lightScene.getAmbientLight());

        //set specular power
        sceneShaderProgram.setUniform("specularPower", specularPower);
    }

    protected boolean wasReized (final int windowWidth, final int windowHeight) {
        //check, if window was resized
        final boolean wasResized = this.lastWindowWidth != windowWidth || this.lastWindowHeight != windowHeight;

        //cache last window width & height
        this.lastWindowWidth = windowWidth;
        this.lastWindowHeight = windowHeight;

        return wasResized;
    }

}
