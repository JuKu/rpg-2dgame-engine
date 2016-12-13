package com.jukusoft.rpg.graphic.opengl.renderer;

import com.jukusoft.rpg.core.utils.FileUtils;
import com.jukusoft.rpg.graphic.camera.ReadonlyCamera;
import com.jukusoft.rpg.graphic.lighting.LightingScene;
import com.jukusoft.rpg.graphic.opengl.shader.OpenGLShaderProgram;
import com.jukusoft.rpg.graphic.renderer.Renderable;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.jukusoft.rpg.graphic.lighting.LightingScene.MAX_NUMBER_OF_POINT_LIGHTS;
import static com.jukusoft.rpg.graphic.lighting.LightingScene.MAX_NUMBER_OF_SPOT_LIGHTS;

/**
 * Created by Justin on 13.12.2016.
 */
public class SceneRenderer {

    /**
     * flag, if renderer was initialized
     */
    protected AtomicBoolean isInitialized = new AtomicBoolean(false);

    /**
     * shader program for UI
     */
    protected OpenGLShaderProgram sceneShaderProgram = null;

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
        //
    }

}
