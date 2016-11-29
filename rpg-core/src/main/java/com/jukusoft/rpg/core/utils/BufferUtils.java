package com.jukusoft.rpg.core.utils;

import java.lang.reflect.Field;
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

    /**
    * release memory of byte buffer
     *
     * @param buffer instance of byte buffer
    */
    public static void releaseMemory (ByteBuffer buffer) {
        Field cleanerField = null;
        try {
            cleanerField = buffer.getClass().getDeclaredField("cleaner");
            cleanerField.setAccessible(true);
            sun.misc.Cleaner cleaner = (sun.misc.Cleaner) cleanerField.get(buffer);
            cleaner.clean();
        } catch (NoSuchFieldException e) {
            //e.printStackTrace();
        } catch (IllegalAccessException e) {
            //e.printStackTrace();
        }

        buffer = null;
    }

    /**
     * release memory of float buffer
     *
     * @param buffer instance of byte buffer
     */
    public static void releaseMemory (FloatBuffer buffer) {
        Field cleanerField = null;
        try {
            cleanerField = buffer.getClass().getDeclaredField("cleaner");
            cleanerField.setAccessible(true);
            sun.misc.Cleaner cleaner = (sun.misc.Cleaner) cleanerField.get(buffer);
            cleaner.clean();
        } catch (NoSuchFieldException e) {
            //e.printStackTrace();
        } catch (IllegalAccessException e) {
            //e.printStackTrace();
        }

        buffer = null;
    }

}
