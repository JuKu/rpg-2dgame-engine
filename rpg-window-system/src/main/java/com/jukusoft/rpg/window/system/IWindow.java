package com.jukusoft.rpg.window.system;

import com.jukusoft.rpg.window.system.callback.KeyCallback;

import java.io.IOException;

/**
 * Created by Justin on 21.08.2016.
 */
public interface IWindow {

    /**
    * create window
    */
    public void create();

    /**
     * set visibility of window
     *
     * @param visible visibility of window
     */
    public void setVisible(boolean visible);

    /**
     * set window title
     *
     * @param title title of window
     */
    public void setTitle(String title);

    /**
     * set window size
     *
     * @param width width of window
     * @param height height of window
     */
    public void setSize(int width, int height);

    /**
    * get width of window
     *
     * @return width of window in pixel
    */
    public int getWidth();

    /**
     * get height of window
     *
     * @return height of window in pixel
     */
    public int getHeight();

    /**
    * set minimum size of window
     *
     * @param width minimum width of window
     * @param height minimum height of window
    */
    public void setMinSize(int width, int height);

    /**
     * set maximum size of window
     *
     * @param width maximum width of window
     * @param height maximum height of window
     */
    public void setMaxSize(int width, int height);

    /**
    * set window position
    */
    public void setPosition(int x, int y);

    /**
    * set resizeable of window, if true window is resizeable, else it isnt resizeable
    */
    public void setResizeable(boolean resizeable);

    /**
    * set window icon
    */
    public void setIcon(String imagePath) throws IOException;

    /**
    * set default window icon
    */
    public void setDefaultIcon();

    /**
    * add an key callback which is called, if key was pressed or released
     *
     * @param callback key callback
    */
    public void addKeyCallback(KeyCallback callback);

    /**
     * remove key callback
     *
     * @param callback key callback
     */
    public void removeKeyCallback(KeyCallback callback);

    /**
    * prepare rendering of window
    */
    public void prepareRendering();

    /**
    * check, if window should be closed
     *
     * @return true, if window should be closed
    */
    public boolean shouldClose();

    /**
    * returns true, if window was resized
    */
    public boolean wasResized();

    /**
    * set resized flag of window
     *
     * @param resized resized flag, true if window was resized
    */
    public void setResizedFlag(boolean resized);

    /**
    * set to true, if window should exit, when exit button in window was pressed
     *
     * @param exitOnClose true, if window should exit, when exit button in window was pressed
    */
    public void setExitOnClose(boolean exitOnClose);

    /**
    * get flag, if vSync is enabled
     *
     * @return true, if vSync is enabled
    */
    public boolean isVSync();

    /**
    * set flag, if vSync is enabled
     *
     * @param vsync true, if vSync is enabled
    */
    public void setVSync(boolean vsync);

    /**
    * set window clear color
     *
     * @param r red
     * @param g green
     * @param b blue
     * @param a alpha
    */
    public void setClearColor(float r, float g, float b, float a);

    /**
    * set viewport of window
     *
     * @param x start x position
     * @param y start y position
     * @param width width of viewport
     * @param height height of viewport
    */
    public void setViewPort(int x, int y, int width, int height);

    /**
    * check, if key is pressed
     *
     * TODO: maybe caching is required, if system call requires to much performance
     *
     * @param keyCode ascii code of key
     *
     * @return true, if key is pressed
    */
    public boolean isKeyPressed(int keyCode);

    /**
    * clear window
    */
    public void clear();

    /**
    * swap buffers
    */
    public void swap();

    /**
    * poll input events
    */
    public void processInput();

    /**
    * close window
    */
    public void close();

    /**
    * destroy window
    */
    public void destroy();

}
