package com.jukusoft.rpg.game.gamestate;

import com.jukusoft.rpg.game.engine.app.GameApp;
import com.jukusoft.rpg.game.engine.gamestate.GameState;
import com.jukusoft.rpg.game.engine.gamestate.GameStateManager;
import com.jukusoft.rpg.game.engine.gamestate.impl.BasicGameState;
import com.jukusoft.rpg.game.engine.logger.GameLogger;
import com.jukusoft.rpg.game.engine.resource.ResourceManager;
import com.jukusoft.rpg.graphic.opengl.font.FontTexture;
import org.apache.log4j.Logger;

import java.awt.*;

/**
 * Created by Justin on 29.11.2016.
 */
public class IntroGameState extends BasicGameState {

    @Override
    public <T extends GameState> void onInit(GameStateManager<T> gameStateManager, GameApp app) {
        GameLogger.info("IntroGameState", "IntroGameState::onInit().");

        //load texture
        FontTexture texture = ResourceManager.getInstance().getFontTexture(new Font("Arial", Font.PLAIN, 20), "ISO-8859-1", Color.WHITE);
    }

    @Override
    public void update(GameApp app, double delta) {
        //update
    }

    @Override
    public void render (GameApp app) {
        //clear window
        getWindow().clear();

        //check, if window was resized
        if (getWindow().wasResized()) {
            //reset viewport
            getWindow().setViewPort(0, 0, getWindow().getWidth(), getWindow().getHeight());

            //reset resized flag
            getWindow().setResizedFlag(false);
        }
    }

}
