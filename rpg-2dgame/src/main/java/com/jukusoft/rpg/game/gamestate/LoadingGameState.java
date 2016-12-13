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
import com.jukusoft.rpg.graphic.animation.Frame;
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
    protected BasicAnimation loading = null;

    protected OpenGL2DImage loadImage = null;

    protected BasicAnimation campfire = null;

    @Override
    public <T extends GameState> void onInit(GameStateManager<T> gameStateManager, GameApp app) {
        GameLogger.info("IntroGameState", "IntroGameState::onInit().");

        //create new UI renderer
        GameLogger.info("IntroGameState", "create new UI Renderer for IntroGameState.");

        try {
            this.uiRenderer = new UIRenderer("./data/shader/hud_vertex.vs", "./data/shader/hud_fragment.fs");
        } catch (IOException e) {
            e.printStackTrace();
            throw new OpenGLShaderException("IOException while loading UI Renderer: " + e.getLocalizedMessage());
        }

        //load textures
        this.bgTexture = ResourceManager.getInstance().getTexture("thirdparty/wallpaper/ocean/Ocean_large.png");
        this.bgTexture2 = ResourceManager.getInstance().getTexture("thirdparty/wallpaper/starry_night/starry_night.png");
        OpenGL2DTexture loadingTexture = ResourceManager.getInstance().getTexture("loadscreen/spritesheet.png");
        OpenGL2DTexture frameTexture = ResourceManager.getInstance().getTexture("loadscreen/frame1.png");
        this.loadImage = new OpenGL2DImage((getWindow().getWidth() / 2) - 200, (getWindow().getHeight() / 2) - 200, frameTexture);

        this.loading = new BasicAnimation((getWindow().getWidth() / 2) - 200, (getWindow().getHeight() / 2) - 200, loadingTexture);

        this.loading.addFrame(new Frame(0, 0, 400, 400, 200));
        this.loading.addFrame(new Frame(400, 0, 400, 400, 200));
        this.loading.addFrame(new Frame(0, 400, 400, 400, 200));
        this.loading.addFrame(new Frame(400, 400, 400, 400, 200));

        this.image = new OpenGL2DImage(0, 0, bgTexture);

        OpenGL2DTexture campfireTexture = ResourceManager.getInstance().getTexture("spritesheets/campfire/CampFireFinished.png");
        //create new campfire animation
        this.campfire = new BasicAnimation(200, 500, campfireTexture);

        //add frames to animation
        for (int i = 0; i < 5; i++) {
            this.campfire.addFrame(new Frame(i * 64, 0, 64, 64, 150));
        }

        //this.drawableObjects.add(text);
        this.drawableObjects.add(image);
        //this.drawableObjects.add(loading);
        this.drawableObjects.add(this.campfire);
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
