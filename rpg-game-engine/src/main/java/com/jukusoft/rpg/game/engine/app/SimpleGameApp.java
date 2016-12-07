package com.jukusoft.rpg.game.engine.app;

import com.jukusoft.rpg.core.logger.GameLogger;
import com.jukusoft.rpg.game.engine.utils.GamePlatform;
import com.jukusoft.rpg.game.engine.utils.Timer;
import com.jukusoft.rpg.window.system.IWindow;
import com.jukusoft.rpg.window.system.callback.AbstractKeyCallback;
import com.jukusoft.rpg.window.system.glfw.GLFWUtils;
import com.jukusoft.rpg.window.system.glfw.GLFWWindow;
import org.apache.log4j.Logger;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Justin on 21.08.2016.
 */
public abstract class SimpleGameApp implements GameApp {

    /**
    * window
    */
    protected IWindow window = null;

    /**
    * flag, if multi threading should be used and
    */
    protected final boolean useMultiThreading;

    /**
    * exit flag
    */
    protected AtomicBoolean exitFlag = new AtomicBoolean(false);

    /**
    * fixed frames per second rate flag
     *
     * if true, game application will not execute render method more than fixed fps
    */
    protected AtomicBoolean fixedFPS = new AtomicBoolean(false);

    /**
     * frames per second (fps) count
     */
    protected AtomicLong fps = new AtomicLong(1);

    /**
     * timestamp of last render execution
     */
    protected AtomicLong lastRendererTimestamp = new AtomicLong(1);

    /**
     * calculate updates per second and call call update() method near this rate
     */
    protected final AtomicBoolean useUPS = new AtomicBoolean(true);

    /**
     * target frames per second rate
     *
     * by default 60 frames per second
     */
    protected final AtomicInteger targetFPS = new AtomicInteger(60);

    /**
    * target updates per second rate
     *
     * by default 60 updates per second
    */
    protected final AtomicInteger targetUPS = new AtomicInteger(60);

    /**
    * updates per second timer
    */
    protected final Timer upsTimer = new Timer();

    /**
    * frames per second timer
    */
    protected final Timer fpsTimer = new Timer();

    /**
    * last update per second counter
    */
    protected final AtomicInteger lastUPSCounter = new AtomicInteger(0);

    /**
     * last frames per second counter
     */
    protected final AtomicInteger lastFPSCounter = new AtomicInteger(0);

    /**
    * the update keep up warning message
    */
    private static final String UPDATE_KEEP_UP_WARNING =
            "Can't keep up with update rate, maybe it's to high or other tasks are slowing down the execution.";

    protected boolean vSync = false;

    /**
    * default constructor
     *
     * @param useMultiThreading true, if updates should be executed in extra update thread
    */
    public SimpleGameApp(boolean useMultiThreading, int targetFPS, int targetUPS, boolean vSync) {
        this.useMultiThreading = useMultiThreading;

        if (targetFPS < 1) {
            this.fixedFPS.set(false);
        } else {
            this.fixedFPS.set(true);
            this.targetFPS.set(targetFPS);
        }

        if (targetUPS < 1) {
            this.useUPS.set(false);
        } else {
            this.useUPS.set(true);
            this.targetUPS.set(targetUPS);
        }

        if (!this.useMultiThreading && this.useUPS.get()) {
            throw new IllegalArgumentException("fixed updates per second is only available if multi threading is enabled.");
        }

        this.vSync = vSync;
    }

    /**
     * default constructor
     */
    public SimpleGameApp() {
        this.useMultiThreading = true;
        this.fixedFPS.set(true);
        this.targetFPS.set(60);
    }

    @Override
    public void init () {
        //log message
        Logger.getRootLogger().info("initialize GLFW now.");

        //initialize GLFW
        GLFWUtils.init();
    }

    @Override
    public void start () {
        //create new GLFW window
        this.window = new GLFWWindow(600, 400, "Simple Game App", this.vSync);
        this.window.create();

        //set window title
        window.setTitle("Window Title");

        this.onCreateWindow(window);

        //set key callback
        window.addKeyCallback(new AbstractKeyCallback() {
            @Override
            public boolean keyPressed(int key) {
                //Logger.getRootLogger().info("key pressed.");

                //execute other callbacks
                return true;
            }

            @Override
            public boolean keyReleased(int key) {
                //Logger.getRootLogger().info("key released.");

                //execute other callbacks
                return true;
            }
        });

        //show window
        window.setVisible(true);

        //prepare rendering and create GL capabilities like GL.createCapabilities()
        window.prepareRendering();

        //initialize game
        this.initialize();

        float elapsedTime = 0;

        /**
         * for frames per second calculation
         */
        int lastFPSSecond = 0;
        int currentFPSSecond = 0;
        int fpsCounter = 0;

        this.window.clear();

        //start renderer and gameloop
        if (!this.useMultiThreading) {
            Logger.getRootLogger().info("multi threading for game engine isnt enabled, use only one thread to update and render game.");

            //renderer loop
            while (!window.shouldClose()) {
                elapsedTime = fpsTimer.getElapsedTime();

                //get current second for fps calculation
                currentFPSSecond = (int) System.currentTimeMillis() / 1000;

                //check, if its the same second
                if (currentFPSSecond != lastFPSSecond) {
                    //save ups
                    this.lastFPSCounter.set(fpsCounter);

                    //set new last second
                    lastFPSSecond = currentFPSSecond;

                    //log ups counter
                    //Logger.getRootLogger().debug("FPS: " + this.lastFPSCounter.get() + ", UPS: " + this.lastUPSCounter.get());

                    //reset counter
                    fpsCounter = 0;
                }

                //process input events and call callbacks
                window.processInput();

                //update game state
                this.update(1);

                //execute tasks which should be executed in update thread
                GamePlatform.executeUpdateQueue();

                //clear framebuffer
                //glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
                window.clear();

                if (GameLogger.isRendererDebugMode()) {
                    GameLogger.debug("SimpleGameApp", "render.");
                }

                //render
                this.render();

                //increment frames per second counter
                fpsCounter++;

                //swap back and front buffers
                window.swap();

                //execute tasks which should be executed in ui thread
                GamePlatform.executeUIQueue();

                if (this.fixedFPS.get()) {
                    syncFPS(this.fpsTimer.getLastLoopExecutionTime());
                }
            }
        } else {
            Logger.getRootLogger().info("multi threading for game engine is enabled, create new thread for gameloop.");

            //create new thread for updates
            this.createUpdateThread();

            //renderer loop
            while (!window.shouldClose()) {
                elapsedTime = fpsTimer.getElapsedTime();

                //get current second for fps calculation
                currentFPSSecond = (int) System.currentTimeMillis() / 1000;

                //check, if its the same second
                if (currentFPSSecond != lastFPSSecond) {
                    //save ups
                    this.lastFPSCounter.set(fpsCounter);

                    //set new last second
                    lastFPSSecond = currentFPSSecond;

                    //log ups counter
                    //Logger.getRootLogger().debug("FPS: " + this.lastFPSCounter.get() + ", UPS: " + this.lastUPSCounter.get());

                    //reset counter
                    fpsCounter = 0;
                }

                //process input events and call callbacks
                window.processInput();

                //clear framebuffer
                //glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
                window.clear();

                if (GameLogger.isRendererDebugMode()) {
                    GameLogger.debug("SimpleGameApp", "render.");
                }

                //render
                this.render();

                //increment frames per second counter
                fpsCounter++;

                //swap back and front buffers
                window.swap();

                //execute tasks which should be executed in ui thread
                GamePlatform.executeUIQueue();

                if (this.fixedFPS.get()) {
                    syncFPS(this.fpsTimer.getLastLoopExecutionTime());
                }
            }

            //set exit flag, if it wasnt set before
            this.exitFlag.set(true);
        }

        Logger.getRootLogger().info("window was closed by user.");

        //shutdown game engine
        this.shutdown();
    }

    protected void createUpdateThread () {
        //create new update thread
        Thread updateThread = new Thread(() -> {
            /**
             * for updates per second execution
             */
            int lastSecond = 0;
            int currentSecond = 0;
            int upsCounter = 0;

            float elapsedTime;
            float accumulator = 0f;
            float interval = 1f / this.targetUPS.get();

            //start gameloop
            while (!exitFlag.get() && !Thread.currentThread().isInterrupted()) {
                if (this.useUPS.get()) {
                    elapsedTime = upsTimer.getElapsedTime();
                    accumulator += elapsedTime;

                    //get current second for ups calculation
                    currentSecond = (int) System.currentTimeMillis() / 1000;

                    //check, if its the same second
                    if (currentSecond != lastSecond) {
                        //save ups
                        this.lastUPSCounter.set(upsCounter);

                        //set new last second
                        lastSecond = currentSecond;

                        //log ups counter
                        //Logger.getRootLogger().debug("UPS: " + this.lastUPSCounter.get());

                        //reset counter
                        upsCounter = 0;
                    }

                    while (accumulator >= interval) {
                        //update game state
                        updateGame(interval);

                        accumulator -= interval;

                        //increment updates per second counter
                        upsCounter++;
                    }

                    //wait
                    syncUPS(upsTimer.getLastLoopExecutionTime());
                } else {
                    updateGame(1);
                }
            }

            Logger.getRootLogger().info("shutdown gameloop now.");
        });

        //set name of thread
        updateThread.setName("updateThread");

        //log message
        Logger.getRootLogger().info("start update thread now.");

        //start thread
        updateThread.start();
    }

    private void syncUPS (double loopStartTime) {
        float loopSlot = 1f / this.targetUPS.get();
        double endTime = loopStartTime + loopSlot;

        while(upsTimer.getTime() < endTime) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException ie) {}
        }
    }

    private void syncFPS (double loopStartTime) {
        float loopSlot = 1f / this.targetFPS.get();
        double endTime = loopStartTime + loopSlot;

        while(upsTimer.getTime() < endTime) {
            try {
                //wait 1 ms
                Thread.sleep(1);
            } catch (InterruptedException ie) {}
        }
    }

    private final void updateGame (double delta) {
        //Logger.getRootLogger().debug("update.");

        //update game
        update(delta);

        //execute tasks which should be executed in update thread
        GamePlatform.executeUpdateQueue();
    }

    @Override
    public void shutdown () {
        //set exit flag
        this.exitFlag.set(true);

        //call hook
        this.beforeShutdown();

        //close window
        this.window.close();

        //log message
        Logger.getRootLogger().info("shutdown GLFW now.");

        //shutdown GLFW
        GLFWUtils.shutdownGLFW();

        //call hook
        this.afterShutdown();
    }

    protected void beforeShutdown () {
        //
    }

    protected void afterShutdown () {
        //
    }

    @Override
    public IWindow getWindow () {
        return this.window;
    }

    @Override
    public boolean wasResized () {
        return this.getWindow().wasResized();
    }

    @Override
    public void setResizedFlag (boolean resized) {
        this.getWindow().setResizedFlag(resized);
    }

    /**
    * get frames per second rate
     *
     * @return frames per second rate
    */
    @Override
    public long getFPS () {
        return this.lastFPSCounter.get();
    }

    /**
    * get updates per second rate
     *
     * @return updates per second rate
    */
    @Override
    public int getUPS () {
        return this.lastUPSCounter.get();
    }

    protected abstract void onCreateWindow (IWindow window);

    /**
    * will be called if app is initializing game
    */
    protected abstract void initialize();

    /**
    * update game state
    */
    public abstract void update (double delta);

    /**
    * render window
    */
    public abstract void render ();

}
