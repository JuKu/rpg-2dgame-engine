package com.jukusoft.rpg.game.engine.app;

import com.jukusoft.rpg.window.system.IWindow;

/**
 * Created by Justin on 23.08.2016.
 */
public interface GameApp {

    /**
    * initialize game application
    */
    public void init();

    /**
    * start game application
    */
    public void start();

    /**
    * shutdown game application
    */
    public void shutdown();

    /**
    * get application window
     *
     * @return application window
    */
    public IWindow getWindow();

    /**
    * return true, if window was resized
     *
     * @return true, if window was resized
    */
    public boolean wasResized();

    /**
    * set resized flag
     *
     * @param resized flag if window was resized
    */
    public void setResizedFlag(boolean resized);

    /**
    * get frames per second rate
     *
     * @return frames per rate
    */
    public long getFPS();

    /**
     * get updates per second rate
     *
     * @return updates per second rate
     */
    public int getUPS();

}
