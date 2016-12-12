package com.jukusoft.rpg.benchmark;

import com.jukusoft.rpg.core.exception.FilePermissionException;
import com.jukusoft.rpg.core.logger.GameLogger;
import com.jukusoft.rpg.core.utils.ExceptionWindow;
import com.jukusoft.rpg.game.engine.config.GameConfig;
import com.jukusoft.rpg.game.engine.exception.GameConfigException;

import java.io.File;

/**
 * Created by Justin on 12.12.2016.
 */
public class Main {

    public static void main (String[] args) {
        //initialize game logger
        GameLogger.init();

        //log
        GameLogger.info("GameMain", "app start now.");
        GameLogger.debug("GameMain", "initialize game configuration now.");

        //GameLogger.setRendererDebugMode(true);

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

        try {
            //create new instance of game application with 2 threads for renderer and update thread, unlimited fps rate and 60 updates per second, also vSync isnt enabled.
            MyBenchmarkGame game = new MyBenchmarkGame(true, -1, 60, true);

            //initialize game
            game.init();

            //start game
            game.start();
        } catch (FilePermissionException e) {
            e.printStackTrace();
            ExceptionWindow.createAndWait("Exception: wrong file permissions", "Wrong file permissons!\nStacktrace:\n" + e.getLocalizedMessage());
        } catch (Exception e) {
            e.printStackTrace();
            ExceptionWindow.createAndWait("Application Crash", "Application crashed!\nPlease copy this full message text and send it to the developer!\n\nStacktrace:\n" + e.getLocalizedMessage());
        }
    }

}
