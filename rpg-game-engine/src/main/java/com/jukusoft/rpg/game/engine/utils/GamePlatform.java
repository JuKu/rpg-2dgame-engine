package com.jukusoft.rpg.game.engine.utils;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * UIPlatform to make ui updates thread safe
 *
 * Created by Justin on 31.12.2015.
 */
public class GamePlatform {

    /**
    * thread safe queue for runnables which have to be run in ui thread
    */
    protected static Queue<Runnable> uiQueue = new ConcurrentLinkedQueue<>();

    /**
     * thread safe queue for runnables which have to be run in update thread
     */
    protected static Queue<Runnable> updateQueue = new ConcurrentLinkedQueue<>();

    /**
    * run runnable later in ui thread
    */
    public static void runOnUIThread (Runnable runnable) {
        uiQueue.offer(runnable);
    }

    /**
     * run runnable later in update thread
     */
    public static void runOnUpdateThread (Runnable runnable) {
        updateQueue.offer(runnable);
    }

    public static void executeUIQueue () {
        //process queue
        while (!uiQueue.isEmpty()) {
            //get elememt from queue like FIFO system and remove item from queue
            Runnable runnable = uiQueue.poll();

            //execute runnable
            runnable.run();
        }
    }

    public static void executeUpdateQueue () {
        //process queue
        while (!updateQueue.isEmpty()) {
            //get elememt from queue like FIFO system and remove item from queue
            Runnable runnable = updateQueue.poll();

            //execute runnable
            runnable.run();
        }
    }

}
