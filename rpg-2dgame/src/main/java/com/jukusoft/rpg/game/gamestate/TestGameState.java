package com.jukusoft.rpg.game.gamestate;

import com.jukusoft.rpg.core.logger.GameLogger;
import com.jukusoft.rpg.game.engine.app.GameApp;
import com.jukusoft.rpg.game.engine.gamestate.GameState;
import com.jukusoft.rpg.game.engine.gamestate.GameStateManager;
import com.jukusoft.rpg.game.engine.gamestate.impl.BasicGameState;
import com.jukusoft.rpg.game.engine.resource.ResourceManager;
import com.jukusoft.rpg.graphic.animation.BasicAnimation;
import com.jukusoft.rpg.graphic.animation.Frame;
import com.jukusoft.rpg.graphic.exception.OpenGLShaderException;
import com.jukusoft.rpg.graphic.opengl.image.OpenGL2DImage;
import com.jukusoft.rpg.graphic.opengl.renderer.SceneRenderer;
import com.jukusoft.rpg.graphic.opengl.renderer.UIRenderer;
import com.jukusoft.rpg.graphic.opengl.texture.OpenGL2DTexture;

import java.io.IOException;

/**
 * Created by Justin on 14.12.2016.
 */
public class TestGameState extends BasicGameState {

    protected SceneRenderer sceneRenderer = null;

    @Override
    public <T extends GameState> void onInit(GameStateManager<T> gameStateManager, GameApp app) {
        //
    }

    @Override
    public void render(GameApp app) {
        //
    }

    @Override
    public void update(GameApp app, double delta) {
        //
    }

}
