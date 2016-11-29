package com.jukusoft.rpg.game;

import com.jukusoft.rpg.game.engine.config.GameConfig;
import com.jukusoft.rpg.game.engine.exception.FilePermissionException;
import com.jukusoft.rpg.game.engine.exception.GameConfigException;
import com.jukusoft.rpg.game.engine.logger.GameLogger;

import java.io.File;

/**
 * Created by Justin on 29.11.2016.
 */
public class Main {

    public static void main (String[] args) throws InterruptedException {
        //initialize game logger
        GameLogger.init();

        //log
        GameLogger.info("GameMain", "app start now.");
        GameLogger.debug("GameMain", "initialize game configuration now.");

        try {
            //initialize game configuration
            GameConfig.init(new File("./data/config"));
        } catch (GameConfigException e) {
            e.printStackTrace();
            System.exit(0);
        } catch (FilePermissionException e) {
            e.printStackTrace();
            System.exit(0);
        }

        //log
        GameLogger.info("GameMain", "config parsed successfully.");

        //create new instance of game application
        MyGameApp game = new MyGameApp(true, -1, 60, false);

        //initialize game
        game.init();

        //start game
        game.start();
    }

}
