package com.jukusoft.rpg.game.gamestate;

import com.jukusoft.rpg.core.asset.Direction;
import com.jukusoft.rpg.core.logger.GameLogger;
import com.jukusoft.rpg.game.engine.app.GameApp;
import com.jukusoft.rpg.game.engine.gamestate.GameState;
import com.jukusoft.rpg.game.engine.gamestate.GameStateManager;
import com.jukusoft.rpg.game.engine.gamestate.impl.BasicGameState;
import com.jukusoft.rpg.game.engine.resource.ResourceManager;
import com.jukusoft.rpg.graphic.animation.*;
import com.jukusoft.rpg.graphic.animation.Frame;
import com.jukusoft.rpg.graphic.exception.OpenGLShaderException;
import com.jukusoft.rpg.graphic.opengl.image.OpenGL2DImage;
import com.jukusoft.rpg.graphic.renderer.Renderable;
import com.jukusoft.rpg.graphic.opengl.renderer.UIRenderer;
import com.jukusoft.rpg.graphic.opengl.texture.OpenGL2DTexture;

import java.io.IOException;
import java.util.*;

import static org.lwjgl.glfw.GLFW.*;

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

    protected volatile float speedX = 0;
    protected volatile float speedY = 0;
    protected volatile float ambientIntensity = 0.7f;

    protected BasicAnimation characterAnimation = null;
    protected Direction lastDirection = Direction.DOWN;

    @Override
    public <T extends GameState> void onInit(GameStateManager<T> gameStateManager, GameApp app) {
        GameLogger.info("LoadingGameState", "LoadingGameState::onInit().");

        //create new UI renderer
        GameLogger.info("LoadingGameState", "create new UI Renderer for IntroGameState.");

        try {
            this.uiRenderer = new UIRenderer("./data/shader/lighting_vertex.vs", "./data/shader/lighting_fragment.fs", true);
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

        OpenGL2DTexture characterTexture = ResourceManager.getInstance().getTexture("spritesheets/pentaquin/Walk.png");
        this.characterAnimation = new BasicAnimation(500, 500, characterTexture);

        //add frames to animation
        for (int i = 0; i < 5; i++) {
            this.characterAnimation.addFrame(new Frame(i * 64, 0, 64, 64, 150));
        }

        //this.drawableObjects.add(text);
        this.drawableObjects.add(image);
        //this.drawableObjects.add(loading);
        this.drawableObjects.add(this.campfire);
        this.drawableObjects.add(this.characterAnimation);
    }

    protected void setAnimation (Direction direction) {
        if (direction == lastDirection) {
            return;
        }

        GameLogger.info("LoadingGameState", "set character direction: " + direction.name());

        int k = 0;

        if (direction == Direction.DOWN) {
            k = 0;
        } else if (direction == Direction.DOWN_LEFT) {
            k = 7;
        } else if (direction == Direction.DOWN_RIGHT) {
            k = 1;
        } else if (direction == Direction.UP) {
            k = 4;
        } else if (direction == Direction.UP_LEFT) {
            k = 5;
        } else if (direction == Direction.UP_RIGHT) {
            k = 3;
        } else if (direction == Direction.LEFT) {
            k = 6;
        } else if (direction == Direction.RIGHT) {
            k = 2;
        }

        this.characterAnimation.clearAllFrames();

        for (int i = 0; i < 16; i++) {
            this.characterAnimation.addFrame(new Frame(i * 160, k * 160, 160, 160, 60));
        }

        this.lastDirection = direction;
    }

    @Override
    public void update(GameApp app, double delta) {
        //
    }

    @Override
    public void render (GameApp app) {
        float newSpeedX = 0;
        float newSpeedY = 0;

        if (getWindow().isKeyPressed(GLFW_KEY_W)) {
            newSpeedY = -1;
        } else if (getWindow().isKeyPressed(GLFW_KEY_S)) {
            newSpeedY = 1;
        }

        if (getWindow().isKeyPressed(GLFW_KEY_A)) {
            newSpeedX = -1;
        } else if (getWindow().isKeyPressed(GLFW_KEY_D)) {
            newSpeedX = 1;
        }

        if (getWindow().isKeyPressed(GLFW_KEY_N)) {
            this.ambientIntensity -= 0.01f;
        } else if (getWindow().isKeyPressed(GLFW_KEY_M)) {
            this.ambientIntensity += 0.01f;
        }

        this.speedX = newSpeedX;
        this.speedY = newSpeedY;

        if (speedX == 0 && speedY == 0) {
            //this.characterAnimation.clearAllFrames();
            this.characterAnimation.addFrame(new Frame(0, 0, 160, 160, 150));
        } else if (speedX == -1 && speedY == 0) {
            this.setAnimation(Direction.LEFT);
        } else if (speedX == -1 && speedY == -1) {
            this.setAnimation(Direction.UP_LEFT);
        } else if (speedX == -1 && speedY == 1) {
            this.setAnimation(Direction.DOWN_LEFT);
        } else if (speedX == 0 && speedY == -1) {
            this.setAnimation(Direction.UP);
        } else if (speedX == 0 && speedY == 1) {
            this.setAnimation(Direction.DOWN);
        } else if (speedX == 1 && speedY == -1) {
            this.setAnimation(Direction.UP_RIGHT);
        } else if (speedX == 1 && speedY == 0) {
            this.setAnimation(Direction.RIGHT);
        } else if (speedX == 1 && speedY == 1) {
            this.setAnimation(Direction.DOWN_RIGHT);
        }

        this.characterAnimation.getPosition().add(speedX, speedY, 0);

        //GameLogger.debug("LoadingGameState", "ambient intensity: " + this.ambientIntensity);
        this.uiRenderer.setAmbientIntensity(this.ambientIntensity);
        this.uiRenderer.setAmbientColor(0.3f, 0.3f, 0.3f);
        //this.uiRenderer.disableLighting();

        GameLogger.debug("LoadingGameState", "FPS: " + getGameApp().getFPS());

        if (getWindow().isKeyPressed(GLFW_KEY_Y)) {
            this.uiRenderer.disableLighting();
        } else if (getWindow().isKeyPressed(GLFW_KEY_X)) {
            this.uiRenderer.enableLighting();
        }

        //render UI
        this.uiRenderer.render(getWindow().getWidth(), getWindow().getHeight(), this.drawableObjects);
    }

}
