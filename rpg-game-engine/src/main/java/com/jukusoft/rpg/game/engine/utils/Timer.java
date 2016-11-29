package com.jukusoft.rpg.game.engine.utils;

/**
 * Created by Justin on 22.08.2016.
 */
public class Timer {

    /**
    * last loop execution time in seconds
    */
    protected volatile double lastTime = 0;

    public Timer () {
        this.lastTime = this.getTime();
    }

    /**
    * get current timestamp in seconds
     *
     * @return current timestamp in seconds
    */
    public double getTime () {
        return System.nanoTime() / 1000_000_000d;
    }

    public float getElapsedTime () {
        //get current time
        double currentTime = getTime();

        //calculate elapsed time
        float elapsedTime = (float) (currentTime - this.lastTime);

        //set new last loop execution timestam
        lastTime = currentTime;

        //return elapsed time in seconds
        return elapsedTime;
    }

    public double getLastLoopExecutionTime () {
        return lastTime;
    }

}
