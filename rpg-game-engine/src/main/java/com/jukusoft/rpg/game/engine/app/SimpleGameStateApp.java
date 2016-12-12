package com.jukusoft.rpg.game.engine.app;

import com.jukusoft.rpg.game.engine.gamestate.GameState;
import com.jukusoft.rpg.game.engine.gamestate.GameStateManager;
import com.jukusoft.rpg.game.engine.gamestate.impl.DefaultGameStateManager;
import org.apache.log4j.Logger;

/**
 * Created by Justin on 22.08.2016.
 */
public abstract class SimpleGameStateApp<T extends GameState> extends SimpleGameApp implements GameStateEngine<T> {

    /**
    * game state manager
    */
    protected DefaultGameStateManager<T> stateManager = null;

    /**
    * default constructor
     *
     * @param useMultiThreading flag if multi threading should be used or not
    */
    public SimpleGameStateApp(boolean useMultiThreading, int fixedFPS, int fixedUPS, boolean vSync) {
        super(useMultiThreading, fixedFPS, fixedUPS, vSync);

        //create new game state manager
        this.stateManager = new DefaultGameStateManager<>(this);
    }

    public SimpleGameStateApp() {
        //use multi threading, not fixed fps and target updates per second value of 60
        super(true, 0, 60, false);

        //create new game state manager
        this.stateManager = new DefaultGameStateManager<>(this);
    }

    @Override
    public GameStateManager<T> getGameStateManager() {
        return this.stateManager;
    }

    @Override
    protected void initialize() {
        //initialize game states
        this.onInitGame(this.stateManager);
    }

    @Override
    public void update(double delta) {
        //update game states
        this.stateManager.update(delta);
    }

    @Override
    public void render() {
        //check, if window was resized
        if (this.wasResized()) {
            //window was resized

            //reset resized flag
            this.setResizedFlag(false);

            //reset viewport
            this.getWindow().setViewPort(0, 0, this.getWindow().getWidth(), this.getWindow().getHeight());

            //notify game states
            this.stateManager.onResized(getWindow().getWidth(), getWindow().getHeight());
        }

        //render game states
        this.stateManager.render();
    }

    @Override
    protected void beforeRender () {
        //
    }

    @Override
    public void beforeShutdown () {
        Logger.getRootLogger().info("shutdown all game states.");

        //shutdown all game states
        this.stateManager.removeAllGameStates();
    }

    @Override
    public void afterShutdown () {
        //TODO: cleanup
    }

    /**
    * will be called if game is initializing
    */
    protected abstract void onInitGame (GameStateManager<T> stateManager);

}
