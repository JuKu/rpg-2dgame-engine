package com.jukusoft.rpg.game.engine.gamestate;

import com.jukusoft.rpg.game.engine.exception.GameStateNotFoundException;

import java.util.Iterator;
import java.util.List;

/**
 * Created by Justin on 17.08.2016.
 */
public interface GameStateManager<T extends GameState> {

    /**
     * add new game state
     *
     * @param name name of game state
     * @param gameState instance of game state
     */
    public void addGameState(String name, T gameState);

    /**
     * remove game state
     *
     * @param name name of game state
     */
    public void removeGameState(String name);

    public void removeAllGameStates();

    /**
     * leaves all current game states and enter new game state
     */
    public void leaveAndEnterGameState(String name) throws GameStateNotFoundException;

    /**
     * leave all game states
     */
    public void leaveAllGameStates();

    /**
     * push new game state on focus
     */
    public void pushGameState(String name) throws GameStateNotFoundException;

    /**
     * pop game state on top of stack
     */
    public T pop();

    /**
    * list all active game states
    */
    public List<T> listActiveGameStates();

    /**
    * get iterator of active game states
    */
    public Iterator<T> activeGameStatesIterator();

    /**
    * count all registered game states
     *
     * @return number of registered game states
    */
    public int countRegisteredGameStates();

    /**
     * count all active game states
     *
     * @return number of active game states
     */
    public int countActiveGameStates();

    /**
    * returns true, if there are one or more active game states, else load screen should be shown
     *
     * @return true, if there are one or more active game states
    */
    public boolean hasActiveGameState();

}
