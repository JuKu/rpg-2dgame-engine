package com.jukusoft.rpg.window.system.glfw;

import com.jukusoft.rpg.core.color.Color;
import com.jukusoft.rpg.core.logger.GameLogger;
import com.jukusoft.rpg.window.system.IWindow;
import com.jukusoft.rpg.window.system.callback.KeyCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * Created by Justin on 21.08.2016.
 */
public class GLFWWindow implements IWindow {

    protected int width = 600;
    protected int height = 400;
    protected String title = "";

    protected int minWidth = GLFW_DONT_CARE;
    protected int maxWidth = GLFW_DONT_CARE;
    protected int minHeight = GLFW_DONT_CARE;
    protected int maxHeight = GLFW_DONT_CARE;

    /**
    * GLFW window id
    */
    protected long window = 0l;

    /**
    * GLFW monitor id on which window is shown
    */
    protected long monitor = 0l;

    /**
    * key callback to process key events
    */
    protected GLFWKeyCallback keyCallback = null;

    /**
    * window resize callback to process window resize events
    */
    protected GLFWWindowSizeCallback windowSizeCallback = null;

    /**
    * list with key callbacks
    */
    protected List<KeyCallback> keyCallbackList = new ArrayList<>();

    /**
    * true, if application should exit if window close button was pressed
    */
    protected AtomicBoolean exitOnClose = new AtomicBoolean(false);

    /**
    * flag if window was resized
    */
    protected AtomicBoolean wasResized = new AtomicBoolean(false);

    /**
    * flag, if rendering was prepared
    */
    protected AtomicBoolean wasPreparedRendering = new AtomicBoolean(false);

    /**
    * flag for v sync
    */
    protected AtomicBoolean vSync = new AtomicBoolean(false);

    protected int openGLMajorVersion = 3;
    protected int openGLMinorVersion = 2;

    /**
    * clear color
    */
    protected Color clearColor = new Color(1.0f, 0.0f, 0.0f, 0.0f);

    public GLFWWindow(int width, int height, String title, boolean vSync) {
        this.width = width;
        this.height = height;
        this.title = title;
        this.vSync.set(vSync);

        //http://www.glfw.org/docs/latest/group__window.html
    }

    @Override
    public void create () {
        //create new window

        //configure GLFW window - optional, the current window hints are already the default
        glfwDefaultWindowHints();

        //hide window after creation
        glfwWindowHint(GLFW_VISIBLE, GL_FALSE);

        //set window resizeable
        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);

        //use OpenGL 3.2 and above, dont use legacy OpenGL - use highest possible version of OpenGL
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, this.openGLMajorVersion);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, this.openGLMinorVersion);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);

        //set monitor to primary monitor, if monitor != NULL window will be shown in fullscreen mode
        this.monitor = NULL;//glfwGetPrimaryMonitor();

        //create GLFW window
        this.window = glfwCreateWindow(this.width, this.height, this.title, this.monitor, NULL);

        //check, if error occours while creating window
        if (window == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        //setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(this.window, this.keyCallback = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                    glfwSetWindowShouldClose(window, true); //we will detect this in our rendering loop

                    if (exitOnClose.get()) {
                        //close and destroy window
                        GLFWWindow.this.close();

                        //cleanup GLFW
                        GLFWUtils.shutdownGLFW();

                        //exit
                        System.exit(0);
                    }
                }

                //call key callbacks
                callKeyCallbacks(window, key, scancode, action, mods);
            }
        });

        // Setup resize callback
        glfwSetWindowSizeCallback(this.window, windowSizeCallback = new GLFWWindowSizeCallback() {
            @Override
            public void invoke(long window, int width, int height) {
                GLFWWindow.this.width = width;
                GLFWWindow.this.height = height;
                GLFWWindow.this.wasResized.set(true);
            }
        });

        //getresolution of the primary monitor
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

        //center window
        glfwSetWindowPos(
                window,
                (vidmode.width() - this.width) / 2,
                (vidmode.height() - this.height) / 2
        );

        //make the OpenGL context current
        glfwMakeContextCurrent(this.window);

        //check if vsync should be enabled
        if (this.vSync.get()) {
            // Enable v-sync
            glfwSwapInterval(1);
        }

        this.prepareRendering();
    }

    /**
    * set visibility of window
     *
     * @param visible visibility of window
    */
    @Override
    public void setVisible (boolean visible) {
        if (visible) {
            //make the window visible
            glfwShowWindow(this.window);
        } else {
            //hide window
            glfwHideWindow(this.window);
        }
    }

    /**
    * set window title
     *
     * @param title title of window
    */
    @Override
    public void setTitle (String title) {
        //set window title
        glfwSetWindowTitle(this.window, title);

        this.title = title;
    }

    /**
    * set window size
     *
     * @param width width of window
     * @param height height of window
    */
    @Override
    public void setSize (int width, int height) {
        //set window size
        glfwSetWindowSize(this.window, width, height);
    }

    @Override
    public int getWidth() {
        //create integer buffer to get width and height
        /*IntBuffer widthBuffer = IntBuffer.allocate(1);
        IntBuffer heightBuffer = IntBuffer.allocate(1);

        //get window size
        glfwGetWindowSize(this.window, widthBuffer, heightBuffer);

        return widthBuffer.get();*/

        return this.width;
    }

    @Override
    public int getHeight() {
        //create integer buffer to get width and height
        /*IntBuffer widthBuffer = IntBuffer.allocate(1);
        IntBuffer heightBuffer = IntBuffer.allocate(1);

        //get window size
        glfwGetWindowSize(this.window, widthBuffer, heightBuffer);

        return heightBuffer.get();*/

        return this.height;
    }

    @Override
    public void setMinSize(int width, int height) {
        this.minWidth = width;
        this.minHeight = height;

        this.updateMinAndMaxSize();
    }

    @Override
    public void setMaxSize(int width, int height) {
        this.maxWidth = width;
        this.maxHeight = height;

        this.updateMinAndMaxSize();
    }

    /**
    * update minimum and maximum window size
    */
    private void updateMinAndMaxSize () {
        glfwSetWindowSizeLimits(this.window, this.minWidth, this.minHeight, this.maxWidth, this.maxHeight);
    }

    /**
    * get GLFW id of monitor on which window is shown
     *
     * @return GLFW id of monitor
    */
    public long getMonitorID () {
        return glfwGetWindowMonitor(this.window);
    }

    /**
    * get GLFW id of window
     *
     * @return GLFW id of window
    */
    public long getWindowID () {
        return this.window;
    }

    @Override
    public void setPosition(int x, int y) {
        //set window position
        glfwSetWindowPos(this.window, x, y);
    }

    @Override
    public void setResizeable(boolean resizeable) {
        throw new UnsupportedOperationException("This method isnt implemented yet.");
    }

    @Override
    public void setIcon(String imagePath) throws IOException {
        throw new UnsupportedOperationException("This method isnt implemented yet.");

        /*BufferedImage iconImageBuffer = ImageIO.read(new File(imagePath));
        ByteBuffer imageBuffer = ImageUtils.convertImageToByteBuffer(iconImageBuffer);
        glfwSetWindowIcon(this.window, imageBuffer);*/
    }

    @Override
    public void setDefaultIcon() {
        throw new UnsupportedOperationException("This method isnt implemented yet.");
        //glfwSetWindowIcon(this.window, NULL);
    }

    @Override
    public void addKeyCallback(KeyCallback callback) {
        //add callback to list
        Collections.synchronizedCollection(this.keyCallbackList).add(callback);

        //sort list
        Collections.sort(Collections.synchronizedList(this.keyCallbackList));
    }

    @Override
    public void removeKeyCallback(KeyCallback callback) {
        //remove callback from list
        Collections.synchronizedCollection(this.keyCallbackList).remove(callback);
    }

    @Override
    public void prepareRendering() {
        if (wasPreparedRendering.get()) {
            throw new IllegalStateException("Window was already prepared.");
        }

        if (GameLogger.isRendererDebugMode()) {
            GameLogger.debug("GLFWWindow", "prepareRendering() for window " + getWindowID() + ".");
        }

        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the ContextCapabilities instance and makes the OpenGL
        // bindings available for use.

        //see https://github.com/lwjglgamedev/lwjglbook/blob/master/chapter01/src/main/java/org/lwjglb/game/Main.java
        GL.createCapabilities();

        //set OpenGL ViewPort
        this.setViewPort(0, 0, getWidth(), getHeight());

        // Set the clear color
        //glClearColor(1.0f, 0.0f, 0.0f, 0.0f);

        glEnable(GL_DEPTH_TEST);

        // Support for transparencies
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        this.wasPreparedRendering.set(true);

        //clear screen
        this.clear();
    }

    @Override
    public boolean shouldClose() {
        return glfwWindowShouldClose(this.window);
    }

    @Override
    public boolean wasResized() {
        return this.wasResized.get();
    }

    @Override
    public void setResizedFlag(boolean resized) {
        this.wasResized.set(resized);
    }

    @Override
    public void setExitOnClose(boolean exitOnClose) {
        this.exitOnClose.set(exitOnClose);
    }

    @Override
    public boolean isVSync() {
        return this.vSync.get();
    }

    @Override
    public void setVSync(boolean vsync) {
        this.vSync.set(vsync);
    }

    @Override
    public void setClearColor(float r, float g, float b, float a) {
        this.clearColor.setRed(r);
        this.clearColor.setGreen(g);
        this.clearColor.setBlue(b);
        this.clearColor.setAlpha(a);
    }

    @Override
    public void setViewPort(int x, int y, int width, int height) {
        if (GameLogger.isRendererDebugMode()) {
            GameLogger.debug("GLFWWindow", "set new viewport (x: " + x + ", y:" + y + ", width: " + width + ", height: " + height + ") for window " + getWindowID() + ".");
        }

        glViewport(x, y, width, height);
    }

    @Override
    public boolean isKeyPressed(int keyCode) {
        return glfwGetKey(this.window, keyCode) == GLFW_PRESS;
    }

    @Override
    public void clear() {
        if (!wasPreparedRendering.get()) {
            throw new IllegalStateException("You have to prepare rendering with method prepareRendering() first, before you can use clear() method.");
        }

        if (GameLogger.isRendererDebugMode()) {
            GameLogger.debug("GLFWWindow", "clear window " + getWindowID() + ".");
        }

        //get clear color
        final Color color = this.clearColor;

        // Set the clear color
        glClearColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());

        //clear framebuffer
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
    }

    @Override
    public void swap() {
        if (!wasPreparedRendering.get()) {
            throw new IllegalStateException("You have to prepare rendering with method prepareRendering() first, before you can swap buffers.");
        }

        if (GameLogger.isRendererDebugMode()) {
            GameLogger.debug("GLFWWindow", "swap window " + getWindowID() + ".");
        }

        //swap back and front buffer
        glfwSwapBuffers(this.window);
    }

    @Override
    public void center() {
        //get resolution of the primary monitor
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

        //center window
        glfwSetWindowPos(
                this.window,
                (vidmode.width() - width) / 2,
                (vidmode.height() - height) / 2
        );
    }

    @Override
    public void processInput() {
        if (GameLogger.isRendererDebugMode()) {
            GameLogger.debug("GLFWWindow", "process input from window " + getWindowID() + ".");
        }

        // Poll for window events. The key callback above will only be
        // invoked during this call.
        glfwPollEvents();
    }

    protected void callKeyCallbacks (long window, int key, int scancode, int action, int mods) {
        //check, if key was pressed
        if (action == GLFW_PRESS) {
            //iterate through key callbacks
            for (KeyCallback callback : this.keyCallbackList) {
                //call key callback, if return value if false, dont execute other key callbacks
                if (!callback.keyPressed(key)) {
                    break;
                }
            }
        } else if (action == GLFW_RELEASE) {
            //iterate through key callbacks
            for (KeyCallback callback : this.keyCallbackList) {
                //call key callback, if return value if false, dont execute other key callbacks
                if (!callback.keyReleased(key)) {
                    break;
                }
            }
        }
    }

    @Override
    public void close() {
        //set window should close flag to true
        glfwSetWindowShouldClose(this.window, true);

        //TODO: call close listeners

        this.destroy();
    }

    @Override
    public void destroy() {
        //release window and window callbacks
        glfwDestroyWindow(window);
    }

}
