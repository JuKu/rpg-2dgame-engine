package com.jukusoft.rpg.core.exception;

/**
 * Created by Justin on 09.12.2016.
 */
public class AssetException extends RuntimeException {

    public AssetException (final String message, Throwable e) {
        super(message, e);
    }

    public AssetException (final String message) {
        super(message);
    }

}
