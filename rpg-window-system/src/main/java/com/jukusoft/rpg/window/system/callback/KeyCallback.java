package com.jukusoft.rpg.window.system.callback;

/**
 * Created by Justin on 21.08.2016.
 */
public interface KeyCallback extends Comparable<KeyCallback>, InputEventCallback {

    /**
    * key was pressed
     *
     * @param key ascii key code
     *
     * @return true, if other key callbacks should be executed
    */
    public boolean keyPressed(int key);

    /**
     * key was released
     *
     * @param key ascii key code
     *
     * @return true, if other key callbacks should be executed
     */
    public boolean keyReleased(int key);

}
