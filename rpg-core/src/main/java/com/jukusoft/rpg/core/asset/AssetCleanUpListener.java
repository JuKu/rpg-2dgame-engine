package com.jukusoft.rpg.core.asset;

/**
 * Created by Justin on 29.11.2016.
 */
public interface AssetCleanUpListener {

    /**
    * cleanUp asset
     *
     * @param assetID local unique asset ID
     * @param asset instance of asset
    */
    public void cleanUp (final long assetID, final Asset asset);

}
