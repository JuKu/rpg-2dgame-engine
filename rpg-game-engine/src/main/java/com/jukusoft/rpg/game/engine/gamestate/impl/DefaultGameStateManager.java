package com.jukusoft.rpg.game.engine.gamestate.impl;

import com.jukusoft.rpg.game.engine.app.GameApp;
import com.jukusoft.rpg.game.engine.gamestate.GameState;

/**
 * Created by Justin on 17.08.2016.
 */
public class DefaultGameStateManager<T extends GameState> extends BasicGameStateManager<T> {

    public DefaultGameStateManager(GameApp app) {
        super(app);
    }

    public void render () {
        //iterate through active game states
        for (T state : this.iteratorQueue) {
            //render game state
            state.render(this.app);
        }
    }

    public void processInput () {
        //iterate through active game states
        for (T state : this.iteratorQueue) {
            //process input events on game state
            state.processInputEvents();
        }
    }

    public void update (double delta) {
        //iterate through active game states
        for (T state : this.iteratorQueue) {
            //update game state
            state.update(this.app, delta);
        }
    }

    public void onResized (int width, int height) {
        //iterate through active game states
        for (T state : this.iteratorQueue) {
            //update game state
            state.onResized(width, height);
        }
    }

}
