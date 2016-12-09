package com.jukusoft.rpg.core.asset.image;

import com.jukusoft.rpg.core.asset.Asset;
import com.jukusoft.rpg.core.exception.AssetNotFoundException;
import com.jukusoft.rpg.core.exception.FilePermissionException;
import com.jukusoft.rpg.core.exception.UnsupportedAssetException;
import com.jukusoft.rpg.core.path.GamePaths;
import com.jukusoft.rpg.core.utils.BufferUtils;
import de.matthiasmann.twl.utils.PNGDecoder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by Justin on 29.11.2016.
 */
public class Image2D extends Asset {

    //width and height of image
    protected int width = 0;
    protected int height = 0;

    //path to image file
    protected String path = "";

    /**
    * byte buffer with RGBA values for each pixl, size of width * height * 4
    */
    protected ByteBuffer buffer = null;

    /**
    * default constructor
    */
    public Image2D(String path) throws UnsupportedAssetException, AssetNotFoundException, IOException {
        //check, if image is an png graphic
        if (!path.endsWith(".png") && !path.endsWith(".PNG")) {
            throw new UnsupportedAssetException("only PNG assets are supported! file path: " + path);
        }

        //get image path
        this.path = GamePaths.getImagePath(path);

        File file = new File(this.path);

        //check, if file exists
        if (!file.exists()) {
            throw new AssetNotFoundException("Could not found PNG image asset: " + this.path + ", original path: " + path);
        }

        //check read permissions
        if (!file.canRead()) {
            throw new FilePermissionException("Wrong file permissions, cannot read PNG image asset: " + this.path + ", original path: " + path);
        }

        this.load();
    }

    /**
    * load image
    */
    protected void load () throws IOException {
        File file = new File(this.path);

        //create new PNG decoder
        PNGDecoder pngDecoder = new PNGDecoder(new FileInputStream(file));

        //get width and height
        this.width = pngDecoder.getWidth();
        this.height = pngDecoder.getHeight();

        //create new byte buffer
        this.buffer = BufferUtils.createByteBuffer(this.width * this.height * 4);
        //BufferUtils.createByteBuffer(this.width * this.height * 4);

        //decode image and insert pixel values into byte buffer
        pngDecoder.decode(this.buffer, this.width * 4, PNGDecoder.Format.RGBA);

        //flip buffer, so reader index will be set to first position of buffer
        this.buffer.flip();
    }

    /**
    * get width of image
     *
     * @return width of image
    */
    public int getWidth () {
        return this.width;
    }

    /**
    * get height of image
     *
     * @return height of image
    */
    public int getHeight () {
        return this.height;
    }

    /**
    * get instance of direct byte buffer (Off Heap memory)
     *
     * @return instance of byte buffer
    */
    public ByteBuffer getBuffer () {
        return this.buffer;
    }

    @Override
    public void cleanUp () {
        //cleanUp buffer
        BufferUtils.releaseMemory(this.buffer);
    }

}
