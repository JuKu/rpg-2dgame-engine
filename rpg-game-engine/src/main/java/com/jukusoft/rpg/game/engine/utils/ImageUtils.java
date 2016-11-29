package com.jukusoft.rpg.game.engine.utils;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

/**
 * Created by Justin on 21.08.2016.
 */
public class ImageUtils {

    /**
     * convert an buffered image to byte buffer
     *
     * @param image image
     *
     * @return byte buffer
     */
    public static ByteBuffer convertImageToByteBuffer (BufferedImage image) {
        final int bytesPerPixel = 4;//RGBA
        final int height = image.getHeight();
        final int width = image.getWidth();

        //create new byte array
        byte[] bufferArray = new byte[image.getWidth() * image.getHeight() * bytesPerPixel];

        int pixelCounter = 0;

        //iterate through all pixels
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                //get color of pixel - integer has 4 bytes, 1 byte red, 1 byte green, 1 byte blue and 1 byte alpha value
                int color = image.getRGB(x, y);

                //add color to buffer and do bit shifting
                bufferArray[pixelCounter] = (byte) ((color << 8) >> 24);//red
                bufferArray[pixelCounter + 1] = (byte) ((color << 16) >> 24);//green
                bufferArray[pixelCounter + 2] = (byte) ((color << 24) >> 24);//blue
                bufferArray[pixelCounter + 3] = (byte) (color >> 24);//alpha

                pixelCounter += 4;
            }
        }

        //convert to byte buffer and return buffer
        return ByteBuffer.wrap(bufferArray);
    }

}
