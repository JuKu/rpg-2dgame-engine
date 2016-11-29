package com.jukusoft.rpg.window.system.callback;

/**
 * Created by Justin on 21.08.2016.
 */
public abstract class AbstractKeyCallback implements KeyCallback {

    protected int priority = 5;

    public AbstractKeyCallback(int priority) {
        this.priority = priority;
    }

    public AbstractKeyCallback() {
        //
    }

    @Override
    public int compareTo(KeyCallback o) {
        return new Integer(this.priority).compareTo(o.getPriority());
    }

    @Override
    public int getPriority() {
        return this.priority;
    }
}
