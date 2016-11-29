package com.jukusoft.rpg.core.exception;

/**
 * Created by Justin on 27.02.2016.
 */
public class AssetNotFoundException extends Exception {

    public AssetNotFoundException(String message, Throwable e) {
        super(message, e);
    }

    public AssetNotFoundException(String message) {
        super(message);
    }

}
