package com.jukusoft.rpg.game.engine.gamestate.impl;

import com.jukusoft.rpg.game.engine.app.GameApp;
import com.jukusoft.rpg.game.engine.exception.GameStateNotFoundException;
import com.jukusoft.rpg.game.engine.gamestate.GameState;
import com.jukusoft.rpg.game.engine.gamestate.GameStateManager;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Justin on 17.08.2016.
 */
public class BasicGameStateManager<T extends GameState> implements GameStateManager<T> {

    /**
    * map with all game states
    */
    protected Map<String,T> states = new ConcurrentHashMap<>();

    /**
    * stack with activate game states
     *
     * We dont use an Deque, because we want to iterate from lowest to hight
    */
    protected Deque<T> activeGameStates = new ConcurrentLinkedDeque<>();

    /**
    * stack with active game states
     *
     * alias to activeGameStates, but optimized for iteration
    */
    protected Queue<T> iteratorQueue = new ConcurrentLinkedQueue<>();

    /**
    * instance of game application
    */
    protected GameApp app = null;

    public BasicGameStateManager (GameApp app) {
        this.app = app;
    }

    @Override
    public void addGameState(String name, T gameState) {
        //create game state
        gameState.init(this, this.app);

        //add game state to map
        this.states.put(name, gameState);

        Logger.getRootLogger().debug("game state " + name + " added successfully.");
    }

    @Override
    public void removeGameState(String name) {
        //check, if game state exists
        GameState state = this.states.get(name);

        if (state != null) {
            //game state exists

            //remove game state
            this.states.remove(name);

            //shutdown game state
            state.shutdown();
        }
    }

    @Override
    public void removeAllGameStates() {
        //leave all game states first
        this.leaveAllGameStates();

        //iterate through all registered game states
        for (Map.Entry<String,T> entry : this.states.entrySet()) {
            //shutdown game state
            entry.getValue().shutdown();
        }

        //clear map
        states.clear();
    }

    @Override
    public void leaveAndEnterGameState(String name) throws GameStateNotFoundException {
        //leave all game states
        this.leaveAllGameStates();

        //push new game state
        this.pushGameState(name);
    }

    @Override
    public void leaveAllGameStates() {
        while (!this.activeGameStates.isEmpty()) {
            //get and remove element on top of active game states stack
            GameState state = this.activeGameStates.pop();

            //pause game state
            state.onPause();
        }

        //clear optimized queue for iteration
        this.iteratorQueue.clear();
    }

    @Override
    public void pushGameState(String name) throws GameStateNotFoundException {
        //find game state
        T state = this.states.get(name);

        if (state == null) {
            throw new GameStateNotFoundException("Couldnt find game state " + name + ", game state cannot pushed, because it wasnt registered before.");
        }

        Logger.getRootLogger().info("push gamestate: " + name);

        //start game state
        state.onStart(this, this.app);

        //push game state to active game state stack
        this.activeGameStates.push(state);

        //push game state to iterator optimized game state stack
        this.iteratorQueue.add(state);
    }

    @Override
    public T pop() {
        T state = this.activeGameStates.pop();

        if (state != null) {
            //pause game state
            state.onPause();
        }

        //remove state from iterator optimized game state stack
        this.iteratorQueue.remove(state);

        return state;
    }

    @Override
    public List<T> listActiveGameStates() {
        //create new list
        List<T> list = new ArrayList<T>();

        //iterate through active game states
        for (T state : this.activeGameStates) {
            //add state to list
            list.add(state);
        }

        //return list
        return list;
    }

    @Override
    public int countRegisteredGameStates() {
        return this.states.size();
    }

    @Override
    public int countActiveGameStates() {
        return this.activeGameStates.size();
    }

    @Override
    public boolean hasActiveGameState() {
        return this.activeGameStates.size() > 0;
    }

    @Override
    public Iterator<T> activeGameStatesIterator () {
        return this.iteratorQueue.iterator();
    }

}
