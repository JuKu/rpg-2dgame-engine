package com.jukusoft.rpg.game;

import com.jukusoft.rpg.game.engine.app.SimpleGameStateApp;
import com.jukusoft.rpg.game.engine.exception.GameStateNotFoundException;
import com.jukusoft.rpg.game.engine.gamestate.GameState;
import com.jukusoft.rpg.game.engine.gamestate.GameStateManager;
import com.jukusoft.rpg.game.gamestate.IntroGameState;
import com.jukusoft.rpg.game.gamestate.LoadingGameState;
import com.jukusoft.rpg.game.gamestate.MyGameState;
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
        //create and register new intro game state
        IntroGameState intro = new IntroGameState();
        stateManager.addGameState("intro", intro);

        LoadingGameState loadingGameState = new LoadingGameState();
        stateManager.addGameState("loading", loadingGameState);

        MyGameState gameState = new MyGameState();
        stateManager.addGameState("game", gameState);

        //push game state to activate game state --> set intro to active game state
        try {
            stateManager.pushGameState("intro");
        } catch (GameStateNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreateWindow(IWindow window) {
        //set window size
        window.setSize(1280, 720);

        //set window title
        window.setTitle("2D RPG");

        //set clear color to black
        window.setClearColor(0, 0, 0, 0);

        //centralize window
        window.center();
    }

}
