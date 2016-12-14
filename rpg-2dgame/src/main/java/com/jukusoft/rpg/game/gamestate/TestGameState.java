package com.jukusoft.rpg.game.gamestate;

import com.jukusoft.rpg.core.logger.GameLogger;
import com.jukusoft.rpg.core.math.Vector3f;
import com.jukusoft.rpg.game.engine.app.GameApp;
import com.jukusoft.rpg.game.engine.gamestate.GameState;
import com.jukusoft.rpg.game.engine.gamestate.GameStateManager;
import com.jukusoft.rpg.game.engine.gamestate.impl.BasicGameState;
import com.jukusoft.rpg.game.engine.resource.ResourceManager;
import com.jukusoft.rpg.graphic.animation.BasicAnimation;
import com.jukusoft.rpg.graphic.animation.Frame;
import com.jukusoft.rpg.graphic.camera.Camera2D;
import com.jukusoft.rpg.graphic.camera.Camera3D;
import com.jukusoft.rpg.graphic.camera.DefaultCamera;
import com.jukusoft.rpg.graphic.exception.OpenGLShaderException;
import com.jukusoft.rpg.graphic.lighting.LightingScene;
import com.jukusoft.rpg.graphic.opengl.image.OpenGL2DImage;
import com.jukusoft.rpg.graphic.opengl.renderer.SceneRenderer;
import com.jukusoft.rpg.graphic.opengl.renderer.UIRenderer;
import com.jukusoft.rpg.graphic.opengl.texture.OpenGL2DTexture;
import com.jukusoft.rpg.graphic.renderer.Renderable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Justin on 14.12.2016.
 */
public class TestGameState extends BasicGameState {

    protected SceneRenderer sceneRenderer = null;

    protected List<Renderable> drawableObjects = new ArrayList<>();

    protected OpenGL2DImage bgImage = null;

    protected BasicAnimation campfire = null;

    /**
    * current camera
    */
    protected Camera3D camera = new DefaultCamera();

    /**
    * scene with all lights
    */
    protected LightingScene lightingScene = new LightingScene();

    @Override
    public <T extends GameState> void onInit(GameStateManager<T> gameStateManager, GameApp app) {
        GameLogger.info("TestGameState", "TestGameState::onInit().");

        //log
        GameLogger.info("TestGameState", "create new Scene Renderer for LoadingGameState.");

        //create new scene renderer
        this.sceneRenderer = new SceneRenderer("./data/shader/scene_vertex.vs", "./data/shader/scene_fragment.fs");

        //log
        GameLogger.info("TestGameState", "Scene Renderer created successfully.");

        //load background texture & image
        OpenGL2DTexture bgTexture = ResourceManager.getInstance().getTexture("thirdparty/wallpaper/ocean/Ocean_large.png");
        this.bgImage = new OpenGL2DImage(0, 0, bgTexture);

        //load campfire texture
        OpenGL2DTexture campfireTexture = ResourceManager.getInstance().getTexture("spritesheets/campfire/CampFireFinished.png");

        //create new campfire animation
        this.campfire = new BasicAnimation(200, 500, campfireTexture);

        //add frames to animation
        for (int i = 0; i < 5; i++) {
            this.campfire.addFrame(new Frame(i * 64, 0, 64, 64, 150));
        }

        this.drawableObjects.add(bgImage);
        this.drawableObjects.add(this.campfire);

        //setup lightings
        this.lightingScene.setAmbientLight(new Vector3f(0.3f, 0.3f, 0.3f));
    }

    @Override
    public void render(GameApp app) {
        this.sceneRenderer.renderScene(getWindow().getWidth(), getWindow().getHeight(), this.camera, this.lightingScene, this.drawableObjects);
    }

    @Override
    public void update(GameApp app, double delta) {
        //
    }

}
