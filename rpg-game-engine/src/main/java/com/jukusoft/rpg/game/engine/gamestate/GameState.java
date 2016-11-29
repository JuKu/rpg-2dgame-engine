package com.jukusoft.rpg.game.engine.gamestate;

import com.jukusoft.rpg.game.engine.app.GameApp;

/**
 * Created by Justin on 17.08.2016.
 */
public interface GameState {

    /**
    * create and initialize game state
     *
     * will be called, if game state will be created
    */
    public <T extends GameState> void init(GameStateManager<T> gameStateManager, GameApp app);

    /**
     * shutdown game state
     */
    public void shutdown();

    /**
    * pause game state and switch to another game state
    */
    public void onPause();

    /**
    * switch to this game state and start
    */
    public <T extends GameState> void onStart(GameStateManager<T> gameStateManager, GameApp app);

    /**
    * will be called if window was resized
     *
     * @param width new width of window
     * @param height new height of window
    */
    public void onResized(int width, int height);

    /**
    * render game state
    */
    public void render(GameApp app);

    /**
    * process input events
    */
    public void processInputEvents();

    /**
    * update game state
    */
    public void update(GameApp app, double delta);

}
