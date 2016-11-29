package com.jukusoft.rpg.core.asset;

import com.jukusoft.rpg.core.utils.LocalUniqueID;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Justin on 29.11.2016.
 */
public abstract class Asset {

    /**
    * reference counter
    */
    private final AtomicInteger refCounter = new AtomicInteger(1);

    /**
    * list with cleanUp listeners
    */
    private final List<AssetCleanUpListener> cleanUpListenerList = new ArrayList<>();

    /**
    * local unique asset id
    */
    private final long assetID = LocalUniqueID.generateID();

    public final void release () {
        //decrement reference counter and check, if reference counter is 0
        if (this.refCounter.decrementAndGet() == 0) {
            //cleanUp asset, because it isnt used anymore
            this.callCleanUp();
        }
    }

    public final void incrementReference () {
        //check, if reference counter is 0
        if (this.refCounter.get() <= 0) {
            throw new IllegalStateException("Cannot increment reference counter, reference counter was already 0, so maybe asset was already cleaned up.");
        }

        //increment reference counter
        this.refCounter.incrementAndGet();
    }

    /**
    * get reference count
     *
     * @reference count
    */
    public final int refCount () {
        return this.refCounter.get();
    }

    /**
    * get local unique asset ID
    */
    public final long getAssetID () {
        return this.assetID;
    }

    public final void addCleanUpListener (AssetCleanUpListener listener) {
        if (this.refCounter.get() <= 0) {
            throw new IllegalStateException("You cannot add an cleanUp listener to an asset (id " + getAssetID() + "), if asset was already cleaned up.");
        }

        //add listener to list
        Collections.synchronizedList(this.cleanUpListenerList).add(listener);
    }

    public final void removeCleanUpListener (AssetCleanUpListener listener) {
        //remove listener from list
        Collections.synchronizedList(this.cleanUpListenerList).remove(listener);
    }

    public final void callCleanUp () {
        //set reference counter to 0
        this.refCounter.set(0);

        //iterate through listeners
        for (AssetCleanUpListener listener : this.cleanUpListenerList) {
            //call listener
            listener.cleanUp(this.getAssetID(), this);
        }

        //clear list
        this.cleanUpListenerList.clear();
    }

    /**
    * cleanUp memory resources, can be overriden by sub classes
    */
    public void cleanUp () {
        //
    }

}
