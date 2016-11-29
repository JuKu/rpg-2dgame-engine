package com.jukusoft.rpg.game.engine.gamestate.impl;

import com.jukusoft.rpg.game.engine.app.GameApp;
import com.jukusoft.rpg.game.engine.gamestate.GameState;
import com.jukusoft.rpg.game.engine.gamestate.GameStateManager;
import com.jukusoft.rpg.window.system.IWindow;

/**
 * Created by Justin on 17.08.2016.
 */
public class BasicGameState implements GameState {

    /**
    * instance of game application
    */
    protected GameApp app = null;

    @Override
    public final <T extends GameState> void init(GameStateManager<T> gameStateManager, GameApp app) {
        //save instance of game application
        this.app = app;

        this.onInit(gameStateManager, app);
    }

    /**
     * create and initialize game state
     *
     * will be called, if game state will be created
     */
    public <T extends GameState> void onInit(GameStateManager<T> gameStateManager, GameApp app) {
        //
    }

    /**
     * shutdown game state
     */
    @Override
    public void shutdown() {
        this.onShutdown();
    }

    public void onShutdown() {
        //
    }

    @Override
    public void onPause() {

    }

    @Override
    public <T extends GameState> void onStart(GameStateManager<T> gameStateManager, GameApp app) {

    }

    @Override
    public void onResized(int width, int height) {

    }

    @Override
    public void render(GameApp app) {

    }

    @Override
    public void processInputEvents() {

    }

    @Override
    public void update(GameApp app, double delta) {

    }

    /**
    * get instance of window
    */
    protected IWindow getWindow () {
        return this.app.getWindow();
    }

}
