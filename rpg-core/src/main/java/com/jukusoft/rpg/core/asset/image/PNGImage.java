package com.jukusoft.rpg.core.asset.image;

import com.jukusoft.rpg.core.exception.AssetNotFoundException;
import com.jukusoft.rpg.core.exception.UnsupportedAssetException;

import java.io.IOException;

/**
 * Created by Justin on 09.12.2016.
 */
public class PNGImage extends Image2D {

    /**
     * default constructor
     *
     * @param path
     */
    public PNGImage(String path) throws UnsupportedAssetException, AssetNotFoundException, IOException {
        super(path);
    }

}
