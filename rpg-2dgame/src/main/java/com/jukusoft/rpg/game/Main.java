package com.jukusoft.rpg.game;

import com.jukusoft.rpg.core.path.GamePaths;
import com.jukusoft.rpg.core.utils.ExceptionWindow;
import com.jukusoft.rpg.core.utils.FileUtils;
import com.jukusoft.rpg.game.engine.config.GameConfig;
import com.jukusoft.rpg.core.exception.FilePermissionException;
import com.jukusoft.rpg.game.engine.exception.GameConfigException;
import com.jukusoft.rpg.core.logger.GameLogger;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.GregorianCalendar;

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
            MyGameApp game = new MyGameApp(true, -1, 60, false);

            //initialize game
            game.init();

            //start game
            game.start();
        } catch (FilePermissionException e) {
            e.printStackTrace();

            writeCrashLog(e);
            ExceptionWindow.createAndWait("Exception: wrong file permissions", "Wrong file permissons!\nStacktrace:\n" + e.getLocalizedMessage());
        } catch (Exception e) {
            e.printStackTrace();

            writeCrashLog(e);
            ExceptionWindow.createAndWait("Application Crash", "Application crashed!\nPlease copy this full message text and send it to the developer!\n\nStacktrace:\n" + e.getLocalizedMessage());
        }
    }

    /**
    * write crash log
    */
    public static void writeCrashLog (Throwable e) {
        Calendar calendar = new GregorianCalendar();

        //try to write lo
        try {
            FileUtils.writeFile(GamePaths.getLogDir() + "/app.log", "[GameMain Exception " + calendar.getTime() + ", " + System.currentTimeMillis() + "] " + e.getMessage() + ", " + e.getLocalizedMessage(), StandardCharsets.UTF_8);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

}
