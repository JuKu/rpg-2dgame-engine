package com.jukusoft.rpg.game;

import com.jukusoft.rpg.game.engine.config.GameConfig;
import com.jukusoft.rpg.game.engine.exception.FilePermissionException;
import com.jukusoft.rpg.game.engine.exception.GameConfigException;
import com.jukusoft.rpg.game.engine.logger.GameLogger;
import com.jukusoft.rpg.window.system.IWindow;
import com.jukusoft.rpg.window.system.callback.AbstractKeyCallback;
import com.jukusoft.rpg.window.system.glfw.GLFWUtils;
import com.jukusoft.rpg.window.system.glfw.GLFWWindow;

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

        GameLogger.info("GameMain", "create window now.");

        //you have to initialize GLFW first (only once)
        GLFWUtils.init();

        //create window
        IWindow window = new GLFWWindow(1280, 720, "2D RPG", false);
        window.create();

        //show window
        window.setVisible(true);

        window.setExitOnClose(true);

        //prepare rendering
        window.prepareRendering();

        //set clear color
        window.setClearColor(0, 0, 0, 0);

        while (true) {
            //process input
            window.processInput();

            window.clear();

            window.swap();

            Thread.currentThread().sleep(100);
        }
    }

}
