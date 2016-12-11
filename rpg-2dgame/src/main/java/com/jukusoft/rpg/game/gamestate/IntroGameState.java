package com.jukusoft.rpg.game.gamestate;

import com.jukusoft.rpg.game.engine.app.GameApp;
import com.jukusoft.rpg.game.engine.gamestate.GameState;
import com.jukusoft.rpg.game.engine.gamestate.GameStateManager;
import com.jukusoft.rpg.game.engine.gamestate.impl.BasicGameState;
import com.jukusoft.rpg.core.logger.GameLogger;
import com.jukusoft.rpg.game.engine.resource.ResourceManager;
import com.jukusoft.rpg.core.utils.GamePlatform;
import com.jukusoft.rpg.graphic.exception.OpenGLShaderException;
import com.jukusoft.rpg.graphic.opengl.font.FontTexture;
import com.jukusoft.rpg.graphic.opengl.image.OpenGL2DImage;
import com.jukusoft.rpg.graphic.opengl.image.OpenGL2DTextureRegion;
import com.jukusoft.rpg.graphic.opengl.mesh.DrawableObject;
import com.jukusoft.rpg.graphic.opengl.renderer.UIRenderer;
import com.jukusoft.rpg.graphic.opengl.text.OpenGLText;
import com.jukusoft.rpg.graphic.opengl.texture.OpenGL2DTexture;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Justin on 29.11.2016.
 */
public class IntroGameState extends BasicGameState {

    protected UIRenderer uiRenderer = null;

    protected List<DrawableObject> drawableObjects = new ArrayList<>();

    protected OpenGLText text = null;
    protected OpenGL2DImage image = null;
    protected OpenGL2DTextureRegion campfire = null;

    protected long lastAnimationTime = 0l;
    protected long animationInterval = 150l;
    protected int animFrameCount = 0;

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

        //get font texture
        FontTexture fontTexture = ResourceManager.getInstance().getFontTexture(new Font("Arial", Font.PLAIN, 20), "ISO-8859-1", Color.WHITE);

        //add text to UI
        text = new OpenGLText(10f, getWindow().getHeight() - 50, "DEMO", fontTexture);
        text.setPosition(20, getWindow().getHeight() - 50);

        text.setText("JuKuSoft - Draw Text Example");

        //load textures
        OpenGL2DTexture texture = ResourceManager.getInstance().getTexture("intro/intro_screen.png");
        OpenGL2DTexture campfireTexture = ResourceManager.getInstance().getTexture("spritesheets/campfire/CampFireFinished.png");

        this.image = new OpenGL2DImage(0, 0, texture);
        this.campfire = new OpenGL2DTextureRegion(200, 200, 0, 0, 64, 64, campfireTexture);

        //this.drawableObjects.add(text);
        this.drawableObjects.add(image);
        this.drawableObjects.add(text);
        this.drawableObjects.add(campfire);
    }

    @Override
    public void update(GameApp app, double delta) {
        //update
        this.text.setPosition(text.getPosition().getX() + 1, getWindow().getHeight() - 50);

        if (text.getPosition().getX() > getWindow().getWidth() - 100) {
            text.setPosition(0, getWindow().getHeight() - 50);
        }
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

        if (lastAnimationTime + animationInterval < System.currentTimeMillis()) {
            campfire.setRegion(animFrameCount * 64, 0, 64, 64);

            this.animFrameCount = (this.animFrameCount + 1) % 5;
            this.lastAnimationTime = System.currentTimeMillis();
        }

        //render UI
        this.uiRenderer.render(getWindow().getWidth(), getWindow().getHeight(), this.drawableObjects);
    }

}
