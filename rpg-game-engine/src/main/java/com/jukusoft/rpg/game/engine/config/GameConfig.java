package com.jukusoft.rpg.game.engine.config;

import com.jukusoft.rpg.core.exception.FilePermissionException;
import com.jukusoft.rpg.game.engine.exception.GameConfigException;

import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Justin on 29.11.2016.
 */
public class GameConfig {

    /**
    * Singleton instance of game config
    */
    protected static GameConfig instance = null;

    /**
    * initialization flag
    */
    protected static AtomicBoolean isInitialized = new AtomicBoolean(false);

    protected GameConfig () {
        //
    }

    /**
    * get current instance of game config
     *
     * @return instance of game config
    */
    public static GameConfig getInstance () {
        //check, if game config was initialized
        if (!isInitialized.get()) {
            throw new IllegalStateException("GameConfig wasnt initialized. Call GameConfig.init() first.");
        }

        if (instance == null) {
            throw new IllegalStateException("GameConfig instance is null, maybe it wasnt initialized?");
        }

        return instance;
    }

    /**
    * initialize game configuration
     *
     * @param file an directory or an file
    */
    public static void init (File file) throws GameConfigException, FilePermissionException {
        //check, if configuration was already initialized
        if (isInitialized.get()) {
            throw new IllegalStateException("GameConfig was already initialized.");
        }

        //create new instance
        instance = new GameConfig();

        //check, if file or directory exists
        if (!file.exists()) {
            //throw exception
            throw new GameConfigException("Configuration file or directory '" + file.getAbsolutePath() + "' doesnt exists.");
        }

        //check, if file is an directory
        if (file.isDirectory()) {
            //load config directory
            instance.loadDir(file);
        } else {
            //load config file
            instance.loadFile(file);
        }
    }

    /**
    * load all configuration files from directory
    */
    public void loadDir (File configDir) throws FilePermissionException {
        //check, if file is an directory
        if (!configDir.isDirectory()) {
            throw new IllegalStateException("configuration path '" + configDir.getAbsolutePath() + "' isnt an directory.");
        }

        //check file permissions
        if (!configDir.canRead()) {
            throw new FilePermissionException("wrong file permissions, cannot read configuration directory: " + configDir.getAbsolutePath());
        }

        //get all files from directory
        File[] files = configDir.listFiles();

        //iterate through all files
        for (File configFile : files) {
            //load configuration file
            instance.loadFile(configFile);
        }
    }

    /**
     * load configuration files
     */
    public void loadFile (File configFile) throws FilePermissionException {
        //check, if file is an directory
        if (!configFile.isFile()) {
            throw new IllegalStateException("configuration path '" + configFile.getAbsolutePath() + "' isnt an file.");
        }

        //check file permissions
        if (!configFile.canRead()) {
            throw new FilePermissionException("wrong file permissions, cannot read configuration file: " + configFile.getAbsolutePath());
        }
    }

}
