package com.jukusoft.rpg.window.system.glfw;

import org.lwjgl.glfw.GLFWErrorCallback;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Created by Justin on 21.08.2016.
 */
public class GLFWUtils {

    private static GLFWErrorCallback errorCallback = null;

    private static AtomicBoolean wasShutdown = new AtomicBoolean(false);

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

        //register shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            //execute code on shutdown
            shutdownGLFW();
        }));
    }

    public static void shutdownGLFW () {
        //GLFW only needs to shutdown one time
        if (wasShutdown.get()) {
            return;
        }

        // Terminate GLFW and release the GLFWerrorfun
        glfwTerminate();

        //release memory of error callback
        errorCallback.free();

        //set flag
        wasShutdown.set(true);
    }

}
