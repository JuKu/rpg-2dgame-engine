package com.jukusoft.rpg.core.exception;

/**
 * Exception which is thrown if file permissions arent set correctly.
 *
 * Created by Justin on 29.11.2016.
 */
public class FilePermissionException extends RuntimeException {

    public FilePermissionException (String message) {
        super(message);
    }

}
