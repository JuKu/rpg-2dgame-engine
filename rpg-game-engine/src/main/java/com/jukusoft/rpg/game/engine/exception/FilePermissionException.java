package com.jukusoft.rpg.game.engine.exception;

/**
 * Exception which is thrown if file permissions arent set correctly.
 *
 * Created by Justin on 29.11.2016.
 */
public class FilePermissionException extends Exception {

    public FilePermissionException (String message) {
        super(message);
    }

}
