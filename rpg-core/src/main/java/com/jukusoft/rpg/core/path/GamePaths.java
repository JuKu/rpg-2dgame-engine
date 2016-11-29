package com.jukusoft.rpg.core.path;

/**
 * Created by Justin on 29.11.2016.
 */
public class GamePaths {

    public static String getDataDir () {
        //TODO: read from GameConfig

        return "./data/";
    }

    public static String getConfigDir () {
        return getDataDir() + "config/";
    }

    public static String getShaderDir () {
        return getDataDir() + "shader/";
    }

}
