package com.jukusoft.rpg.game.engine.app;

import com.jukusoft.rpg.game.engine.gamestate.GameState;
import com.jukusoft.rpg.game.engine.gamestate.GameStateManager;

/**
 * Created by Justin on 22.08.2016.
 */
public interface GameStateEngine<T extends GameState> {

    /**
     * get instance of game state manager
     */
    public GameStateManager<T> getGameStateManager();

}
