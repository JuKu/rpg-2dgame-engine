package com.jukusoft.rpg.graphic.opengl.renderer;

import com.jukusoft.rpg.graphic.camera.ReadonlyCamera;
import com.jukusoft.rpg.graphic.lighting.LightingScene;
import com.jukusoft.rpg.graphic.opengl.shader.OpenGLShaderProgram;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

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

        this.isInitialized.set(true);
    }

    public void renderScene (int windowWidth, int windowHeight, ReadonlyCamera camera, LightingScene lightsScene, List<Renderable> drawableObjectsList) {
        //
    }

}
