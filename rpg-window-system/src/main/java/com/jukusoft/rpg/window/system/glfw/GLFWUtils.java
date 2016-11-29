package com.jukusoft.rpg.window.system.glfw;

import org.lwjgl.glfw.GLFWErrorCallback;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Created by Justin on 21.08.2016.
 */
public class GLFWUtils {

    private static GLFWErrorCallback errorCallback = null;

    /**
    * initialize GLFW system
     *
     * @link https://github.com/lwjglgamedev/lwjglbook/blob/master/chapter01/src/main/java/org/lwjglb/game/Main.java
    */
    public static void init () {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW!");
        }
    }

    public static void shutdownGLFW () {
        // Terminate GLFW and release the GLFWerrorfun
        glfwTerminate();

        //release memory of error callback
        errorCallback.free();
    }

}
