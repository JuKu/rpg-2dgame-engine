package com.jukusoft.rpg.core.logger;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

/**
 * Created by Justin on 29.11.2016.
 */
public class GameLogger {

    /**
    * initialize game logger
    */
    public static void init () {
        //initialize log4j
        BasicConfigurator.configure();
    }

    /**
    * log an debug message
     *
     * @param loggerName name of logger
     * @param message log message
    */
    public static void debug (String loggerName, String message) {
        Logger.getRootLogger().debug("[" + loggerName + "] " + message);
    }

    /**
     * log an info message
     *
     * @param loggerName name of logger
     * @param message log message
     */
    public static void info (String loggerName, String message) {
        Logger.getRootLogger().info("[" + loggerName + "] " + message);
    }

    /**
     * log an warn message
     *
     * @param loggerName name of logger
     * @param message log message
     */
    public static void warn (String loggerName, String message) {
        Logger.getRootLogger().warn("[" + loggerName + "] " + message);
    }

    /**
     * log an error message
     *
     * @param loggerName name of logger
     * @param message log message
     */
    public static void error (String loggerName, String message) {
        Logger.getRootLogger().error("[" + loggerName + "] " + message);
    }

}
