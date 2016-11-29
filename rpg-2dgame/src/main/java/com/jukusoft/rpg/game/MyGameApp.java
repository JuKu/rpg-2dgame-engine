package com.jukusoft.rpg.game;

import com.jukusoft.rpg.game.engine.app.SimpleGameStateApp;
import com.jukusoft.rpg.game.engine.gamestate.GameState;
import com.jukusoft.rpg.game.engine.gamestate.GameStateManager;
import com.jukusoft.rpg.window.system.IWindow;

/**
 * Created by Justin on 29.11.2016.
 */
public class MyGameApp extends SimpleGameStateApp<GameState> {

    public MyGameApp (boolean useMultiThreading, int fixedFPS, int fixedUPS, boolean vSync) {
        super(useMultiThreading, fixedFPS, fixedUPS, vSync);
    }

    @Override
    protected void onInitGame(GameStateManager<GameState> stateManager) {
        //
    }

    @Override
    protected void onCreateWindow(IWindow window) {
        //
    }

}
