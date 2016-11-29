package com.jukusoft.rpg.core.path;

import java.io.File;

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

    public static String getGraphicDir () {
        return getDataDir() + "graphic/";
    }

    public static String getImagePath (String path) {
        //check, if user wants to access current directory
        if (path.startsWith("./")) {
            return path;
        } else if (path.startsWith("data/")) {
            return getDataDir() + path.replace("data/", "");
        } else {
            return getGraphicDir() + path;
        }
    }

    public static String getFontCacheDir () {
        String path = "./cache/font/";
        File f = new File(path);

        //check, if directory exists
        if (!f.exists()) {
            //create directory
            f.mkdirs();
        }

        return path;
    }

    public static String getCacheDir () {
        return "./cache/";
    }

    public static String getLogDir () {
        return "./logs/";
    }

}
