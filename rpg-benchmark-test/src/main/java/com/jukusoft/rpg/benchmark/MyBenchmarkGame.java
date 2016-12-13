package com.jukusoft.rpg.benchmark;

import com.jukusoft.rpg.game.engine.app.SimpleGameApp;
import com.jukusoft.rpg.game.engine.resource.ResourceManager;
import com.jukusoft.rpg.graphic.exception.OpenGLShaderException;
import com.jukusoft.rpg.graphic.opengl.image.OpenGL2DImage;
import com.jukusoft.rpg.graphic.renderer.Renderable;
import com.jukusoft.rpg.graphic.opengl.renderer.UIRenderer;
import com.jukusoft.rpg.graphic.opengl.texture.OpenGL2DTexture;
import com.jukusoft.rpg.window.system.IWindow;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Justin on 12.12.2016.
 */
public class MyBenchmarkGame extends SimpleGameApp {

    protected UIRenderer uiRenderer = null;

    protected List<Renderable> drawableObjects = new ArrayList<>();

    public MyBenchmarkGame (boolean useMultiThreading, int fixedFPS, int fixedUPS, boolean vSync) {
        super(useMultiThreading, fixedFPS, fixedUPS, vSync);
    }

    @Override
    protected void onCreateWindow(IWindow window) {
        //set window size
        window.setSize(1280, 720);

        //set window title
        window.setTitle("2D RPG");

        //set clear color to black
        window.setClearColor(0, 0, 0, 0);

        //centralize window
        window.center();
    }

    @Override
    protected void initialize() {
        try {
            this.uiRenderer = new UIRenderer("./data/shader/hud_vertex.vs", "./data/shader/hud_fragment.fs");
        } catch (IOException e) {
            e.printStackTrace();
            throw new OpenGLShaderException("IOException while loading UI Renderer: " + e.getLocalizedMessage());
        }

        //load textures
        OpenGL2DTexture texture = ResourceManager.getInstance().getTexture("intro/intro_screen.png");
        OpenGL2DTexture texture1 = ResourceManager.getInstance().getTexture("spritesheets/campfire/CampFireFinished.png");

        //create image
        final OpenGL2DImage image = new OpenGL2DImage(0, 0, texture);
        final OpenGL2DImage image2 = new OpenGL2DImage(10, 10, texture1);

        this.uiRenderer.disableRedrawSameMeshOptimization();

        //draw same texture 2000 times every render loop
        for (int i = 0; i < 1000; i++) {
            this.drawableObjects.add(image);
            this.drawableObjects.add(image2);
        }
    }

    @Override
    public void update(double delta) {

    }

    @Override
    public void render() {
        this.uiRenderer.render(getWindow().getWidth(), getWindow().getHeight(), this.drawableObjects);

        System.out.println("FPS: " + getFPS());
    }

}
