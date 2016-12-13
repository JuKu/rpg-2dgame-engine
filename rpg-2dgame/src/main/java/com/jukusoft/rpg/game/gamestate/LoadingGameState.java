package com.jukusoft.rpg.game.gamestate;

import com.jukusoft.rpg.core.logger.GameLogger;
import com.jukusoft.rpg.core.utils.GamePlatform;
import com.jukusoft.rpg.game.engine.app.GameApp;
import com.jukusoft.rpg.game.engine.exception.GameStateNotFoundException;
import com.jukusoft.rpg.game.engine.gamestate.GameState;
import com.jukusoft.rpg.game.engine.gamestate.GameStateManager;
import com.jukusoft.rpg.game.engine.gamestate.impl.BasicGameState;
import com.jukusoft.rpg.game.engine.resource.ResourceManager;
import com.jukusoft.rpg.graphic.animation.*;
import com.jukusoft.rpg.graphic.exception.OpenGLShaderException;
import com.jukusoft.rpg.graphic.opengl.font.FontTexture;
import com.jukusoft.rpg.graphic.opengl.image.OpenGL2DImage;
import com.jukusoft.rpg.graphic.opengl.renderer.Renderable;
import com.jukusoft.rpg.graphic.opengl.renderer.UIRenderer;
import com.jukusoft.rpg.graphic.opengl.text.OpenGLText;
import com.jukusoft.rpg.graphic.opengl.texture.OpenGL2DTexture;

import java.awt.*;
import java.io.IOException;
import java.util.*;

/**
 * Created by Justin on 13.12.2016.
 */
public class LoadingGameState extends BasicGameState {

    protected UIRenderer uiRenderer = null;

    protected java.util.List<Renderable> drawableObjects = new ArrayList<>();

    protected OpenGL2DTexture bgTexture = null;
    protected OpenGL2DTexture bgTexture2 = null;
    protected OpenGL2DImage image = null;

    protected long lastUpdate = System.currentTimeMillis();
    protected int imageIndex = 0;

    @Override
    public <T extends GameState> void onInit(GameStateManager<T> gameStateManager, GameApp app) {
        GameLogger.info("IntroGameState", "IntroGameState::onInit().");

        //create new UI renderer
        GameLogger.info("IntroGameState", "create new UI Renderer for IntroGameState.");

        try {
            this.uiRenderer = new UIRenderer("./data/shader/hud_vertex.vs", "./data/shader/hud_fragment.fs");
            this.uiRenderer.disableRedrawSameMeshOptimization();
        } catch (IOException e) {
            e.printStackTrace();
            throw new OpenGLShaderException("IOException while loading UI Renderer: " + e.getLocalizedMessage());
        }

        //load textures
        this.bgTexture = ResourceManager.getInstance().getTexture("thirdparty/wallpaper/ocean/Ocean_large.png");
        this.bgTexture2 = ResourceManager.getInstance().getTexture("thirdparty/wallpaper/starry_night/starry_night.png");

        this.image = new OpenGL2DImage(0, 0, bgTexture);

        //this.drawableObjects.add(text);
        this.drawableObjects.add(image);
    }

    @Override
    public void update(GameApp app, double delta) {
        //
    }

    @Override
    public void render (GameApp app) {
        //check, if window was resized
        if (getWindow().wasResized()) {
            //reset viewport
            getWindow().setViewPort(0, 0, getWindow().getWidth(), getWindow().getHeight());

            //reset resized flag
            getWindow().setResizedFlag(false);
        }

        //render UI
        this.uiRenderer.render(getWindow().getWidth(), getWindow().getHeight(), this.drawableObjects);
    }

}
