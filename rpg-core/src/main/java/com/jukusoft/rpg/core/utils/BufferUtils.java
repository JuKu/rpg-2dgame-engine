package com.jukusoft.rpg.core.utils;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

/**
 * Created by Justin on 04.09.2016.
 */
public class BufferUtils {

    /**
    * create new Off Heap FloatBuffer
     *
     * @param capacity number of capacity of float values in buffer
     *
     * @return instance of Off Heap FloatBuffer
    */
    public static FloatBuffer createFloatBuffer (int capacity) {
        //create new Off Heap byte buffer with capacity float values, one float requires 4 bytes
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * capacity);

        //convert and return float buffer
        return byteBuffer.asFloatBuffer();
    }

}
